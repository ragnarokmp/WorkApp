package it.mobileprogramming.ragnarok.workapp;

import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import it.mobileprogramming.ragnarok.workapp.util.BaseActivity;
import it.mobileprogramming.ragnarok.workapp.util.CircularProgressBar;

public class StartExerciseActivity extends BaseActivity {

    public static final String START_EXERCISE_FRAGMENT = "StartExerciseFragment";

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

        final CircularProgressBar circularProgressBar = (CircularProgressBar) findViewById(R.id.circular_progress_bar);
        circularProgressBar.setMax(30);

        CountDownTimer countDownTimer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(final long millisUntilFinished) {
                final int secondsRemaining = (int) (millisUntilFinished / 1000);
                circularProgressBar.setProgress(secondsRemaining);
            }

            @Override
            public void onFinish() {
                circularProgressBar.setProgress(0);
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
