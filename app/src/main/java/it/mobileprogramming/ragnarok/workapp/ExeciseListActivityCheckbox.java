package it.mobileprogramming.ragnarok.workapp;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

public class ExeciseListActivityCheckbox extends BaseActivityWithToolbar {

    // to visualize the exercises list
    public ArrayList<Exercise> exercisesFromDB;
    public ArrayList<Exercise> exercisesSelected;
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
        exercisesListAdapter = new ExercisesListAdapterWithCheckbox(exercisesFromDB,exercisesSelected,this);
        myList.setAdapter(exercisesListAdapter);
    }
}
