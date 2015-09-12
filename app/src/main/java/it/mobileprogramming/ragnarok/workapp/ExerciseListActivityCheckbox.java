package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

public class ExerciseListActivityCheckbox extends BaseActivityWithToolbar {

    // to visualize the exercises list
    public ArrayList<Exercise> exercisesFromDB;
    public static ArrayList<Integer> exercisesSelected;
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

        if (savedInstanceState != null && savedInstanceState.containsKey("selected_exercise")) {
            exercisesSelected = savedInstanceState.getIntegerArrayList("selected_exercise");
        } else {
            exercisesSelected = new ArrayList<>();
        }

        exercisesListAdapter = new ExercisesListAdapterWithCheckbox(exercisesFromDB, exercisesSelected, this);
        myList.setAdapter(exercisesListAdapter);
    }

    public void onSelected(View view) {
        exercisesSelected.add((Integer) view.getTag());
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
