package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Commentable;
import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserExercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseFragment;

import static it.mobileprogramming.ragnarok.workapp.util.Util.boldTextBetweenTokens;

/**
 * A fragment representing a single Exercise detail screen.
 * This fragment is either contained in a {@link ExerciseListActivity}
 * in two-pane mode (on tablets) or a {@link ExerciseDetailActivity}
 * on handsets.
 */
public class ExerciseDetailFragment extends BaseFragment {


    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    int exerciseID;
    public static UserWorkoutSession userWorkoutSession;
    private Exercise currentExercise;
    private ArrayList<Exercise> exercises;

    private boolean workout_session  = false;
    private boolean workout_finished = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExerciseDetailFragment() {
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_exercise_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
        dbSerializer.open();
        if (!getArguments().containsKey("isTablet")) {
            if (getActivity().getIntent().hasExtra("workoutSession") ||
                getActivity().getIntent().hasExtra("next")) {
                workout_session = true;
                userWorkoutSession = getActivity().getIntent().getExtras().getParcelable("workoutSession");
                User currentUser = ((App) getActivity().getApplication()).getCurrentUser();
                assert userWorkoutSession != null;
                userWorkoutSession = dbSerializer.loadSession(userWorkoutSession.getId(), currentUser, userWorkoutSession.getDateSessionDate());
                exercises = userWorkoutSession.getExercisesOfSession();
                exerciseID = getActivity().getIntent().getExtras().getInt("exerciseID");
                currentExercise = (UserExercise) exercises.get(exerciseID);
            } else {
                currentExercise = getActivity().getIntent().getParcelableExtra("exercise");
            }

        } else {
            if (getArguments().containsKey("workoutSession")) {
                // if in workout session the play button can be activated
                workout_session = true;
                userWorkoutSession = getArguments().getParcelable("workoutSession");
                User currentUser = ((App) getActivity().getApplication()).getCurrentUser();
                assert userWorkoutSession != null;
                userWorkoutSession = dbSerializer.loadSession(userWorkoutSession.getId(), currentUser, userWorkoutSession.getDateSessionDate());
                exercises = userWorkoutSession.getExercisesOfSession();
                exerciseID = getArguments().getInt("exerciseID");
                currentExercise = (UserExercise) exercises.get(exerciseID);
            } else {
                currentExercise = getArguments().getParcelable("exercise");
            }
        }


    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        if (currentExercise != null) {
            TextView durationTitleTextView = (TextView) view.findViewById(R.id.duration_title_text_view);
            durationTitleTextView.setText(getResources().getString(R.string.duration_title).toUpperCase());

            TextView exercisesTitleTextView = (TextView) view.findViewById(R.id.exercises_title_text_view);
            exercisesTitleTextView.setText(getResources().getString(R.string.repetitions_title).toUpperCase());

            TextView completionTitleTextView = (TextView) view.findViewById(R.id.completion_title_text_view);
            completionTitleTextView.setText(getResources().getString(R.string.completion_title).toUpperCase());

            ImageView exerciseImageView = (ImageView) view.findViewById(R.id.session_image_view);

            int resourceId = getResources().getIdentifier("exercise_" + String.valueOf((currentExercise.getId() % 8) + 1), "raw", getActivity().getPackageName());

            Picasso.with(getActivity())
                    .load(resourceId)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_logo_colored)
                    .into(exerciseImageView);

            FloatingActionButton startFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.start_fab);
//            startFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), StartExerciseActivity.class);
//                    intent.putExtra("exerciseID", exerciseID);
//                    getActivity().startActivity(intent);
//                }
//            });

            if (workout_session) {
                if (((UserExercise) currentExercise).isDone()) {
                    String done = getResources().getString(R.string.exercise_done);
                    ((TextView) view.findViewById(R.id.completion_text_view)).setText(done);
                } else {
                    String done = getResources().getString(R.string.exercise_not_yet);
                    ((TextView) view.findViewById(R.id.completion_text_view)).setText(done);
                }
            } else {
                //view.findViewById(R.id.completion_layout).setVisibility(View.GONE);
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
            if (workout_session) {
                if (!((UserExercise) currentExercise).getComment().equals("") && ((UserExercise) currentExercise).getRating() != 0) {
                    description += "\n\nCommento: " + ((UserExercise) currentExercise).getComment();
                    description += "\nRating: " + ((UserExercise) currentExercise).getRating();
                } else if (((UserExercise) currentExercise).getRating() != 0) {
                    description += "\n\nRating: " + ((UserExercise) currentExercise).getRating();
                } else if (!((UserExercise) currentExercise).getComment().equals("")) {
                    description += "\n\nCommento: " + ((UserExercise) currentExercise).getComment();
                }
            }

            ((TextView) view.findViewById(R.id.exercise_detail)).setText(boldTextBetweenTokens(description, "$"));

            ((TextView) view.findViewById(R.id.exercises_text_view)).setText(String.valueOf(currentExercise.getSeries() * currentExercise.getRepetition()));

            int totalTime = 0;
            totalTime += (currentExercise.getRepetition() / currentExercise.getFrequency()) * currentExercise.getSeries() + (currentExercise.getSeries() - 1)* currentExercise.getRecovery();
            ((TextView) view.findViewById(R.id.duration_text_view)).setText("~" + totalTime / 60 + " min");
       }


