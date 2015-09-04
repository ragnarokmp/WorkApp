package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.GymModel.Workout;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivity;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;
import it.mobileprogramming.ragnarok.workapp.util.JSONRoot;

/**
 * An activity representing a list of Exercises. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ExerciseDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ExerciseListFragment} and the item details
 * (if present) is a {@link ExerciseDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ExerciseListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ExerciseListActivity extends BaseActivityWithToolbar implements ExerciseListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // to retrieve exercises from the website
    private String website = "http://46.101.165.167/index.php/exercise/getAllExercise";
    private int userID;
    private int workoutID;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_exercise_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        SQLiteSerializer dbSerializer = ((App) getApplication()).getDBSerializer();
        dbSerializer.open();
        if (intent.hasExtra("userID")) {
            userID = intent.getExtras().getInt("userID");
            ArrayList<UserWorkout> usWorkouts = dbSerializer.loadWorkoutsForUser(userID);
            ArrayList<UserWorkoutSession> firstWorkoutSessions = usWorkouts.get(0).getWoSessions();
            workoutID = intent.getExtras().getInt("workoutID");
            UserWorkoutSession userWorkoutSession = firstWorkoutSessions.get(workoutID);
            Toast.makeText(this, userWorkoutSession.getStrComment(), Toast.LENGTH_LONG).show();
        }

        if (findViewById(R.id.exercise_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large-land and
            // res/values-sw600dp-land). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ExerciseListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.exercise_list))
                    .setActivateOnItemClick(true);

        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        if (!getIntent().hasExtra(WorkoutFragment.EXTRA_USER_WORKOUT_SESSION)) {
            // setting up the swipe-refresh layout
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // performing async connection
                    JSONAsyncTask JAT = new JSONAsyncTask();
                    JAT.execute(website);

                    // letting the swipe to refresh to stop
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method from {@link ExerciseListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            //arguments.putString(ExerciseDetailFragment.ARG_ITEM_ID, id);
            arguments.putInt("userID",userID);
            arguments.putInt("workoutID",workoutID);
            arguments.putInt("exerciseID",Integer.valueOf(id));
            ExerciseDetailFragment fragment = new ExerciseDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.exercise_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ExerciseDetailActivity.class);
            detailIntent.putExtra(ExerciseDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
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
            data.deserializeRoot(((App) getApplication()).getDBSerializer());
        }

        @Override
        protected void onCancelled(String result) {
            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot establish connection!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
