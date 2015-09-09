package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;

import com.dexafree.materialList.cards.SimpleCard;

import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.R;

/**
 * Card for WorkoutSession.
 */
public class UserWorkoutSessionCreateCard extends SimpleCard {

    /**
     * Indicates if the Session is complete or not.
     */
    private boolean complete;

    public UserWorkoutSessionCreateCard(Context context, UserWorkoutSession workoutSession) {
        super(context);
        // Set the tag of the card as explained here https://github.com/dexafree/MaterialList/wiki/Recovering-data-from-the-Cards
        this.setTag(workoutSession);
    }

    @Override
    public int getLayout() {
        // Set the right layout of the card
        return R.layout.user_workout_session_create_card_layout;
    }

    /**
     * Getter for complete attribute
     * @return true if the session is complete, false otherwise.
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Setter for complete attribute.
     * @param complete true if the session is complete, false otherwise.
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}