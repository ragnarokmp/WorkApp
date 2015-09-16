package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;
import it.mobileprogramming.ragnarok.workapp.util.DatePickerFragment;

/**
 * An activity representing a list of Workout. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link WorkoutDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link WorkoutListFragment} and the item details
 * (if present) is a {@link WorkoutDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link WorkoutListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class WorkoutListActivity extends BaseActivityWithToolbar implements WorkoutListFragment.Callbacks, DatePickerFragment.DatePickerCallback {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public int userID = -1;

    @Override
    protected int getLayoutResourceId() {

        return R.layout.activity_workout_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (findViewById(R.id.workout_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((WorkoutListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.workout_list))
                    .setActivateOnItemClick(true);
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
     * Callback method from {@link WorkoutListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {

        Log.d("WORKOUT_LIST_ACTIVITY", "Premo workout con id " + id);

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(WorkoutDetailFragment.WORKOUT_ID, id);
            if (userID != -1) {
                arguments.putInt("userID",userID);
            }
            WorkoutDetailFragment fragment = new WorkoutDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.workout_detail_container, fragment)
                    .commit();

            TextView emptyWorkoutTextView = (TextView) findViewById(R.id.empty_workout);
            emptyWorkoutTextView.setVisibility(View.GONE);

        } else {
            // In single-pane mode, simply start the detail activityd
            // for the selected item ID.
            Intent detailIntent = new Intent(this, WorkoutDetailActivity.class);
            detailIntent.putExtra(WorkoutDetailFragment.WORKOUT_ID, id);
            if (userID != -1) {
                detailIntent.putExtra("userID",userID);
            }
            startActivityForResult(detailIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Log.i(TAG, "tutto a posto workout list activity");
            super.setResult(RESULT_OK);
            super.finish();
        }
    }

    @Override
    public void pickerOnDateSet(Calendar result, Integer workout_id) {
        if (getSupportFragmentManager().getFragments().get(1) != null) {
            ((WorkoutDetailFragment) getSupportFragmentManager().getFragments().get(1)).dateFromActiivty(result, workout_id);
        }
    }
}
