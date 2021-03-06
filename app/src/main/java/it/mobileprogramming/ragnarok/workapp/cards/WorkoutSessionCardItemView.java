package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import it.mobileprogramming.ragnarok.workapp.ExerciseListActivity;
import it.mobileprogramming.ragnarok.workapp.FeedbackActivity;
import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.R;
import it.mobileprogramming.ragnarok.workapp.StartExerciseActivity;

import static it.mobileprogramming.ragnarok.workapp.util.Util.boldTextBetweenTokens;

/**
 * View for WorkoutSessionCard.
 */
public class WorkoutSessionCardItemView extends CardItemView<WorkoutSessionCard> {
    /**
     * Divider margin for Android guidelines.
     */
    private final static int DIVIDER_MARGIN_DP = 16;

    /**
     * The session bound with the card.
     */
    private WorkoutSession workoutSession;
    private Context context;

    // Default constructors
    public WorkoutSessionCardItemView(Context context) {
        super(context);

        this.context = context;
    }

    public WorkoutSessionCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
    }

    public WorkoutSessionCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
    }

    @Override
    public void build(WorkoutSessionCard card) {

        super.build(card);

        // Get workout session from the card
        workoutSession = (WorkoutSession) card.getTag();
        // Generate session description with exercises and muscle used
        String sessionDescription = generateDescription();

        // Set all stuffs
        setImage();
        setDescription(sessionDescription);
        setTitles();
        setDuration();
        setExercises();
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
        TextView durationTitleTextView = (TextView) findViewById(R.id.duration_title_text_view);
        durationTitleTextView.setText(getResources().getString(R.string.duration_title).toUpperCase());

        TextView exercisesTitleTextView = (TextView) findViewById(R.id.exercises_title_text_view);
        exercisesTitleTextView.setText(getResources().getString(R.string.exercises_title).toUpperCase());
    }

    /**
     * This method allows to set session image.
     */
    public void setImage() {
        ImageView sessionImageView = (ImageView) findViewById(R.id.session_image_view);
        int resourceId = getResources().getIdentifier("exercise_" + String.valueOf(new Random().nextInt(7) + 1), "raw", context.getPackageName());

        Picasso.with(context)
                .load(resourceId)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_logo_colored)
                .into(sessionImageView);
    }

    /**
     * This method generates description of the session according to exercises.
     * @return the description in a human readable way.
     */
    private String generateDescription() {

        ArrayList<Exercise> exercises = workoutSession.getExercisesOfSession();
        String  description = "In the session of we will work on $";

        for (int i = 0; i < exercises.size(); i++) {

            String muscle = exercises.get(i).getMuscles();
            muscle = muscle.substring(0,muscle.length()-2); //There is \n escape character at the end
            description += muscle;

            if (i < exercises.size() - 2) {
                description += ", ";
            }

            if (i == exercises.size() - 2) {
                description += " and ";
            }
        }
        description += "$.\n\nIn details:\n";

        for (int i = 0; i < exercises.size(); i++) {

            Exercise currentEx = exercises.get(i);
            description += " $• " + currentEx.getSeries() + "$ series of $" + currentEx.getName() + "$";

            if (exercises.size() > 1 && i != exercises.size() - 1) {
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
        final TextView startNowTextButton = (TextView) findViewById(R.id.start_now_text_button);
        startNowTextButton.setVisibility(ButtonsVisible ? VISIBLE : GONE);

        startNowTextButton.setText(getResources().getString(R.string.start_now_button).toUpperCase());
        startNowTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        // Nothing to be done
                    }
                }.onButtonPressedListener(startNowTextButton, card);
            }
        });

        //hide start button
        startNowTextButton.setVisibility(GONE);

        final TextView detailsTextButton = (TextView) findViewById(R.id.date_text_button);
        detailsTextButton.setVisibility(ButtonsVisible ? VISIBLE : GONE);

        if (((WorkoutSessionCard) card).getStatusDetailButton())
            detailsTextButton.setVisibility(VISIBLE);
        else
            detailsTextButton.setVisibility(GONE);

        if (((WorkoutSessionCard) card).getTitleDetailButon() == null)
            detailsTextButton.setText(getResources().getString(R.string.date_text_button).toUpperCase());
        else
            detailsTextButton.setText(((WorkoutSessionCard) card).getTitleDetailButon());

        detailsTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OnButtonPressListener listener = ((WorkoutSessionCard) card).getOnDataClickListener();

                if (listener != null) {
                    listener.onButtonPressedListener(detailsTextButton, card);
                }
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
        descriptionTextView.setText(boldTextBetweenTokens(description, "$"));
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
