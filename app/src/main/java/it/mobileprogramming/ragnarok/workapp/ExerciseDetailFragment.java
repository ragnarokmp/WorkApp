package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserExercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.util.App;

import static it.mobileprogramming.ragnarok.workapp.util.Util.boldTextBetweenTokens;

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
    private UserWorkoutSession userWorkoutSession;
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

        if (getArguments().containsKey("workoutSession")) {
            userWorkoutSession = savedInstanceState.getParcelable("workoutSession");
            User currentUser =   ((App) getActivity().getApplication()).getCurrentUser();
            assert userWorkoutSession != null;
            userWorkoutSession = dbSerializer.loadSession(userWorkoutSession.getId(),currentUser,userWorkoutSession.getDateSessionDate());
            ArrayList<Exercise> exercises = userWorkoutSession.getExercisesOfSession();
            exerciseID = savedInstanceState.getInt("exerciseID");
            currentExercise = (UserExercise) exercises.get(exerciseID);

        } else {

            if (getActivity().getIntent().getExtras() != null) {
                // Mobile
                currentExercise = getActivity().getIntent().getParcelableExtra("exerciseID");
            } else {
                // Tablet
                currentExercise = getArguments().getParcelable("exerciseID");
            }

        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_detail, container, false);

        if (currentExercise != null) {
            TextView durationTitleTextView = (TextView) rootView.findViewById(R.id.duration_title_text_view);
            durationTitleTextView.setText(getResources().getString(R.string.duration_title).toUpperCase());

            TextView exercisesTitleTextView = (TextView) rootView.findViewById(R.id.exercises_title_text_view);
            exercisesTitleTextView.setText(getResources().getString(R.string.repetitions_title).toUpperCase());

            TextView completionTitleTextView = (TextView) rootView.findViewById(R.id.completion_title_text_view);
            completionTitleTextView.setText(getResources().getString(R.string.completion_title).toUpperCase());

            ImageView exerciseImageView = (ImageView) rootView.findViewById(R.id.session_image_view);

            int resourceId = getResources().getIdentifier("exercise_" + String.valueOf((currentExercise.getId()) % 8), "raw", getActivity().getPackageName());

            Picasso.with(getActivity())
                    .load(resourceId)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_logo_colored)
                    .into(exerciseImageView);

            FloatingActionButton startFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.start_fab);
            startFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), StartExerciseActivity.class);
                    intent.putExtra("exerciseID", exerciseID);
                    getActivity().startActivity(intent);
                }
            });

            if (currentExercise instanceof UserExercise) {
                if (((UserExercise) currentExercise).isDone()) {
                    String done = "DONE";
                    ((TextView) rootView.findViewById(R.id.completion_text_view)).setText(done);
                } else {
                    String done = "NOT YET";
                    ((TextView) rootView.findViewById(R.id.completion_text_view)).setText(done);
                }
            } else {
                rootView.findViewById(R.id.completion_layout).setVisibility(View.GONE);
                startFloatingActionButton.setVisibility(View.GONE);
            }

            String description = "$";
            description += String.valueOf(currentExercise.getSeries()) + "$ " +
                           getResources().getString(R.string.exercise_detail_description_series) + " $" +
                           String.valueOf(currentExercise.getRepetition()) + "$ " +
                           currentExercise.getName() + " " +
                           getResources().getString(R.string.exercise_detail_description_with) + " $" +
                           String.valueOf(currentExercise.getRecovery()) + "$ " +
                           getResources().getString(R.string.exercise_detail_description_recrate) + " $" +
                           String.valueOf(currentExercise.getFrequency()) + "/sec$";

            ((TextView) rootView.findViewById(R.id.exercise_detail)).setText(boldTextBetweenTokens(description, "$"));

            ((TextView) rootView.findViewById(R.id.exercises_text_view)).setText(String.valueOf(currentExercise.getSeries() * currentExercise.getRepetition()));

            int totalTime = 0;
            totalTime += (currentExercise.getRepetition() / currentExercise.getFrequency()) * currentExercise.getSeries() + (currentExercise.getSeries() - 1)* currentExercise.getRecovery();
            ((TextView) rootView.findViewById(R.id.duration_text_view)).setText("~" + totalTime / 60 + " min");
        }

        FloatingActionButton startFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.start_fab);
        startFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartExerciseActivity.class);
                if (currentExercise instanceof UserExercise) {
                    intent.putExtra("workoutSession",userWorkoutSession);
                    intent.putExtra("exerciseID",exerciseID);
                } else {
                    intent.putExtra("exercise", currentExercise);
                }
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }
}
