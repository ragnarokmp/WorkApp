package it.mobileprogramming.ragnarok.workapp.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.mobileprogramming.ragnarok.workapp.BuildConfig;

/**
 * Base fragment with logging.
 *
 * @author pincopallino93
 * @version 1.1
 */
public abstract class BaseFragment extends Fragment {
    /**
     * The TAG used for logging.
     */
    public final String TAG = getClass().getCanonicalName();

    /**
     * The context fetched form the Activity.
     */
    public Context context;

    /**
     * The main content.
     */
    public View view;

    /**
     * Abstract method used in order to return the layout of the Activity.
     * @return the resource ID of the layout to set.
     */
    protected abstract int getLayoutResourceId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get context now in order not to suffer stress after
        context = getActivity();

        if (BuildConfig.DEBUG) Log.v(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate layout passed through the abstract method
        view = inflater.inflate(getLayoutResourceId(), container, false);

        if (BuildConfig.DEBUG) Log.v(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) Log.v(TAG, "onResume");
    }

    @Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
        if (BuildConfig.DEBUG) Log.v(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) Log.v(TAG, "onStart");
    }

    @Override
    public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        if (BuildConfig.DEBUG) Log.v(TAG, "onSaveInstanceState");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) Log.v(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) Log.v(TAG, "onDestroy");
    }
}

