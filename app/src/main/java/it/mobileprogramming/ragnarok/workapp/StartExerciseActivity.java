package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserExercise;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivity;

public class StartExerciseActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    public static final String START_EXERCISE_FRAGMENT = "StartExerciseFragment";

    private DecoView decoView;
    private TextView textViewPercentage;
    private TextView textViewRemaining;
    private TextView currRepetition;
    private TextView currSeries;
    private ImageView pauseImageView;

    private UserExercise currentExercise;
    private CountDownTimer countDownTimer;

    public int millis;
    public int repetitions;
    public int series;
    public int frequency;
    public int currentRepetition;
    public int currentSeries;
    public boolean stopped;
    public long leftTime;
    private boolean inRecovery = false;
    private int recoveryValue = 0;
    private long startRecovery;
    private View[] linkedViews;

    private SeriesItem seriesItem;
    private SeriesItem seriesItemRec;
    private TextToSpeech textToSpeech;
    private int previous_value;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_start_exercise;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get all views
        textViewPercentage = (TextView) findViewById(R.id.percentage_text_view);
        textViewRemaining = (TextView) findViewById(R.id.remaining_text_view);
        TextView totalRepetitions = (TextView) findViewById(R.id.totalRepTV);
        currRepetition = (TextView) findViewById(R.id.currentRepTV);
        TextView totalSeries = (TextView) findViewById(R.id.totalSeriesTV);
        currSeries = (TextView) findViewById(R.id.currentSeriesTV);

        pauseImageView = (ImageView) findViewById(R.id.pause_image_view);

        final View layout = findViewById(R.id.frame_layout);
        final ImageView pauseImageView = (ImageView) findViewById(R.id.pause_image_view);
        final ImageView stopImageView = (ImageView) findViewById(R.id.stop_image_view);

        if (getSupportFragmentManager().findFragmentByTag(START_EXERCISE_FRAGMENT) == null ) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            StartExerciseFragment fragment = new StartExerciseFragment();
            transaction.add(fragment, START_EXERCISE_FRAGMENT);
            transaction.commit();
        }

        // Get exercise info
        Intent intent = getIntent();
        SQLiteSerializer dbSerializer = ((App) getApplication()).getDBSerializer();
        dbSerializer.open();

        if (intent.hasExtra("exercise")) {
            currentExercise = intent.getExtras().getParcelable("exercise");
            if (currentExercise != null) {
                currentExercise.setSerializer(((App) getApplication()).getDBSerializer());
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.internal_error), Toast.LENGTH_LONG).show();
            finish();
        }

        if (currentExercise != null) {
            // setup for the very first time the exercise
            millis = (((currentExercise.getRepetition() / currentExercise.getFrequency()) * currentExercise.getSeries()) + currentExercise.getRecovery() * (currentExercise.getSeries() - 1))*1000;
            repetitions = currentExercise.getRepetition();
            series = currentExercise.getSeries();
            frequency = currentExercise.getFrequency();
        } else {
            // Else show error and close activity
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internal_error), Toast.LENGTH_LONG).show();
            this.finish();
        }

        // Try to recover from previous state
        if (savedInstanceState != null) {
            leftTime = savedInstanceState.getLong("leftTime");
            currentRepetition = savedInstanceState.getInt("currentRepetition");
            currentSeries = savedInstanceState.getInt("currentSeries");
            stopped = savedInstanceState.getBoolean("stopped");
            this.startRecovery = savedInstanceState.getLong("startRecovery");
            this.inRecovery = savedInstanceState.getBoolean("inRecovery");
            this.recoveryValue = savedInstanceState.getInt("recoveryValue");
        } else {
            leftTime = millis;
            currentRepetition = 0;
            currentSeries = 1;
            stopped = false;
        }
        
        // Set empty text
        currRepetition.setText(String.valueOf(currentRepetition));
        totalRepetitions.setText(" / " + String.valueOf(currentExercise.getRepetition()));
        currSeries.setText(String.valueOf(currentSeries));
        totalSeries.setText(" / " + String.valueOf(currentExercise.getSeries()));

        // Get DecoView
        decoView = (DecoView) findViewById(R.id.deco_view);
        seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.accent))
                .setRange(0, repetitions + 2, 0)
                .setLineWidth(32f)
                .setSpinDuration(101)
                .build();

        seriesItemRec = new SeriesItem.Builder(getResources().getColor(R.color.recovery))
                .setRange(0, currentExercise.getRecovery() + 2, 0)
                .setLineWidth(32f)
                .setSpinDuration(101)
                .build();

        decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(0)
                .setDuration(0)
                .build());

        // Group some of them for final animation
        linkedViews = new View[]{textViewPercentage, textViewRemaining, layout};

        // attaching listeners
        pauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseButton();
            }
        });

        stopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStopButton();
            }
        });

        // Get SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get boolean for sound and voice
        boolean audible = sharedPreferences.getBoolean(getResources().getString(R.string.switch_sound_preference_key), false);
        boolean voiced = sharedPreferences.getBoolean(getResources().getString(R.string.voice_preference_key), false);

        // Initialize TextToSpeech if is audible and voiced, else start timer
        if (audible && voiced) {
            textToSpeech = new TextToSpeech(this, this);
        } else {
            if (!stopped) {
                startCountDownTimer((int) leftTime);
            }
        }
    }

    /**
     * to pause the countdown timer and refresh the UI
     */
    public void onPauseButton() {
        if (pauseImageView.getTag().equals(getString(R.string.pause))) {

            decoView.setVisibility(View.VISIBLE);
            textViewPercentage.setVisibility(View.VISIBLE);

            stopped = true;

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            pauseImageView.setTag(getString(R.string.play));

            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getDrawable(R.drawable.ic_action_play);
            } else {
                drawable = getResources().getDrawable(R.drawable.ic_action_play);
            }

            pauseImageView.setImageDrawable(drawable);
            pauseImageView.setContentDescription(getString(R.string.play));
            pauseImageView.setTag(getString(R.string.play));
        }
        else if (pauseImageView.getTag().equals(getString(R.string.play))) {

            if (stopped && (currentRepetition != 0 || currentSeries != 1)) {
                stopped = false;
            } else {
                leftTime = millis;
                currentRepetition = 0;
                currentSeries = 1;
                stopped = false;
            }

            textViewRemaining.setVisibility(View.VISIBLE);
            textViewPercentage.setVisibility(View.VISIBLE);
            decoView.setVisibility(View.VISIBLE);
            findViewById(R.id.linear_layout_image_stop).setVisibility(View.VISIBLE);
            findViewById(R.id.linear_layout_repetition).setVisibility(View.VISIBLE);
            findViewById(R.id.linear_layout_series).setVisibility(View.VISIBLE);

            if (countDownTimer != null) {
                countDownTimer.start();

            } else {
                startCountDownTimer(leftTime);
            }

            pauseImageView.setTag(getString(R.string.pause));

            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getDrawable(R.drawable.ic_action_pause);
            } else {
                drawable = getResources().getDrawable(R.drawable.ic_action_pause);
            }

            pauseImageView.setImageDrawable(drawable);
            pauseImageView.setContentDescription(getString(R.string.pause));
            pauseImageView.setTag(getString(R.string.pause));
        }
    }

    /**
     * to stop the countdown timer
     */
    public void onStopButton() {

        decoView.setVisibility(View.INVISIBLE);
        textViewPercentage.setVisibility(View.INVISIBLE);

        leftTime = millis;
        currentRepetition = 0;
        currentSeries = 1;
        stopped = true;

        currRepetition.setText(String.valueOf(currentRepetition));
        currSeries.setText(String.valueOf(currentSeries));

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
            countDownTimer = null;
        }

        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getDrawable(R.drawable.ic_action_play);
        } else {
            drawable = getResources().getDrawable(R.drawable.ic_action_play);
        }

        pauseImageView.setImageDrawable(drawable);
        pauseImageView.setContentDescription(getString(R.string.play));
        pauseImageView.setTag(getString(R.string.play));

    }

    /**
     * this is the countdown time function
     * @param milliseconds: int, duration of the exercise
     */
    private void startCountDownTimer(long milliseconds) {
        countDownTimer = new CountDownTimer(milliseconds + 1000, 1000 / frequency) {

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                leftTime = leftTimeInMilliseconds;
                long seconds = leftTimeInMilliseconds / 1000;
                Log.i(TAG, "Second: " + String.valueOf(seconds) + " and currentRepetition: " + String.valueOf(currentRepetition) + " and ");

                int series1Index;

                if (!inRecovery) {
                    series1Index = decoView.addSeries(seriesItem);
                    textViewPercentage.setText(String.valueOf(currentRepetition));

                    if (textToSpeech != null && previous_value != currentRepetition) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.setSpeechRate(frequency);
                            textToSpeech.speak(String.valueOf(currentRepetition), TextToSpeech.QUEUE_ADD, null);
                            previous_value = currentRepetition;
                        } else {
                            textToSpeech.setSpeechRate(frequency);
                            textToSpeech.speak(String.valueOf(currentRepetition), TextToSpeech.QUEUE_ADD, null, "pippo");
                            previous_value = currentRepetition;
                        }
                    }

                    currRepetition.setText(String.valueOf(currentRepetition));
                    currSeries.setText(String.valueOf(currentSeries));
                    decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());

                    currentRepetition += 1;
                }

                // Set text for time passed
                textViewRemaining.setText(String.format("%02d", (millis / 1000 - seconds) / 60) + "  :  " + String.format("%02d", (millis / 1000 - seconds) % 60));

                if (currentRepetition >= repetitions && !inRecovery) {

                    // setting UI elements
                    series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());
                    inRecovery = true;
                    startRecovery = System.currentTimeMillis();
                    currRepetition.setText(String.valueOf(currentRepetition));
                    series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());

                    //currentRepetition = 0;
                    decoView.executeReset();
                    decoView.deleteAll();
                    textViewPercentage.setTextColor(getResources().getColor(R.color.recovery));
                    textViewPercentage.setText(0 + "");
                    series1Index = decoView.addSeries(seriesItemRec);
                    decoView.addEvent(new DecoEvent.Builder(recoveryValue).setIndex(series1Index).setDelay(0).build());
                }

                if (inRecovery) {
                    //check if recovery is finished
                    //restore decoview
                    long elapsed = System.currentTimeMillis()- startRecovery;
                    textViewPercentage.setTextColor(getResources().getColor(R.color.recovery));

                    int value = (int) elapsed / 1000;
                    textViewPercentage.setText(String.valueOf(value));

                    if (textToSpeech != null && previous_value != value) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.speak(String.valueOf(value), TextToSpeech.QUEUE_ADD, null);
                            previous_value = value;
                        } else {
                            textToSpeech.speak(String.valueOf(value), TextToSpeech.QUEUE_ADD, null, "pippo");
                            previous_value = value;
                        }
                    }

                    recoveryValue = (int) elapsed/1000;
                    series1Index = decoView.addSeries(seriesItemRec);
                    decoView.addEvent(new DecoEvent.Builder(recoveryValue).setIndex(series1Index).setDelay(0).build());

                    if (((int)elapsed/1000) >= currentExercise.getRecovery()) {
                        currentRepetition = 0;
                        recoveryValue = 0;
                        currentSeries++;
                        inRecovery = false;
                        decoView.executeReset();
                        decoView.deleteAll();
                        seriesItem.setColor(getResources().getColor(R.color.accent));
                        textViewPercentage.setTextColor(getResources().getColor(R.color.accent));
                        series1Index = decoView.addSeries(seriesItem);
                        decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());
                    }
                }
            }

            @Override
            public void onFinish() {
                if (currentSeries >= series) {

                    int series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                            .setIndex(series1Index)
                            .setLinkedViews(linkedViews)
                            .setDelay(0)
                            .setDuration(4000)
                            .setDisplayText(getString(R.string.complete))
                            .setListener(new DecoEvent.ExecuteEventListener() {
                                @Override
                                public void onEventStart(DecoEvent decoEvent) {

                                }

                                @Override
                                public void onEventEnd(DecoEvent event) {
                                    currentExercise.setDone(true);
                                    finish();
                                }
                            })
                            .build());

                    //TODO dare più tempo all'animazione
                    finish();

                } else {
                    decoView.executeReset();

                    pauseImageView.setTag(getString(R.string.play));
                    textViewRemaining.setText("- - : - -");

                    Drawable drawable;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable = getDrawable(R.drawable.ic_action_play);
                    } else {
                        drawable = getResources().getDrawable(R.drawable.ic_action_play);
                    }

                    pauseImageView.setImageDrawable(drawable);
                    pauseImageView.setContentDescription(getString(R.string.play));
                    pauseImageView.setTag(getString(R.string.play));

                    currRepetition.setText(String.valueOf(0));
                    currSeries.setText(String.valueOf(0));
                    finish();
                }
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Set flag in order to keep screen off
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set flag in order to keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (stopped) {
            decoView.setVisibility(View.VISIBLE);
            int series1Index = decoView.addSeries(seriesItem);
            decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());

            pauseImageView.setTag(getString(R.string.play));
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getDrawable(R.drawable.ic_action_play);
            } else {
                drawable = getResources().getDrawable(R.drawable.ic_action_play);
            }

            pauseImageView.setImageDrawable(drawable);
            pauseImageView.setContentDescription(getString(R.string.play));
            pauseImageView.setTag(getString(R.string.play));
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putLong("leftTime", leftTime);
        outState.putInt("currentRepetition", currentRepetition);
        outState.putInt("currentSeries", currentSeries);
        outState.putBoolean("stopped", stopped);
        outState.putLong("startRecovery", this.startRecovery);
        outState.putBoolean("inRecovery", this.inRecovery);
        outState.putInt("recoveryValue", this.recoveryValue);
    }

    @Override
    public void onInit(int status) {
        // If TextToSpeech is ready start the timer
        if (status == TextToSpeech.SUCCESS) {
            if (!stopped) {
                startCountDownTimer((int) leftTime);
            }
        } else {
            // There is some problems with TextToSpeech start without it
            textToSpeech = null;
            if (!stopped) {
                startCountDownTimer((int) leftTime);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources used by TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }
}