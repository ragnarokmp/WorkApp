package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserExercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.GymModel.Workout;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithNavigationDrawer;
import it.mobileprogramming.ragnarok.workapp.util.BitmapHelper;

public class MainActivity extends BaseActivityWithNavigationDrawer implements WorkoutFragment.OnFragmentInteractionListener, ExercisesFragment.OnFragmentInteractionListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate for launch screen
        setTheme(R.style.WorkApp);
        super.onCreate(savedInstanceState);

        // Set the WorkoutFragment
        setFragment(TypeFragment.Workout);
        //TODO remove in final commit
        //testing task
        /*TestTask testing    =   new TestTask(this.getApplicationContext());
        testing.execute();*/
        //end testing task

        checkSignIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set checked always the only possible item, the Workout one
        navigationView.getMenu().getItem(0).setChecked(true);

        // setting user signed in
        setUserSignedIn();
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
/*                        // Set the ExercisesFragment
                        setFragment(TypeFragment.Exercises);*/
                        // Create new ExerciseListActivity
                        Intent intent = new Intent(getApplicationContext(), ExerciseListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_settings:
                        // Create new SettingsActivity
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_info:
                        // Create new InfoActivity
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
        //TODO: to use in order to fetch interaction from fragments
    }

    /**
     * This method allow to set the right fragment in the container of
     * the Activity.
     *
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

        } /*else if (type == TypeFragment.Exercises) {
            // Set title to the app
            actionBar.setTitle(R.string.exercises);

            // Create fragment and give it an argument specifying the article it should show
            fragment = new ExerciseListFragment();
            Bundle args = new Bundle();
            //args.putInt();
            fragment.setArguments(args);
        }*/

        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.content, fragment);

        // Commit the transaction
        transaction.commit();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                // if user not signed in then he cannot access to the account activity
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (pref.contains("signed_in")) {
                    // Create new AccountActivity
                    Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intent);
                } else {
                    checkSignIn();
                }
                break;
            default:
                break;
        }
    }


    /**
     * shared preferences check if first sign in is done
     */
    public void checkSignIn() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!pref.contains("signed_in"))
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        else
            setUserSignedIn();
    }

    /**
     * shared preferences retrieving
     */
    public void setUserSignedIn() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (pref.contains("signed_in")) {
            setUser(pref);
        }
    }
    public void setUser(SharedPreferences pref) {
        ((TextView) findViewById(R.id.username)).setText(pref.getString("personName", "Username"));
        ((TextView) findViewById(R.id.email)).setText(pref.getString("personEmail", "username@gmail.com"));

        if (!pref.contains("personAvatarBitmap")) {
            String avatar_string;
            if ((avatar_string = pref.getString("personAvatar", null)) != null) {
                BitmapAsync bAsync = new BitmapAsync();
                bAsync.execute(avatar_string);
            }
        } else {
            ((ImageView) findViewById(R.id.avatar)).setImageBitmap(BitmapHelper
                                                   .decodeBase64(pref.getString("personAvatarBitmap", null)));
        }

    }


    /**
     * image retrieving and storing encoded bitmap string
     * (using an async task to retrieve)
     */
    private class BitmapAsync extends AsyncTask<String, String, InputStream> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected InputStream doInBackground(final String...args) {
            try {
                URL url = new URL(args[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return input;

                //prefeditor.putString("personAvatar", BitmapHelper.encodeTobase64(avatar_bitmap));

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // nothing to do (for now...)
        }

        @Override
        protected void onPostExecute(InputStream result) {
            if (result == null)
                this.cancel(true);
            Bitmap avatar_bitmap = BitmapFactory.decodeStream(result);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("personAvatarBitmap", BitmapHelper.encodeTobase64(avatar_bitmap));
            editor.commit();

            ((ImageView) findViewById(R.id.avatar)).setImageBitmap(avatar_bitmap);
        }
    }
}