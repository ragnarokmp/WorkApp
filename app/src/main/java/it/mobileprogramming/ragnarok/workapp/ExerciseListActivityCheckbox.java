package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

public class ExerciseListActivityCheckbox extends BaseActivityWithToolbar {

    // to visualize the exercises list
    public ArrayList<Exercise> exercisesFromDB;
    public ArrayList<Integer> exercisesSelected;
    public ExercisesListAdapterWithCheckbox exercisesListAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_execises_list_checkbox;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView myList = (ListView) findViewById(R.id.exerciselistView);

        SQLiteSerializer dbSerializer = ((App) this.getApplication()).getDBSerializer();
        dbSerializer.open();

        exercisesFromDB = dbSerializer.loadAll();

        if (getIntent() != null) {
            exercisesSelected = getIntent().getIntegerArrayListExtra("exercises_selected");
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("selected_exercise")) {
            exercisesSelected = savedInstanceState.getIntegerArrayList("selected_exercise");
        }

        Log.i(TAG, exercisesSelected.toString());
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
