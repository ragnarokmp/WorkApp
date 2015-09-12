package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;

import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.R;

/**
 * Card for WorkoutSession.
 */
public class WorkoutSessionCreateCard extends SimpleCard {

    /**
     * Indicates if the Session is complete or not.
     */
    private OnButtonPressListener onExerciseButtonPressedListener;

    public WorkoutSessionCreateCard(Context context, WorkoutSession workoutSession) {
        super(context);
        // Set the tag of the card as explained here https://github.com/dexafree/MaterialList/wiki/Recovering-data-from-the-Cards
        this.setTag(workoutSession);
    }

    @Override
    public int getLayout() {
        // Set the right layout of the card
        return R.layout.user_workout_session_create_card_layout;
    }

    public OnButtonPressListener getOnExerciseButtonPressedListener() {
        return onExerciseButtonPressedListener;
    }

    public void setOnExerciseButtonPressedListener(OnButtonPressListener onExerciseButtonPressedListener) {
        this.onExerciseButtonPressedListener = onExerciseButtonPressedListener;
    }
}