package it.mobileprogramming.ragnarok.workapp;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.grabner.circleprogress.CircleProgressView;
import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivity;


public class DoExerciseActivity extends BaseActivity {

    CircleProgressView circleProgressView;
    TextView textViewRemaining, totalRepetitions, currRepetition, totalSeries, currSeries;
    ImageView pauseImageView, stopImageView;

    CountDownTimer countDownTimer;

    int currentRepetition = 1;
    int currentSerie      = 1;
    int millis, repetitions, series, recovery;

    Exercise currentExercise;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_start_exercise;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);

        // Get Views
        circleProgressView = (CircleProgressView) findViewById(R.id.circleView);

        textViewRemaining  = (TextView) findViewById(R.id.remaining_text_view);
        totalRepetitions   = (TextView) findViewById(R.id.totalRepTV);
        currRepetition     = (TextView) findViewById(R.id.currentRepTV);
        totalSeries        = (TextView) findViewById(R.id.totalSeriesTV);
        currSeries         = (TextView) findViewById(R.id.currentSeriesTV);

        pauseImageView = (ImageView) findViewById(R.id.pause_image_view);
        stopImageView = (ImageView) findViewById(R.id.stop_image_view);

        // retrieving exercise from intent
        Intent intent = getIntent();
        SQLiteSerializer dbSerializer = ((App) getApplication()).getDBSerializer();
        dbSerializer.open();
        if (intent.hasExtra("exerciseID")) {
            currentExercise = dbSerializer.loadExercise(intent.getExtras().getInt("exerciseID"));
        }
        if (currentExercise != null) {
            currRepetition.setText(String.valueOf(currentRepetition));
            totalRepetitions.setText(" / " + String.valueOf(currentExercise.getRepetition()));
            currSeries.setText(String.valueOf(currentSerie));
            totalSeries.setText(" / " + String.valueOf(currentExercise.getSeries()));
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.internal_error),
                    Toast.LENGTH_LONG).show();
            this.finish();
        }

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


        // starting for the very first time the exercise
        millis      = (currentExercise.getFrequency() * currentExercise.getRepetition()) * currentExercise.getSeries();
        repetitions = currentExercise.getRepetition();
        series      = currentExercise.getSeries();
        recovery    = currentExercise.getRecovery();

        startCountDownTimer(millis, currentExercise.getFrequency());
    }

    /**
     * to pause the countdown timer and refresh the UI
     */
    public void onPauseButton() {
        if (pauseImageView.getTag().equals(getString(R.string.pause))) {
            if (countDownTimer != null) {
                countDownTimer.cancel();

                // TODO using the saved data to set a new countdown timer instance
                // TODO on landscape layout adjust timer visualization

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
            startCountDownTimer(millis, currentExercise.getFrequency());

            pauseImageView.setTag(getString(R.string.pause));
            circleProgressView.setVisibility(View.VISIBLE);


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
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
        else {
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
     * this is the countdown time function
     * @param milliseconds: int, duration of the exercise
     * @param frequency: int, number of repetitions per seconds
     */
    private void startCountDownTimer(int milliseconds, int frequency) {
        circleProgressView.setMaxValue((int) repetitions);
        countDownTimer = new CountDownTimer(milliseconds + 1000, frequency) {

            int start_value = -1;
            int serie = 1;

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;

                start_value++;

                // setting UI elements
                circleProgressView.setValue(start_value);
                circleProgressView.setText(String.valueOf(start_value));
                currRepetition.setText(String.valueOf(start_value));

                // format the textview to show the easily readable format
                textViewRemaining.setText(String.format("%02d", (millis / 1000 - seconds) / 60) + ":" +
                                          String.format("%02d", (millis / 1000 - seconds) % 60));

                if (start_value == repetitions) {

                    start_value = 0;
                    currRepetition.setText(String.valueOf(start_value));

                    serie++;
                    if (serie <= series)
                        currSeries.setText(String.valueOf(serie));
                }
            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                textViewRemaining.setText("--:--");
                circleProgressView.setMaxValue(0);
                circleProgressView.setVisibility(View.INVISIBLE);

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

                currRepetition.setText(String.valueOf(0));
                currSeries.setText(String.valueOf(0));

            }

        }.start();

    }



}
