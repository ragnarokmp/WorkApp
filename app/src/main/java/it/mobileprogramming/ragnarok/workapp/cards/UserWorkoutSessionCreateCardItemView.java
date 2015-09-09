package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import it.mobileprogramming.ragnarok.workapp.ExeciseListActivityCheckbox;
import it.mobileprogramming.ragnarok.workapp.ExerciseListActivity;
import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.R;

/**
 * View for WorkoutSessionCard.
 */
public class UserWorkoutSessionCreateCardItemView extends CardItemView<UserWorkoutSessionCreateCard> {
    /**
     * Divider margin for Android guidelines.
     */
    private final static int DIVIDER_MARGIN_DP = 16;

    /**
     * The session bound with the card.
     */
    private UserWorkoutSession workoutSession;

    // Default constructors
    public UserWorkoutSessionCreateCardItemView(Context context) {
        super(context);
    }

    public UserWorkoutSessionCreateCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserWorkoutSessionCreateCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(UserWorkoutSessionCreateCard card) {

        super.build(card);

        // Get workout session from the card
        workoutSession = (UserWorkoutSession) card.getTag();
        // Choose session image from assets evaluating the session
        Drawable sessionDrawable = chooseSessionDrawable();
        // Generate session description with exercises and muscle used
        String sessionDescription = generateDescription();

        // Set all stuffs
        setImage(sessionDrawable);
        setDescription(sessionDescription);
        setTitles();
        //setDuration();
        setExercises();
        //setCompletion();
        setDivider(true, true);
        setButtons(true, card);
    }

    /**
     * This method choose an image to show of the session according to exercises.
     * @return the chosen drawable image.
     */
    private Drawable chooseSessionDrawable() {

        int resourceId = getResources().getIdentifier("exercise_" + String.valueOf(new Random().nextInt(7) + 1), "raw", getContext().getPackageName());

        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getContext().getDrawable(resourceId);
        } else {
            drawable = getContext().getResources().getDrawable(resourceId);
        }

