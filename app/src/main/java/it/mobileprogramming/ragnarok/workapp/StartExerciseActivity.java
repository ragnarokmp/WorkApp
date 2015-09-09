package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivity;

public class StartExerciseActivity extends BaseActivity {

    public static final String START_EXERCISE_FRAGMENT = "StartExerciseFragment";

    private DecoView decoView;
    TextView textViewPercentage, textViewRemaining, totalRepetitions, currRepetition, totalSeries, currSeries;
    ImageView pauseImageView, stopImageView;

    CountDownTimer countDownTimer;
    Exercise cExercise;

    public int millis;
    public int repetitions;
    public int series;
    public int currentRepetition;
    public int currentSerie;
    public boolean stopped;
    public long leftTime;

    private View[] linkedViews;

    private SeriesItem seriesItem;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_start_exercise;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textViewPercentage = (TextView) findViewById(R.id.percentage_text_view);
        textViewRemaining  = (TextView) findViewById(R.id.remaining_text_view);
        totalRepetitions   = (TextView) findViewById(R.id.totalRepTV);
        currRepetition     = (TextView) findViewById(R.id.currentRepTV);
        totalSeries        = (TextView) findViewById(R.id.totalSeriesTV);
        currSeries         = (TextView) findViewById(R.id.currentSeriesTV);

        pauseImageView = (ImageView) findViewById(R.id.pause_image_view);
        stopImageView = (ImageView) findViewById(R.id.stop_image_view);

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
        if (intent.hasExtra("exerciseID")) {
            cExercise = dbSerializer.loadExercise(intent.getExtras().getInt("exerciseID"));
        }
        if (cExercise != null) {
            // setup for the very first time the exercise
            millis      = (cExercise.getFrequency() * cExercise.getRepetition()) * cExercise.getSeries();
            repetitions = cExercise.getRepetition();
            series      = cExercise.getSeries();
        } else {
            Toast.makeText(getApplicationContext(),
                           getResources().getString(R.string.internal_error),
                           Toast.LENGTH_LONG).show();
            this.finish();
        }


        if(savedInstanceState != null){
            leftTime          =   savedInstanceState.getLong("leftTime");
            currentRepetition =   savedInstanceState.getInt("currentRepetition");
            currentSerie      =   savedInstanceState.getInt("currentSerie");
            stopped           =   savedInstanceState.getBoolean("stopped");
        } else {
            leftTime          = millis;
            currentRepetition = 0;
            currentSerie      = 1;
            stopped           = false;
        }

        currRepetition.setText(String.valueOf(currentRepetition));
        totalRepetitions.setText(" / " + String.valueOf(cExercise.getRepetition()));
        currSeries.setText(String.valueOf(currentSerie));
        totalSeries.setText(" / " + String.valueOf(cExercise.getSeries()));



        // get DecoView
        decoView = (DecoView) findViewById(R.id.deco_view);
        seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.accent))
                .setRange(0, repetitions-1, 0)
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

        if (!stopped)
            startCountDownTimer((int)leftTime);
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


    /**
     * to pause the countdown timer and refresh the UI
     */
    public void onPauseButton() {
        if (pauseImageView.getTag().equals(getString(R.string.pause))) {

            stopped           = true;

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

            if (stopped && (currentRepetition != 0 || currentSerie != 1))
                stopped = false;
            else {
                leftTime          = millis;
                currentRepetition = 0;
                currentSerie      = 1;
                stopped           = false;
            }

            textViewRemaining.setVisibility(View.VISIBLE);
            textViewPercentage.setVisibility(View.VISIBLE);
            decoView.setVisibility(View.VISIBLE);
            findViewById(R.id.linear_layout_image_stop).setVisibility(View.VISIBLE);
            findViewById(R.id.linear_layout_repetition).setVisibility(View.VISIBLE);
            findViewById(R.id.linear_layout_series).setVisibility(View.VISIBLE);

            if (countDownTimer != null)
                countDownTimer.start();
            else
                startCountDownTimer((int)leftTime);

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
        leftTime          = millis;
        currentRepetition = 0;
        currentSerie      = 1;
        stopped           = true;

        currRepetition.setText(String.valueOf(currentRepetition));
        currSeries.setText(String.valueOf(currentSerie));

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
    private void startCountDownTimer(int milliseconds) {
        countDownTimer = new CountDownTimer(milliseconds + 1000, 1000) {

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                leftTime = leftTimeInMilliseconds;
                long seconds = leftTimeInMilliseconds / 1000;

                // setting UI elements
                int series1Index = decoView.addSeries(seriesItem);
                decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());

                if (currentRepetition != 0) {
                    textViewPercentage.setText(String.valueOf(currentRepetition));
                    currRepetition.setText(String.valueOf(currentRepetition));
                }


                currentRepetition++;

                // format the textview to show the easily readable format
                textViewRemaining.setText(String.format("%02d", (millis / 1000 - seconds) / 60) + "  :  " + String.format("%02d", (millis / 1000 - seconds) % 60));

                if (currentRepetition > repetitions) {
                    series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(currentRepetition + 1).setIndex(series1Index).setDelay(0).build());

                    currentRepetition = 1;
                    decoView.executeReset();
                    series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());



                    currentSerie++;
                    if (currentSerie <= series)
                        currSeries.setText(String.valueOf(currentSerie));
                }

            }

            @Override
            public void onFinish() {


                if (currentSerie >= series) {
                    int series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                            .setIndex(series1Index)
                            .setLinkedViews(linkedViews)
                            .setDelay(0)
                            .setDuration(4000)
                            .setDisplayText(getString(R.string.complete))
                            .build());

                    // TODO finishing activity??

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
                }

            }

        }.start();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("leftTime"        , leftTime);
        outState.putInt("currentRepetition", currentRepetition);
        outState.putInt("currentSerie"     , currentSerie);
        outState.putBoolean("stopped"      , stopped);
    }

}