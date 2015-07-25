package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.ExerciseListActivity;
import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.R;
import it.mobileprogramming.ragnarok.workapp.StartExerciseActivity;

import static it.mobileprogramming.ragnarok.workapp.util.Util.boldTextBetweenTokens;

public class WorkoutSessionCardItemView extends CardItemView<WorkoutSessionCard> {
    private final static int DIVIDER_MARGIN_DP = 16;

    private UserWorkoutSession workoutSession;

    // Default constructors
    public WorkoutSessionCardItemView(Context context) {
        super(context);
    }

    public WorkoutSessionCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WorkoutSessionCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(WorkoutSessionCard card) {
        super.build(card);

        // Get workout session from the card
        workoutSession = card.getSession();

        // Choose session image from assets evaluating the session
        Drawable sessionDrawable = chooseSessionDrawable();
        // Generate session description with exercises and muscle used
        String sessionDescription = generateDescription();

        // Set all stuffs
        setImage(sessionDrawable);
        setDescription(sessionDescription);
        setTitles();
        setDuration();
        setExercises();
        setCompletion();
        setDivider(true, true);
        setButtons(card);
    }

    /**
     *
     * @return
     */
    private Drawable chooseSessionDrawable() {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getContext().getDrawable(R.drawable.ic_launcher);
        } else {
            drawable = getContext().getResources().getDrawable(R.drawable.ic_launcher);
        }
        return drawable;
    }

    /**
     *
     */
    private void setCompletion() {

    }

    /**
     *
     */
    private void setExercises() {
        ArrayList<Exercise> exercises = workoutSession.getExercisesOfSession();
        TextView exercisesTextView = (TextView) findViewById(R.id.exercises_text_view);
        exercisesTextView.setText(String.valueOf(exercises.size()));
    }

    /**
     *
     */
    private void setDuration() {
        //TODO FEDERICO: I have included also the reconvery time.
        int totalTime = 0;
        ArrayList<Exercise> exercises = workoutSession.getExercisesOfSession();
        for(int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            int timeForSeries = 0;
            timeForSeries += exercise.getFrequency()*exercise.getRepetition();
            int recoveryTime = 0;
            recoveryTime += exercise.getRecovery();
            totalTime = (timeForSeries+ recoveryTime)*exercise.getSeries();
        }
        TextView durationTextView = (TextView) findViewById(R.id.duration_text_view);
        durationTextView.setText("~" + totalTime/60 + "min");
    }

    /**
     *
     */
    private void setTitles() {
        TextView durationTitleTextView = (TextView) findViewById(R.id.duration_title_text_view);
        durationTitleTextView.setText(getResources().getString(R.string.duration_title).toUpperCase());

        TextView exercisesTitleTextView = (TextView) findViewById(R.id.exercises_title_text_view);
        exercisesTitleTextView.setText(getResources().getString(R.string.exercises_title).toUpperCase());

        TextView completionTitleTextView = (TextView) findViewById(R.id.completion_title_text_view);
        completionTitleTextView.setText(getResources().getString(R.string.completion_title).toUpperCase());
    }


    public void setImage(Drawable image) {

    }

    /**
     *
     * @return
     */
    private String generateDescription() {
        ArrayList<Exercise> exercises = workoutSession.getExercisesOfSession();
        //TODO FEDERICO : We have a problem with WorkoutSession and UserWorkoutSession. RESOLVED
        String description = "In the session of " + workoutSession.getDateSessionDate() + " we will work on ";
        int size = exercises.size();
        for (int i = 0; i < size; i++) {
            description += exercises.get(i).getMuscles();
            if (i < size - 2) {
                description += ", ";
            }
            if (i == size - 2) {
                description += "and";
            }
        }
        for (int i = 0; i < size; i++) {
            Exercise currentEx = exercises.get(i);
            description += "with " + currentEx.getSeries() + " series of " + currentEx.getName();
            if (i < size - 2) {
                description += ", ";
            }
            if (i == size - 2) {
                description += "and";
            }
        }
        return description + ".";
    }

    /**
     *
     * @param card
     */
    private void setButtons(final Card card) {
        final TextView startNowTextButton = (TextView) findViewById(R.id.start_now_text_button);
        final TextView detailsTextButton = (TextView) findViewById(R.id.details_text_button);

        startNowTextButton.setText(getResources().getString(R.string.start_now_button).toUpperCase());
        startNowTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    new OnButtonPressListener() {
                        @Override
                        public void onButtonPressedListener(View view, Card card) {
                            Intent intent = new Intent(getContext(), StartExerciseActivity.class);
                            getContext().startActivity(intent);
                        }
                    }.onButtonPressedListener(startNowTextButton, card);
            }
        });

        detailsTextButton.setText(getResources().getString(R.string.details_button).toUpperCase());
        detailsTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Intent intent = new Intent(getContext(), ExerciseListActivity.class);
                        getContext().startActivity(intent);
                    }
                }.onButtonPressedListener(detailsTextButton, card);
            }
        });
    }

    /**
     *
     * @param dividerVisible
     * @param fullWidthDivider
     */
    private void setDivider(Boolean dividerVisible, Boolean fullWidthDivider) {
        View divider = findViewById(R.id.card_divider);
        divider.setVisibility(dividerVisible ? VISIBLE : INVISIBLE);

        // After setting the visibility, we prepare the divider params according to the preferences
        if (dividerVisible) {
            // If the divider has to be from side to side, the margin will be 0
            if (fullWidthDivider) {
                ((ViewGroup.MarginLayoutParams) divider.getLayoutParams()).setMargins(0, 0, 0, 0);
            } else {
                int dividerMarginPx = (int) dpToPx(DIVIDER_MARGIN_DP);
                // Set the margin
                ((ViewGroup.MarginLayoutParams) divider.getLayoutParams()).setMargins(dividerMarginPx, 0, dividerMarginPx, 0);
            }
        }
    }

    /**
     *
     * @param description
     */
    private void setDescription(String description) {
        TextView descriptionTextView = (TextView) findViewById(R.id.description_text_view);
        descriptionTextView.setText(boldTextBetweenTokens(description, "%"));
    }

    /**
     *
     * @param dp
     * @return
     */
    private float dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     *
     * @param sp
     * @return
     */
    private float spToPx(int sp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(sp * (displayMetrics.scaledDensity / DisplayMetrics.DENSITY_DEFAULT));
    }
}
