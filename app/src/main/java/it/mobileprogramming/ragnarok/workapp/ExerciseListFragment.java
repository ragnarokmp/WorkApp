package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.dummy.DummyContent;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.JSONRoot;

/**
 * A list fragment representing a list of Exercises. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ExerciseDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ExerciseListFragment extends ListFragment {

    // to retrieve exercises from the website
    private String website = "http://46.101.165.167/index.php/exercise/getAllExercise";

    // to visualize the exercises list
    public ArrayList<Exercise> exercises;
    public ExercisesListAdapter exercisesListAdapter;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = exerciseCallback;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks exerciseCallback = new Callbacks() {
        @Override
        public void onItemSelected(String id) {

        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExerciseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
        dbSerializer.open();
        if (intent.hasExtra("userID")) {
            ArrayList<UserWorkout> usWorkouts = dbSerializer.loadWorkoutsForUser(getActivity().getIntent().getExtras().getInt("userID"));
            ArrayList<UserWorkoutSession> firstWorkoutSessions = usWorkouts.get(0).getWoSessions();
            UserWorkoutSession userWorkoutSession = firstWorkoutSessions.get(getActivity().getIntent().getExtras().getInt("workoutID"));
            exercises = userWorkoutSession.getExercisesOfSession();
            exercisesListAdapter = new ExercisesListAdapter(exercises, getActivity());
            setListAdapter(exercisesListAdapter);
        } else {
            exercises = dbSerializer.loadAll();
            exercisesListAdapter = new ExercisesListAdapter(exercises, getActivity());
            setListAdapter(exercisesListAdapter);
        }

        setHasOptionsMenu(true);

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set empty text //TODO right message...
        setEmptyText(getString(R.string.app_name));

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = exerciseCallback;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(String.valueOf(exercises.get(position).getId()));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            // retrieving data from the website
            JSONAsyncTask JAT = new JSONAsyncTask();
            JAT.execute(website);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * AsyncTask to perform a connection on swipe to refresh to retrieve from the website
     * the list of all the exercises
     */
    private class JSONAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final String...args) {
            String json = JSONRoot.JSONRetrieve(args[0]);
            if (json == null)
                this.cancel(true);
            return json;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // nothing to do (for now...)
        }

        @Override
        protected void onPostExecute(String result) {

            Gson gson = new Gson();
            // parsing
            JSONRoot data = gson.fromJson(result, JSONRoot.class);
            data.deserializeRoot(((App) getActivity().getApplication()).getDBSerializer());
            // updating the list
            SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
            exercises = dbSerializer.loadAll();
            exercisesListAdapter = new ExercisesListAdapter(exercises, getActivity());
            setListAdapter(exercisesListAdapter);
        }

        @Override
        protected void onCancelled(String result) {
            if (result == null) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Cannot establish connection!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
