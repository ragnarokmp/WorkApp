package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
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
    private static List<WorkoutSession> workoutSessionList = new ArrayList<>();
    private int sessionNumber = 0;
    private SQLiteSerializer dbSerializer;
    private String difficulty;
    private String workoutName;
    private FloatingActionButton addWorkout;

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

        addWorkout = (FloatingActionButton) findViewById(R.id.create_fab);
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                difficulty = difficulty != null ? difficulty : getString(R.string.difficulty_easy);
                Workout newWorkout = new Workout(workoutName, "custom", difficulty, dbSerializer);

                for (WorkoutSession session : workoutSessionList) {
                    newWorkout.addWorkoutSession(session, true, (int) Double.POSITIVE_INFINITY);
                }

                setResult(1);
                finish();
            }
        });

        final Button addSession = (Button) findViewById(R.id.button);
        addSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View move = findViewById(R.id.moveLayout);
                if (move.getVisibility() == View.VISIBLE)
                    move.setVisibility(View.GONE);

                WorkoutSession session = new WorkoutSession("", dbSerializer);
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

        EditText workoutNameEditText = (EditText) findViewById(R.id.nameWorkoutEditText);
        workoutNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                workoutName = s.toString();
                if (count > 0) {
                    addSession.setVisibility(View.VISIBLE);
                } else {
                    addSession.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                // Add exercise to session
                workoutSessionList.get(requestCode - 1).addExerciseToWorkoutSession(exercise, (int) Double.POSITIVE_INFINITY, true);
            }

            workoutListView.onNotifyDataSetChanged(null);

            addWorkout.setVisibility(View.VISIBLE);
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