package it.mobileprogramming.ragnarok.workapp;

import android.graphics.drawable.Drawable;
import android.os.Build;
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

    private View[] linkedViews;

    private SeriesItem seriesItem;
    private int seriesIndex;
    private float current;

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
        milliseconds = 2000;
        totalMillis = 30 * milliseconds;

        // Get DecoView
        decoView = (DecoView) findViewById(R.id.deco_view);

        // Get Views
        textViewPercentage = (TextView) findViewById(R.id.percentage_text_view);
        textViewRemaining = (TextView) findViewById(R.id.remaining_text_view);
        final View layout = findViewById(R.id.frame_layout);
        final ImageView pauseImageView = (ImageView) findViewById(R.id.pause_image_view);
        final ImageView stopImageView = (ImageView) findViewById(R.id.stop_image_view);

        // Group some of them for final animation
        linkedViews = new View[]{textViewPercentage, textViewRemaining, layout};

        // Setup DecoView
        setupDecoView();

        // TODO: decoview implements handler that doesn't come with pause method.. some trick to do here
        // Set pause and stop onItemClickListener
        pauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick!");
                Log.i(TAG, "Con tag= " + String.valueOf(pauseImageView.getTag()));

                if (pauseImageView.getTag().equals(getString(R.string.play)) && stopImageView.getTag().equals(getString(R.string.stop))) {
                    Log.i(TAG, "PLAY");
                    setupDecoView();
                    startEvent();

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
                    //
                    decoView.executeReset();

                    seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.accent))
                            .setRange(current, totalMillis, current)
                            .build();

                    seriesIndex = decoView.addSeries(seriesItem);

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
                    setupDecoView();
                    startEvent();

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
                    //
                    decoView.executeReset();
                    decoView.deleteAll();

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

        // 3, 2, 1.. GO!
        startEvent();
    }

    private void setupDecoView() {
        seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.accent))
                .setRange(0, totalMillis, 0)
                .build();

        seriesIndex = decoView.addSeries(seriesItem);

        // Add listener
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                current = currentPosition;

                // Calculate and set percentage
                /*float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textViewPercentage.setText(String.format("%.0f%%", percentFilled * 100f));*/
                textViewPercentage.setText(String.valueOf((int) currentPosition / milliseconds));
                // Set time
                textViewRemaining.setText(new SimpleDateFormat("mm:ss:SSS", Locale.ITALY).format(new Date((long) currentPosition)));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }

    private void startEvent() {
        // Add main event
        decoView.addEvent(new DecoEvent.Builder(totalMillis)
                .setIndex(seriesIndex)
                .setInterpolator(new LinearInterpolator())
                .setDuration(totalMillis)
                .build());

        // Add final event
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                        .setIndex(seriesIndex)
                        .setLinkedViews(linkedViews)
                        .setDelay(totalMillis + 2000)
                        .setDuration(4000)
                        .setDisplayText("Complete!") // TODO: use string res
                        .build()
        );
    }
}
