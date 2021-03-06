package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
                    Log.i(TAG, "Sessione:" + session.toString());
                    newWorkout.addWorkoutSession(session, true, Integer.MAX_VALUE);
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

                        ArrayList<Integer> exercisesSelected = new ArrayList<>();

                        for (Exercise exercise : ((WorkoutSession)card.getTag()).getExercisesOfSession()) {
                            exercisesSelected.add(exercise.getId());
                        }

                        Intent intent = new Intent(activity, ExerciseListActivityCheckbox.class);
                        intent.putIntegerArrayListExtra("exercises_selected", exercisesSelected);
                        activity.startActivityForResult(intent, ((WorkoutSession) card.getTag()).getId());
                    }
                });

                workoutListView.add(card);

                hideKeyboard();

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

            WorkoutSession workoutSessionToUpdate = null;
            for (WorkoutSession workoutSession: workoutSessionList) {
                if (workoutSession.getId() == requestCode) {
                    workoutSessionToUpdate = workoutSession;
                    break;
                }
            }

            if (workoutSessionToUpdate != null) {
                ArrayList<Integer> exercises = new ArrayList<>();
                for (Exercise exercise : workoutSessionToUpdate.getExercisesOfSession()) {
                    exercises.add(exercise.getId());
                }

                for (int id : exercisesSelected) {

                    if (!exercises.contains(id)) {
                        // Load exercise from id
                        Exercise exercise = dbSerializer.loadExercise(id);
                        // Add exercise to session
                        workoutSessionToUpdate.addExerciseToWorkoutSession(exercise, Integer.MAX_VALUE, true);
                    }
                }
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


    /**
     * utility to hide keyboard
     */
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}