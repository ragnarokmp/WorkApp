package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserExercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.util.App;

/**
 * A fragment representing a single Exercise detail screen.
 * This fragment is either contained in a {@link ExerciseListActivity}
 * in two-pane mode (on tablets) or a {@link ExerciseDetailActivity}
 * on handsets.
 */
public class ExerciseDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    int exerciseID;

    /**
     * The dummy content this fragment is presenting.
     */
    private Exercise currentExercise;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExerciseDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
        dbSerializer.open();
        if (getArguments().containsKey("userID")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            ArrayList<UserWorkout> usWorkouts = dbSerializer.loadWorkoutsForUser(getActivity().getIntent().getExtras().getInt("userID"));
            ArrayList<UserWorkoutSession> firstWorkoutSessions = usWorkouts.get(0).getWoSessions();
            UserWorkoutSession userWorkoutSession = firstWorkoutSessions.get(getActivity().getIntent().getExtras().getInt("workoutID"));
            ArrayList<Exercise> exercises = userWorkoutSession.getExercisesOfSession();
            exerciseID = getActivity().getIntent().getExtras().getInt("exerciseID");
            currentExercise = (UserExercise) exercises.get(exerciseID); //DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        } else {
            exerciseID = getActivity().getIntent().getExtras().getInt("exerciseID");
            currentExercise = dbSerializer.loadExercise(exerciseID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (currentExercise != null) {
            TextView durationTitleTextView = (TextView) rootView.findViewById(R.id.duration_title_text_view);
            durationTitleTextView.setText(getResources().getString(R.string.duration_title).toUpperCase());

            TextView exercisesTitleTextView = (TextView) rootView.findViewById(R.id.exercises_title_text_view);
            exercisesTitleTextView.setText(getResources().getString(R.string.exercises_title).toUpperCase());

            TextView completionTitleTextView = (TextView) rootView.findViewById(R.id.completion_title_text_view);
            completionTitleTextView.setText(getResources().getString(R.string.completion_title).toUpperCase());

            if (currentExercise instanceof UserExercise) {
                if (((UserExercise) currentExercise).isDone()) {
                    String done = "DONE";
                    ((TextView) rootView.findViewById(R.id.completion_text_view)).setText(done);
                } else {
                    String done = "NOT YET";
                    ((TextView) rootView.findViewById(R.id.completion_text_view)).setText(done);
                }
            }
            String description = getResources().getString(R.string.exercise_detail_description_intro) + "\n";
            description += String.valueOf(currentExercise.getSeries()) + " " +
                           getResources().getString(R.string.exercise_detail_description_series) + " " +
                           String.valueOf(currentExercise.getRepetition()) + " " +
                           currentExercise.getName() + " " +
                           getResources().getString(R.string.exercise_detail_description_with) + " " +
                           String.valueOf(currentExercise.getRecovery()) + " " +
                           getResources().getString(R.string.exercise_detail_description_recrate) + " " +
                           String.valueOf(currentExercise.getFrequency()) + "/sec)";

                    ((TextView) rootView.findViewById(R.id.exercise_detail)).setText(description);
            int totalTime = 0;
            totalTime += (currentExercise.getRepetition() / currentExercise.getFrequency()) * currentExercise.getSeries() + (currentExercise.getSeries() - 1)* currentExercise.getRecovery();
            ((TextView) rootView.findViewById(R.id.duration_text_view)).setText("~" + totalTime/60 + " min");


        }

        FloatingActionButton startFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.start_fab);
        startFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartExerciseActivity.class);
                intent.putExtra("exerciseID",exerciseID);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }
}
