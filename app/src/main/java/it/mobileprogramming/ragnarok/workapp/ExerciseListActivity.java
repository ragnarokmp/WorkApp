package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

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

    private boolean workout_session = false;
    private Menu optmenu;

    @Override
    protected int getLayoutResourceId() {

        return R.layout.activity_exercise_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // When arrive from workout detail in read mode, see WorkoutDetailFragment for info
        if (getIntent().hasExtra("readMode") || getIntent().hasExtra("back")){
            workout_session = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("readMode") || getIntent().hasExtra("back")){
            workout_session = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        optmenu = menu;
        return true;
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
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method from {@link ExerciseListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(UserWorkoutSession userWorkoutSession, int position) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            if (userWorkoutSession == null) {
               arguments.putParcelable("exercise",ExerciseListFragment.exercises.get(position));
            } else {
                arguments.putParcelable("workoutSession", userWorkoutSession);
                arguments.putInt("exerciseID", position);
            }
            arguments.putBoolean("isTablet",true);
            ExerciseDetailFragment fragment = new ExerciseDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.exercise_detail_container, fragment)
                    .commit();
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ExerciseDetailActivity.class);
            if (!workout_session) {
                detailIntent.putExtra("exercise", ExerciseListFragment.exercises.get(position));
            } else {
                detailIntent.putExtra("workoutSession", userWorkoutSession);
                detailIntent.putExtra("exerciseID",position);
            }
            startActivity(detailIntent);
        }
    }
}
