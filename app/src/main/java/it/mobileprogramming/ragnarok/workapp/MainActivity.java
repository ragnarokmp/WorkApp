package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.view.MenuItem;

import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithNavigationDrawer;

public class MainActivity extends BaseActivityWithNavigationDrawer implements WorkoutFragment.OnFragmentInteractionListener, ExercisesFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (menuItem.getItemId()) {
                    case R.id.drawer_workout:
                        actionBar.setTitle(R.string.workout);

                        // Create fragment and give it an argument specifying the article it should show
                        WorkoutFragment workoutFragment = new WorkoutFragment();
                        Bundle args = new Bundle();
                        //args.putInt();
                        workoutFragment.setArguments(args);

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack so the user can navigate back
                        transaction.replace(R.id.content, workoutFragment);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();

                        break;
                    case R.id.drawer_exercises:
                        actionBar.setTitle(R.string.exercises);

                        // Create fragment and give it an argument specifying the article it should show
                        ExercisesFragment exercisesFragment = new ExercisesFragment();
                        args = new Bundle();
                        //args.putInt();
                        exercisesFragment.setArguments(args);

                        transaction = getSupportFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack so the user can navigate back
                        transaction.replace(R.id.content, exercisesFragment);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();
                        break;
                    case R.id.drawer_about:
                        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_settings:
                        //TODO not working
                        //intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        //startActivity(intent);
                        break;
                    case R.id.drawer_info:
                        intent = new Intent(getApplicationContext(), InfoActivity.class);
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

    }
}