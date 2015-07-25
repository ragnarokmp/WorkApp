package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.view.MenuItem;

import it.mobileprogramming.ragnarok.workapp.GymModel.TestTask;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithNavigationDrawer;

public class MainActivity extends BaseActivityWithNavigationDrawer implements WorkoutFragment.OnFragmentInteractionListener, ExercisesFragment.OnFragmentInteractionListener, ExerciseListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.WorkApp);
        super.onCreate(savedInstanceState);

        // Set the WorkoutFragment
        setFragment(TypeFragment.Workout);
        //TODO remove in final commit
        //testing task
        /*TestTask testing    =   new TestTask(this.getApplicationContext());
        testing.execute();*/
        //end testing task
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getNavigationMenu() {
        return R.menu.drawer;
    }

    @Override
    protected int getHeaderView() {
        return R.layout.drawer_header;
    }

    @Override
    protected NavigationView.OnNavigationItemSelectedListener getNavigationListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show(); // TODO: remove after
                // Checked the Drawer Menu Item
                menuItem.setChecked(true);
                // Close Navigation Drawer
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (menuItem.getItemId()) {
                    case R.id.drawer_workout:
                        // Set the WorkoutFragment
                        setFragment(TypeFragment.Workout);
                        break;
                    case R.id.drawer_exercises:
                        // Set the ExercisesFragment
                        setFragment(TypeFragment.Exercises);
                        break;
/*                    case R.id.drawer_about:
                        // Create new AboutActivity
                        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(intent);
                        break;*/
                    case R.id.drawer_settings:
                        //TODO not working
                        // Create new Settingsctivity
                        //intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        //startActivity(intent);
                        break;
                    case R.id.drawer_info:
                        // Create new InfoActivity
                        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO: to use in order to fetch interaction from fragments
    }

    /**
     * This method allow to set the right fragment in the container of
     * the Activity.
     * @param type the type of fragment to set.
     */
    private void setFragment(TypeFragment type) {

        Fragment fragment = new Fragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (type == TypeFragment.Workout) {
            // Set title to the app
            actionBar.setTitle(R.string.workout);

            // Create fragment and give it an argument specifying the article it should show
            fragment = new WorkoutFragment();
            Bundle args = new Bundle();
            //args.putInt();
            fragment.setArguments(args);

        } else if (type == TypeFragment.Exercises) {
            // Set title to the app
            actionBar.setTitle(R.string.exercises);

            // Create fragment and give it an argument specifying the article it should show
            fragment = new ExerciseListFragment();
            Bundle args = new Bundle();
            //args.putInt();
            fragment.setArguments(args);
        }

        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.content, fragment);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onItemSelected(String id) {

    }

    /**
     * Enumeration used in order to manage in better way the naming of fragment.
     */
    private enum TypeFragment {
        /**
         * Enum for WorkoutFragment.
         */
        Workout,

        /**
         * Enum for ExercisesFragment
         */
        Exercises
    }
}