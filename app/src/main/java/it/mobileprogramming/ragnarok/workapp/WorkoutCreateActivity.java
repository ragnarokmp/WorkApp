package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.model.Card;

import java.util.ArrayList;
import java.util.List;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.Workout;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.cards.WorkoutSessionCreateCard;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;
import it.mobileprogramming.ragnarok.workapp.util.MyMaterialListView;

public class WorkoutCreateActivity extends BaseActivityWithToolbar {

    private MyMaterialListView workoutListView;
    private List<WorkoutSession> workoutSessionList = new ArrayList<>();
    private int sessionNumber = 0;
    private SQLiteSerializer dbSerializer;
    private String difficulty;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_workout_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get MaterialListView
        workoutListView = (MyMaterialListView) findViewById(R.id.session_list_view);

        // Get divider for MaterialListView
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getDrawable(R.drawable.divider);
        } else {
            //noinspection deprecation
            drawable = getResources().getDrawable(R.drawable.divider);
        }

        // Set divider to MaterialListView
        workoutListView.setDivider(drawable); //TODO doesn't work well in landscape mode..

        dbSerializer = ((App) getApplication()).getDBSerializer();
        dbSerializer.open();

        final Activity activity = this;

        final EditText workoutName = (EditText) findViewById(R.id.nameWorkoutEditText);

        FloatingActionButton addWorkout = (FloatingActionButton) findViewById(R.id.create_fab);
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = workoutName.getText().toString();
                Workout newWorkout = new Workout(name, "custom", difficulty, dbSerializer);

                for (WorkoutSession session : workoutSessionList) {
                    newWorkout.addWorkoutSession(session, true, (int) Double.POSITIVE_INFINITY);
                }

                setResult(1);
                finish();
            }
        });

        Button addSession = (Button) findViewById(R.id.button);
        addSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View move = findViewById(R.id.moveLayout);
                if (move.getVisibility() == View.VISIBLE)
                    move.setVisibility(View.GONE);

                WorkoutSession session = new WorkoutSession("",dbSerializer);
                workoutSessionList.add(session);

                WorkoutSessionCreateCard card = new WorkoutSessionCreateCard(getApplicationContext(), session);

                card.setOnExerciseButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Intent intent = new Intent(activity, ExerciseListActivityCheckbox.class);
                        activity.startActivityForResult(intent, sessionNumber);
                    }
                });

                workoutListView.add(card);
                sessionNumber = sessionNumber + 1;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            ArrayList<Integer> exercisesSelected = data.getIntegerArrayListExtra("exercise_list");

            for (int id : exercisesSelected) {
                // Load exercise from id
                Exercise exercise = dbSerializer.loadExercise(id);
                Log.i(TAG, "requestCode: " + String.valueOf(requestCode - 1));
                Log.i(TAG, "workoutSessionList.size(): " + String.valueOf(workoutSessionList.size()));
                Log.i(TAG, "exercise: " + exercise.toString());

                // Add exercise to session
                workoutSessionList.get(requestCode - 1).addExerciseToWorkoutSession(exercise, (int) Double.POSITIVE_INFINITY, true);
            }

            workoutListView.onNotifyDataSetChanged(null);
        }
    }

    public void setDifficulty(View view) {
        switch (view.getId()) {

            case R.id.radioButtonEasy:
                difficulty = getString(R.string.difficulty_easy);
                break;
            case R.id.radioButtonMedium:
                difficulty = getString(R.string.difficulty_medium);
                break;
            case R.id.radioButtonHard:
                difficulty = getString(R.string.difficulty_hard);
                break;
            default:
                break;
        }
    }
}