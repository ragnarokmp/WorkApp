package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.R;

/**
 * View for WorkoutSessionCard.
 */
public class WorkoutSessionCreateCardItemView extends CardItemView<WorkoutSessionCreateCard> {
    /**
     * Divider margin for Android guidelines.
     */
    private final static int DIVIDER_MARGIN_DP = 16;
    private final Context context;

    /**
     * The session bound with the card.
     */
    private WorkoutSession workoutSession;

    // Default constructors
    public WorkoutSessionCreateCardItemView(Context context) {
        super(context);
        this.context = context;
    }

    public WorkoutSessionCreateCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public WorkoutSessionCreateCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    public void build(WorkoutSessionCreateCard card) {

        super.build(card);

        // Get workout session from the card
        workoutSession = (WorkoutSession) card.getTag();

        // Set all stuffs
        setImage();
        setTitles();
        setExercises();
        setDivider(true, true);
        setButtons(true, card);
    }

    /**
     * This method allows to set the total number of exercise of the session.
     */
    private void setExercises() {
        ArrayList<Exercise> exercises = workoutSession.getExercisesOfSession();
        Log.i("ITEMVIEW", exercises.toString());
        TextView exercisesTextView = (TextView) findViewById(R.id.exercises_text_view);
        exercisesTextView.setText(String.valueOf(exercises.size()));
    }

    /**
     * This method allows to set the overlays titles.
     */
    private void setTitles() {

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
     * This method allows to set the buttons.
     * @param ButtonsVisible true if the buttons have to be visible, false otherwise.
     * @param card the card.
     */
    private void setButtons(Boolean ButtonsVisible, final Card card) {

        final TextView detailsTextButton = (TextView) findViewById(R.id.exercises_text_button);
        detailsTextButton.setVisibility(ButtonsVisible ? VISIBLE : GONE);

        detailsTextButton.setText(getResources().getString(R.string.exercises_text_button).toUpperCase());
        detailsTextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OnButtonPressListener listener = ((WorkoutSessionCreateCard) card).getOnExerciseButtonPressedListener();

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
