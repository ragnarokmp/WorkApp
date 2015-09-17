package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;
import it.mobileprogramming.ragnarok.workapp.util.JSONRoot;

public class ExerciseListActivityCheckbox extends BaseActivityWithToolbar {

    // to visualize the exercises list
    public ArrayList<Exercise> exercisesFromDB;
    public ArrayList<Integer> exercisesSelected;
    public ExercisesListAdapterWithCheckbox exercisesListAdapter;
    private ListView myList;
    private Context context;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_execises_list_checkbox;
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putIntegerArrayListExtra("exercise_list", exercisesSelected);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myList = (ListView) findViewById(R.id.exerciselistView);

        context = getBaseContext();

        SQLiteSerializer dbSerializer = ((App) this.getApplication()).getDBSerializer();
        dbSerializer.open();

        exercisesFromDB = dbSerializer.loadAll();

        if (getIntent() != null) {
            exercisesSelected = getIntent().getIntegerArrayListExtra("exercises_selected");
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("selected_exercise")) {
            exercisesSelected = savedInstanceState.getIntegerArrayList("selected_exercise");
        }

        exercisesListAdapter = new ExercisesListAdapterWithCheckbox(exercisesFromDB, exercisesSelected, this);
        myList.setAdapter(exercisesListAdapter);
    }

    public void onSelected(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            Log.i(TAG, "ADD");
            exercisesSelected.add((Integer) view.getTag());
        } else {
            Log.i(TAG, "REMOVE");
            exercisesSelected.remove((Integer) view.getTag());
        }

        Log.i(TAG, "Now exercisesSelected: " + exercisesSelected.toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntegerArrayList("selected_exercise", exercisesSelected);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent resultIntent = new Intent();
                resultIntent.putIntegerArrayListExtra("exercise_list", exercisesSelected);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                return true;
            case R.id.action_refresh:
                // retrieving data from the website
                JSONAsyncTask JAT = new JSONAsyncTask();
                JAT.execute(ExerciseListFragment.website);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return true;
    }

    public class JSONAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final String...args) {
            String json = JSONRoot.JSONRetrieve(args[0]);
            if (json == null)
                this.cancel(true);

            Gson gson = new Gson();
            // parsing
            JSONRoot data = gson.fromJson(json, JSONRoot.class);
            data.deserializeRoot(((App) getApplication()).getDBSerializer());
            return json;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // nothing to do (for now...)
        }

        @Override
        protected void onPostExecute(String result) {
            // updating the list
            SQLiteSerializer dbSerializer = ((App) getApplication()).getDBSerializer();
            exercisesFromDB = dbSerializer.loadAll();

            exercisesListAdapter = new ExercisesListAdapterWithCheckbox(exercisesFromDB, exercisesSelected, context);
            myList.setAdapter(exercisesListAdapter);

            // visualizing the snackbar
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.snackbar_JSON_success), Snackbar.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled(String result) {
            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.no_connection), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}