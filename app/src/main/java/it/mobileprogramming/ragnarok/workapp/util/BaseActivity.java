package it.mobileprogramming.ragnarok.workapp.util;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import it.mobileprogramming.ragnarok.workapp.BuildConfig;
import it.mobileprogramming.ragnarok.workapp.R;

/**
 * Base App Compact Activity with logging and Action Bar from Toolbar.
 * @author pincopallino93
 * @version 1.2
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * The TAG used for logging.
     */
    protected final String TAG = getClass().getCanonicalName();

    /**
     * The Action Bar.
     */
    protected ActionBar actionBar;

    /**
     * The main content.
     */
    protected View content;

    /**
     * Abstract method used in order to return the layout of the Activity.
     * @return the resource ID of the layout to set.
     */
    protected abstract int getLayoutResourceId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view using the view passed from abstract method
        setContentView(getLayoutResourceId());

        // Set context for usage in generic class outside Android
        Util.setContext(this);

        // Init toolbar
        initToolbar();

        // Get content in order to make easier use after
        content = findViewById(R.id.content);

        if (BuildConfig.DEBUG) Log.v(TAG, "onCreate");
    }

    /**
     * This method initializes a Toolbar in order to use such a ActionBar.
     */
    private void initToolbar() {
        // Get toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set as Action Bar and get it
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Set the Up arrow in Action Bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) Log.v(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (BuildConfig.DEBUG) Log.v(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) Log.v(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) Log.v(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) Log.v(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) Log.v(TAG, "onDestroy");
    }
}