        return drawable;
    }

    /**
     * This method allows to set the completion of the session.
     */
    private void setCompletion() {
        TextView completionTextView = (TextView) findViewById(R.id.completion_text_view);
        try {
            completionTextView.setText(String.valueOf(workoutSession.allExerciseDone()) + "%");  //TODO is a try/catch correct?
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allows to set the total number of exercise of the session.
     */
    private void setExercises() {
        ArrayList<Exercise> exercises = workoutSession.getExercisesOfSession();
        TextView exercisesTextView = (TextView) findViewById(R.id.exercises_text_view);
        exercisesTextView.setText(String.valueOf(exercises.size()));
    }

    /**
     * This method allows to set session total duration (duration of exercises + recovery time).
     */
    private void setDuration() {
        int totalTime = 0;
        ArrayList<Exercise> exercises = workoutSession.getExercisesOfSession();
        for(int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);

            int timeForSeries = 0;
            timeForSeries += exercise.getFrequency() * exercise.getRepetition();
            int recoveryTime = 0;
            recoveryTime += exercise.getRecovery();
            totalTime += (timeForSeries + recoveryTime) * exercise.getSeries();
        }
        TextView durationTextView = (TextView) findViewById(R.id.duration_text_view);
        durationTextView.setText("~" + totalTime / 60 + " min");
    }

    /**
     * This method allows to set the overlays titles.
     */
    private void setTitles() {

        //TextView durationTitleTextView = (TextView) findViewById(R.id.duration_title_text_view);
        //durationTitleTextView.setText(getResources().getString(R.string.duration_title).toUpperCase());

        TextView exercisesTitleTextView = (TextView) findViewById(R.id.exercises_title_text_view);
        exercisesTitleTextView.setText(getResources().getString(R.string.exercises_title).toUpperCase());

        //TextView completionTitleTextView = (TextView) findViewById(R.id.completion_title_text_view);
        //completionTitleTextView.setText(getResources().getString(R.string.completion_title).toUpperCase());
    }

    /**
     * This method allows to set session image.
     * @param image the image to set.
     */
    public void setImage(Drawable image) {
        ImageView sessionImageView = (ImageView) findViewById(R.id.session_image_view);
        sessionImageView.setImageDrawable(image);
    }

    /**
     * This method generates description of the session according to exercises.
     * @return the description in a human readable way.
     */
    private String generateDescription() {

        ArrayList<Exercise> exercises = workoutSession.getExercisesOfSession();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String description;

        //TODO: @federico improve with the recognition of the actual day...
        if ((workoutSession.getDateSessionDate().getTime() - date.getTime()) == 1) {
            description = "$Tomorrow$ we will work on $";
        } else if ((workoutSession.getDateSessionDate().getTime() - date.getTime()) == 2) {
            description = "$The day after tomorrow we will work on $";
        } else {
            description = "In the session of $" + workoutSession.getDateSessionDate() + "$ we will work on $";
        }
        int size = exercises.size();
        for (int i = 0; i < size; i++) {
            String muscle = exercises.get(i).getMuscles();
            muscle = muscle.substring(0,muscle.length()-2); //There is \n escape character at the end
            description += muscle;
            if (i < size - 2) {
                description += ", ";
            }
            if (i == size - 2) {
                description += " and ";
            }
        }
        description += "$.\n\nIn details:\n";
        for (int i = 0; i < size; i++) {
            Exercise currentEx = exercises.get(i);
            description += " $• " + currentEx.getSeries() + "$ series of $" + currentEx.getName() + "$";
            if (size > 1 && i != size - 1) {
                description += "\n";
            }
        }
        return description;
    }

    /**
     * This method allows to set the buttons.
     * @param ButtonsVisible true if the buttons have to be visible, false otherwise.
     * @param card the card.
     */
    private void setButtons(Boolean ButtonsVisible, final Card card) {

        final TextView startNowTextButton = (TextView) findViewById(R.id.date_text_button);
        startNowTextButton.setVisibility(ButtonsVisible ? VISIBLE : GONE);

        startNowTextButton.setText(getResources().getString(R.string.date_text_button).toUpperCase());
        startNowTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {

                    }
                }.onButtonPressedListener(startNowTextButton, card);
            }
        });

        final TextView detailsTextButton = (TextView) findViewById(R.id.exercises_text_button);
        detailsTextButton.setVisibility(ButtonsVisible ? VISIBLE : GONE);

        detailsTextButton.setText(getResources().getString(R.string.exercises_text_button).toUpperCase());
        detailsTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Intent intent = new Intent(getContext(), ExeciseListActivityCheckbox.class);
                        getContext().startActivity(intent);
                    }
                }.onButtonPressedListener(detailsTextButton, card);
            }
        });
    }

    /**
     * This method allows to set the divider.
     * @param dividerVisible true if the divider has to be visible, false otherwise.
     * @param fullWidthDivider true if the divider must occupy all width's card, false otherwise.
     */
    private void setDivider(Boolean dividerVisible, Boolean fullWidthDivider) {
        View divider = findViewById(R.id.card_divider);
        divider.setVisibility(dividerVisible ? VISIBLE : GONE);

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
     * This method allow to set the description text.
     * @param description the description text to set.
     */
    private void setDescription(String description) {
        TextView descriptionTextView = (TextView) findViewById(R.id.description_text_view);
        descriptionTextView.setVisibility(GONE);
        //descriptionTextView.setText(boldTextBetweenTokens(description, "$"));
    }

    /**
     * Utility method that converts dp to px.
     * @param dp the dp to convert in px.
     * @return the conversion in px of the dp.
     */
    private float dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Utility method that converts sp to px.
     * @param sp the sp to convert in px.
     * @return the conversion in px of the sp.
     */
    private float spToPx(int sp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(sp * (displayMetrics.scaledDensity / DisplayMetrics.DENSITY_DEFAULT));
    }
}