        FloatingActionButton startFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.start_fab);
        Button skipButton                              = (Button)               view.findViewById(R.id.skip);
        Button feedbackButton                          = (Button)
                view.findViewById(R.id.feedback);

        if (workout_session || getActivity().getIntent().hasExtra("next")) {
            if (!((UserExercise) currentExercise).isDone())
                startFloatingActionButton.setVisibility(View.VISIBLE);
            else
                startFloatingActionButton.setVisibility(View.GONE);

            skipButton.setVisibility(View.VISIBLE);
            if (((UserExercise) currentExercise).isDone()) {
                feedbackButton.setVisibility(View.VISIBLE);
            }
            if ((exerciseID + 1) == (exercises.size())) {
                workout_finished = true;
                skipButton.setVisibility(View.GONE);
            }
        }

        startFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartExerciseActivity.class);
                intent.putExtra("exercise", (UserExercise) currentExercise);
                startActivityForResult(intent, 12);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((UserExercise) currentExercise).isDone();
                Intent intent;
                intent = new Intent(getActivity().getApplicationContext(), ExerciseDetailActivity.class);
                intent.putExtra("exercise", exercises.get(exerciseID++));
                intent.putExtra("workoutSession", userWorkoutSession);
                intent.putExtra("exerciseID", exerciseID++);
                intent.putExtra("next", true);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });


        feedbackButton.setOnClickListener(new View.OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View v) {
                // the exercise has been completed
                intent = new Intent(getActivity(), FeedbackActivity.class);
                intent.putExtra("item", (UserExercise) currentExercise);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String description = "$";

        // check for intent presence on comment setted
        if (requestCode == 12) {
            description += String.valueOf(currentExercise.getSeries()) + "$ " +
                    getResources().getString(R.string.exercise_detail_description_series) + " $" +
                    String.valueOf(currentExercise.getRepetition()) + "$ " +
                    currentExercise.getName() + " " +
                    getResources().getString(R.string.exercise_detail_description_with) + " $" +
                    String.valueOf(currentExercise.getRecovery()) + "$ " +
                    getResources().getString(R.string.exercise_detail_description_recrate) + " $" +
                    String.valueOf(currentExercise.getFrequency()) + "/sec$";
            if (workout_session) {
                if (((UserExercise) data.getParcelableExtra("exercise")).isDone()) {
                    (view.findViewById(R.id.feedback)).setVisibility(View.VISIBLE);
                    (view.findViewById(R.id.start_fab)).setVisibility(View.GONE);
                    ((TextView) view.findViewById(R.id.completion_text_view)).setText(getResources().getString(R.string.exercise_done));
                }
            }
        } else {
            if (data != null) {
                if (data.hasExtra("commented")) {
                    UserExercise ue = data.getParcelableExtra("commented");
                    ((Commentable) currentExercise).setComment(ue.getComment());
                    ((Commentable) currentExercise).setRating(ue.getRating());

                    description += String.valueOf(currentExercise.getSeries()) + "$ " +
                            getResources().getString(R.string.exercise_detail_description_series) + " $" +
                            String.valueOf(currentExercise.getRepetition()) + "$ " +
                            currentExercise.getName() + " " +
                            getResources().getString(R.string.exercise_detail_description_with) + " $" +
                            String.valueOf(currentExercise.getRecovery()) + "$ " +
                            getResources().getString(R.string.exercise_detail_description_recrate) + " $" +
                            String.valueOf(currentExercise.getFrequency()) + "/sec$";
                    if (workout_session) {
                        if (!((UserExercise) currentExercise).getComment().equals("") && ((UserExercise) currentExercise).getRating() != 0) {
                            description += "\n\nCommento: " + ((UserExercise) currentExercise).getComment();
                            description += "\nRating: " + ((UserExercise) currentExercise).getRating();
                        } else if (((UserExercise) currentExercise).getRating() != 0) {
                            description += "\n\nRating: " + ((UserExercise) currentExercise).getRating();
                        } else if (!((UserExercise) currentExercise).getComment().equals("")) {
                            description += "\n\nCommento: " + ((UserExercise) currentExercise).getComment();
                        }
                    }
                }
            }
        }



        ((TextView) view.findViewById(R.id.exercise_detail)).setText(boldTextBetweenTokens(description, "$"));
        ((TextView) view.findViewById(R.id.exercises_text_view)).setText(String.valueOf(currentExercise.getSeries() * currentExercise.getRepetition()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO federico: da implementare
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        //TODO federico: da implementare
        super.onResume();
        if (workout_session) {
            if (((UserExercise) currentExercise).isDone()) {
                FloatingActionButton start = (FloatingActionButton) view.findViewById(R.id.start_fab);
                start.setVisibility(View.GONE);
                Button feedback = (Button) view.findViewById(R.id.feedback);
                feedback.setVisibility(View.VISIBLE);
            }
        }
    }
}
