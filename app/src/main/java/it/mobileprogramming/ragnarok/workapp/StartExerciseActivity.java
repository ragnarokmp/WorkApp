package it.mobileprogramming.ragnarok.workapp;

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

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.mobileprogramming.ragnarok.workapp.util.BaseActivity;

public class StartExerciseActivity extends BaseActivity {

    public static final String START_EXERCISE_FRAGMENT = "StartExerciseFragment";

    private DecoView decoView;
    private TextView textViewPercentage;
    private TextView textViewRemaining;

    private int milliseconds;
    private int totalMillis;
    private int progress    =   0;

    private View[] linkedViews;

    private SeriesItem seriesItem;

    boolean paused  =   false;
    boolean stopped =   false;

    private class GraphManager extends AsyncTask{
        long previous;
        @Override
        protected Object doInBackground(Object[] params) {
            while(progress<totalMillis&&stopped==false){
                previous = System.currentTimeMillis();
                System.out.println("MILLISECONDS " + previous);
                SystemClock.sleep(100);
                long now = System.currentTimeMillis();
                now =   now-previous;
                System.out.println("MILLISECONDS DELTA"+now);
                //System.out.println("paused status " + paused);
                if(paused==false&&stopped==false){
                    Object[] payload    =   new Object[1];
                    payload[0]          =   now;
                    this.publishProgress(payload);
                }
            }
            stopped =   false;
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            //System.out.println("aumento");
            long now    =   (long)values[0];
            super.onProgressUpdate(values);
            progress    =   progress+(int)now;
            int series1Index = decoView.addSeries(seriesItem);
            if(progress>totalMillis){
                progress    =   totalMillis;
            }
            decoView.addEvent(new DecoEvent.Builder(progress).setIndex(series1Index).setDelay(0).build());
            textViewPercentage.setText(String.valueOf((int) progress / milliseconds));
            textViewRemaining.setText(new SimpleDateFormat("mm:ss:SSS", Locale.ITALY).format(new Date((long) progress)));
            if(progress>=totalMillis){
                series1Index = decoView.addSeries(seriesItem);
                decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                        .setIndex(series1Index)
                        .setLinkedViews(linkedViews)
                        .setDelay(0)
                        .setDuration(4000)
                        .setDisplayText("Complete!") // TODO: use string res
                        .build());
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

        if (getSupportFragmentManager().findFragmentByTag(START_EXERCISE_FRAGMENT) == null ) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            StartExerciseFragment fragment = new StartExerciseFragment();
            transaction.add(fragment, START_EXERCISE_FRAGMENT);
            transaction.commit();
        }

        // Get exercise info
        milliseconds = 1000;
        totalMillis = 15 * milliseconds;

        // Get DecoView
        decoView = (DecoView) findViewById(R.id.deco_view);
        seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.accent))
                .setRange(0, totalMillis, 0)
                .setLineWidth(32f)
                .build();

        decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build());

        // Get Views
        textViewPercentage = (TextView) findViewById(R.id.percentage_text_view);
        textViewRemaining = (TextView) findViewById(R.id.remaining_text_view);
        final View layout = findViewById(R.id.frame_layout);
        final ImageView pauseImageView = (ImageView) findViewById(R.id.pause_image_view);
        final ImageView stopImageView = (ImageView) findViewById(R.id.stop_image_view);

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

                if (stopImageView.getTag().equals(getString(R.string.play)) && pauseImageView.getTag().equals(getString(R.string.pause))) {
                    Log.i(TAG, "PLAY");
                    //TODO implement actions
                    // Get drawable
                    Drawable drawable;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable = getDrawable(R.drawable.ic_action_stop);
                    } else {
                        drawable = getResources().getDrawable(R.drawable.ic_action_stop);
                    }

                    // Set the new drawable and other stuffs..
                    stopImageView.setImageDrawable(drawable);
                    stopImageView.setContentDescription(getString(R.string.stop));
                    stopImageView.setTag(getString(R.string.stop));
                } else if (stopImageView.getTag().equals(getString(R.string.stop)) && pauseImageView.getTag().equals(getString(R.string.pause))) {
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
                    stopImageView.setImageDrawable(drawable);
                    stopImageView.setContentDescription(getString(R.string.play));
                    stopImageView.setTag(getString(R.string.play));
                }
            }
        });
        GraphManager aManager   =   new GraphManager();
        aManager.execute();

    }
}
