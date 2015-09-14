package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserExercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivity;

public class StartExerciseActivity extends BaseActivity {

    public static final String START_EXERCISE_FRAGMENT = "StartExerciseFragment";

    private DecoView decoView;
    TextView textViewPercentage, textViewRemaining, totalRepetitions, currRepetition, totalSeries, currSeries;
    ImageView pauseImageView, stopImageView;

    UserWorkoutSession userWorkoutSession;
    int exerciseID;
    UserExercise cExercise;
    CountDownTimer countDownTimer;

    public int millis;
    public int repetitions;
    public int series;
    public int frequency;
    public int currentRepetition;
    public int currentSerie;
    public boolean stopped;
    public long leftTime;
    private boolean inRecovery = false;
    private int recoveryValue   =   0;
    private long    startrecovery;
    private View[] linkedViews;

    private SeriesItem seriesItem;
    private SeriesItem seriesItemRec;
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

        if (intent.hasExtra("exercise")) {
            cExercise = intent.getExtras().getParcelable("exercise");
            cExercise.setSerializer(((App) getApplication()).getDBSerializer());

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.internal_error), Toast.LENGTH_LONG).show();
            finish();
        }
        if (cExercise != null) {
            // setup for the very first time the exercise
            millis      = (((cExercise.getRepetition() / cExercise.getFrequency()) * cExercise.getSeries()) + cExercise.getRecovery() * (cExercise.getSeries() - 1))*1000;
            repetitions = cExercise.getRepetition();
            series      = cExercise.getSeries();
            frequency   = cExercise.getFrequency();
        } else {
            Toast.makeText(getApplicationContext(),
                           getResources().getString(R.string.internal_error),
                           Toast.LENGTH_LONG).show();
            this.finish();
        }


        if(savedInstanceState != null){
            leftTime            =   savedInstanceState.getLong("leftTime");
            currentRepetition   =   savedInstanceState.getInt("currentRepetition");
            currentSerie        =   savedInstanceState.getInt("currentSerie");
            stopped             =   savedInstanceState.getBoolean("stopped");
            this.startrecovery  =   savedInstanceState.getLong("startrecovery");
            this.inRecovery     =   savedInstanceState.getBoolean("inrecovery");
            this.recoveryValue  =   savedInstanceState.getInt("recoveryvalue");
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

        seriesItemRec   = new SeriesItem.Builder(getResources().getColor(R.color.recovery))
                .setRange(0, cExercise.getRecovery()-1, 0)
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

            decoView.setVisibility(View.VISIBLE);
            textViewPercentage.setVisibility(View.VISIBLE);

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
                startCountDownTimer(leftTime);

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
    private void startCountDownTimer(long milliseconds) {
        Log.i("andrea","start " + milliseconds);
        final long millisecondstotal    =   milliseconds;
        countDownTimer = new CountDownTimer(milliseconds + 1000, frequency) {

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                Log.i("andrea","tick " + leftTimeInMilliseconds);
                leftTime = leftTimeInMilliseconds;
                long seconds = leftTimeInMilliseconds / 1000;
                int series1Index = 0;

                if (!inRecovery) {
                    series1Index    = decoView.addSeries(seriesItem);
                    textViewPercentage.setText(String.valueOf(currentRepetition));
                    currRepetition.setText(String.valueOf(currentRepetition));
                    decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());
                }

                if(!inRecovery)
                    currentRepetition += 1;

                // format the textview to show the easily readable format
                textViewRemaining.setText(String.format("%02d", (millis / 1000 - seconds) / 60) + "  :  " + String.format("%02d", (millis / 1000 - seconds) % 60));


                if (currentRepetition >= repetitions && !inRecovery) {

                    // setting UI elements
                    series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());
                    inRecovery = true;
                    startrecovery   =   System.currentTimeMillis();
                    currRepetition.setText(String.valueOf(currentRepetition));
                    series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(currentRepetition).setIndex(series1Index).setDelay(0).build());

                    //currentRepetition = 0;
                    decoView.executeReset();
                    decoView.deleteAll();
                    textViewPercentage.setTextColor(getResources().getColor(R.color.recovery));
                    textViewPercentage.setText(0+"");
                    series1Index = decoView.addSeries(seriesItemRec);
                    decoView.addEvent(new DecoEvent.Builder(recoveryValue).setIndex(series1Index).setDelay(0).build());



                    currentSerie++;
                    if (currentSerie <= series&&!inRecovery)
                        currSeries.setText(String.valueOf(currentSerie));
                }

                if (inRecovery) {
                    //check if recovery is finished
                    //restore decoview
                    long elapsed    =   System.currentTimeMillis()-startrecovery;
                    textViewPercentage.setTextColor(getResources().getColor(R.color.recovery));
                    textViewPercentage.setText("" + (int) elapsed / 1000);
                    System.out.println("in recupero" + elapsed + " " + System.currentTimeMillis() + " " + startrecovery);
                    recoveryValue   =   (int)elapsed/1000;
                    series1Index = decoView.addSeries(seriesItemRec);
                    decoView.addEvent(new DecoEvent.Builder(recoveryValue).setIndex(series1Index).setDelay(0).build());
                    if(((int)elapsed/1000)>=cExercise.getRecovery()){
                        currentRepetition   =   0;
                        recoveryValue   =   0;
                        inRecovery  =   false;
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

                Log.i("andrea","Ho finito!");
                if (currentSerie >= series) {
                    int series1Index = decoView.addSeries(seriesItem);
                    decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                            .setIndex(series1Index)
                            .setLinkedViews(linkedViews)
                            .setDelay(0)
                            .setDuration(4000)
                            .setDisplayText(getString(R.string.complete))
                            .build());

                    // finishing activity
                    cExercise.setDone(true);
                    SQLiteSerializer test = ((App) getApplication()).getDBSerializer();
                    cExercise.setComment("tacci tua");
                    cExercise.setRating(4);
                    Log.i("andrea",cExercise.toString());
                    UserExercise pino = test.getAnExerciseOfAUserSession(ExerciseDetailFragment.userWorkoutSession.getId(), cExercise.getMyUserID(), cExercise.getExerciseDate(), cExercise.getId());
                    Log.i("andrea stato esercizio", pino.toString());
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
                }

            }

        }.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("leftTime", leftTime);
        outState.putInt("currentRepetition", currentRepetition);
        outState.putInt("currentSerie", currentSerie);
        outState.putBoolean("stopped"      , stopped);
        outState.putLong("startrecovery",this.startrecovery);
        outState.putBoolean("inrecovery",this.inRecovery);
        outState.putInt("recoveryvalue",this.recoveryValue);
    }

}