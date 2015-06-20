package it.mobileprogramming.ragnarok.workapp;

import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;

import it.mobileprogramming.ragnarok.workapp.GymModel.TestTask;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivity;

public class MainActivity extends BaseActivity implements WorkoutFragment.OnFragmentInteractionListener, ExercisesFragment.OnFragmentInteractionListener {

    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private View content;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO remove in final commit
        //testing task
        TestTask testing    =   new TestTask(this.getApplicationContext());
        testing.execute();
        //end testing task

        initToolbar();
        setupDrawerLayout();

        content = findViewById(R.id.content);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                        //TODO: open new activity
                        break;
                    case R.id.drawer_settings:
                        //TODO: open new activity
                        break;
                    case R.id.drawer_info:
                        //TODO: open new activity
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}