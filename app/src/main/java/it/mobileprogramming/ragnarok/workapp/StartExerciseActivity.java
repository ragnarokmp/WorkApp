package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
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
    private TextView textViewPercentage;
    private TextView textViewRemaining;
    private TextView totalRepetitions;
    private TextView currRepetition;
    private TextView totalSeries;
    private TextView currSeries;

    Exercise cExercise;

    private int milliseconds;
    private int progress    =   0;
    private int currentRepetition = 1;
    private int currentSerie = 1;


    private View[] linkedViews;

    private SeriesItem seriesItem;

    boolean paused  =   false;
    boolean stopped =   false;
    GraphManager aManager;

    private class GraphManager extends AsyncTask{
        long previous;
        @Override
        protected Object doInBackground(Object[] params) {
            while(progress < milliseconds && stopped == false){
                previous = System.currentTimeMillis();
                System.out.println("MILLISECONDS " + previous);
                SystemClock.sleep(100);
                long now = System.currentTimeMillis();
                now =   now-previous;
                System.out.println("MILLISECONDS DELTA"+now+" PROGRESS "+progress);
                //System.out.println("paused status " + paused);
                if(paused==false&&stopped==false){
                    Object[] payload    =   new Object[1];
                    payload[0]          =   now;
                    this.publishProgress(payload);
                }
            }
            stopped = false;
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            //System.out.println("aumento");
            super.onProgressUpdate(values);
            long now    =   (long)values[0];
            progress    =   progress+(int)now;
            int series1Index = decoView.addSeries(seriesItem);
            if(progress > milliseconds){
                progress = milliseconds;
            }
            decoView.addEvent(new DecoEvent.Builder(progress).setIndex(series1Index).build());
            currentRepetition = (int) progress * cExercise.getRepetition() / milliseconds;
            textViewPercentage.setText(String.valueOf(currentRepetition));
            currRepetition.setText(String.valueOf(currentRepetition));


            textViewRemaining.setText(new SimpleDateFormat("mm:ss:SSS", Locale.ITALY).format(new Date((long) progress)));
            if(currentSerie >= cExercise.getSeries()){
                Log.i("ENTERED", "");
                series1Index = decoView.addSeries(seriesItem);
                decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                        .setIndex(series1Index)
                        .setLinkedViews(linkedViews)
                        .setDelay(0)
                        .setDuration(4000)
                        .setDisplayText("Complete!") // TODO: use string res
                        .build());
            } else if (currentRepetition == cExercise.getRepetition()) {
                currentSerie++;
                currSeries.setText(String.valueOf(currentSerie));
                progress = 0;
                // resetting UI when serie is finished
                decoView.executeReset();
                series1Index = decoView.addSeries(seriesItem);
                decoView.addEvent(new DecoEvent.Builder(progress).setIndex(series1Index).setDelay(0).build());
                textViewPercentage.setText(String.valueOf((int) progress / milliseconds));
                textViewRemaining.setText(new SimpleDateFormat("mm:ss:SSS", Locale.ITALY).format(new Date((long) progress)));
            }
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_start_exercise;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Views
        textViewPercentage = (TextView) findViewById(R.id.percentage_text_view);
        textViewRemaining = (TextView) findViewById(R.id.remaining_text_view);
        totalRepetitions  = (TextView) findViewById(R.id.totalRepTV);
        currRepetition = (TextView) findViewById(R.id.currentRepTV);
        totalSeries = (TextView) findViewById(R.id.totalSeriesTV);
        currSeries  = (TextView) findViewById(R.id.currentSeriesTV);

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
            currRepetition.setText(String.valueOf(currentRepetition));
            totalRepetitions.setText(" / " + String.valueOf(cExercise.getRepetition()));
            currSeries.setText(String.valueOf(currentSerie));
            totalSeries.setText(" / " + String.valueOf(cExercise.getSeries()));
        } else {
            Toast.makeText(getApplicationContext(),
                           getResources().getString(R.string.internal_error),
                           Toast.LENGTH_LONG).show();
            this.finish();
        }

        milliseconds = cExercise.getFrequency()*cExercise.getRepetition();

        if(savedInstanceState != null){
            progress            =   savedInstanceState.getInt("progress");
            this.milliseconds   =   savedInstanceState.getInt("milliseconds");
            this.paused         =   savedInstanceState.getBoolean("paused");
            this.stopped        =   savedInstanceState.getBoolean("stopped");
            this.currentSerie   =   savedInstanceState.getInt("current_serie");
            this.currentRepetition = savedInstanceState.getInt("current_repetition");
        }

        // Get DecoView
        decoView = (DecoView) findViewById(R.id.deco_view);
        seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.accent))
                .setRange(0, milliseconds, 0)
                .setLineWidth(32f)
                .setSpinDuration(101)
                .build();

        decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(0)
                .setDuration(0)
                .build());

        //restore pause button state
        if(paused==true){
            // Get drawable
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getDrawable(R.drawable.ic_action_play);
            } else {
                drawable = getResources().getDrawable(R.drawable.ic_action_play);
            }

            // Set the new drawable and other stuffs..
            pauseImageView.setImageDrawable(drawable);
            pauseImageView.setContentDescription(getString(R.string.play));
            pauseImageView.setTag(getString(R.string.play));
        }

        // Group some of them for final animation
        linkedViews = new View[]{textViewPercentage, textViewRemaining, layout};

        // TODO: decoview implements handler that doesn't come with pause method.. some trick to do here
        // Set pause and stop onItemClickListener
        pauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick!");
                Log.i(TAG, "Con tag= " + String.valueOf(pauseImageView.getTag()));

                if (pauseImageView.getTag().equals(getString(R.string.play)) && stopImageView.getTag().equals(getString(R.string.stop))) {
                    Log.i(TAG, "PLAY");
                    paused = false;
                    // Get drawable
                    Drawable drawable;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable = getDrawable(R.drawable.ic_action_pause);
                    } else {
                        drawable = getResources().getDrawable(R.drawable.ic_action_pause);
                    }
                    if(progress==0){
                        aManager = new GraphManager();
                        aManager.execute();
                    }
                    // Set the new drawable and other stuffs..
                    pauseImageView.setImageDrawable(drawable);
                    pauseImageView.setContentDescription(getString(R.string.pause));
                    pauseImageView.setTag(getString(R.string.pause));
                } else if (pauseImageView.getTag().equals(getString(R.string.pause)) && stopImageView.getTag().equals(getString(R.string.stop))) {
                    Log.i(TAG, "PAUSE");
                    paused = true;
                    // Get drawable
                    Drawable drawable;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable = getDrawable(R.drawable.ic_action_play);
                    } else {
                        drawable = getResources().getDrawable(R.drawable.ic_action_play);
                    }

                    // Set the new drawable and other stuffs..
                    pauseImageView.setImageDrawable(drawable);
                    pauseImageView.setContentDescription(getString(R.string.play));
                    pauseImageView.setTag(getString(R.string.play));
                }
            }
        });

        stopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick!");
                Log.i(TAG, "Con tag= " + String.valueOf(stopImageView.getTag()));
                Log.i(TAG, "STOP");
                //TODO implement actions
                stopped =   true;
                progress    =   0;
                decoView.executeReset();
                int series1Index = decoView.addSeries(seriesItem);
                decoView.addEvent(new DecoEvent.Builder(progress).setIndex(series1Index).setDelay(0).build());
                textViewPercentage.setText(String.valueOf((int) progress / milliseconds));
                textViewRemaining.setText(new SimpleDateFormat("mm:ss:SSS", Locale.ITALY).format(new Date((long) progress)));
                // Get drawable
                Drawable drawable;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable = getDrawable(R.drawable.ic_action_play);
                } else {
                    drawable = getResources().getDrawable(R.drawable.ic_action_play);
                }
                // Set the new drawable and other stuffs..
                pauseImageView.setImageDrawable(drawable);
                pauseImageView.setContentDescription(getString(R.string.play));
                pauseImageView.setTag(getString(R.string.play));
            }
        });

        if(savedInstanceState!=null){
            int series1Index = decoView.addSeries(seriesItem);
            if(progress > milliseconds){
                progress = milliseconds;
             }
            decoView.addEvent(new DecoEvent.Builder(progress).setIndex(series1Index).setDelay(0).build());
            textViewPercentage.setText(String.valueOf((int) progress / milliseconds));
            textViewRemaining.setText(new SimpleDateFormat("mm:ss:SSS", Locale.ITALY).format(new Date((long) progress)));
        }
        aManager = new GraphManager();
        aManager.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("progress", progress);
        outState.putInt("milliseconds", milliseconds);
        outState.putBoolean("paused", paused);
        outState.putBoolean("stopped", stopped);
        outState.putInt("current_serie", currentSerie);
        outState.putInt("current_repetition", currentRepetition);
        stopped =   true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopped = true;
        if (!aManager.isCancelled())
            aManager.cancel(true);
    }
}
