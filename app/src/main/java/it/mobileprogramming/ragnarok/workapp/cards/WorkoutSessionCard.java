package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;

import com.dexafree.materialList.cards.SimpleCard;

import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.R;

public class WorkoutSessionCard extends SimpleCard {

    /**
     *
     */
    private WorkoutSession workoutSession;

    /**
     *
     */
    private boolean complete;

    public WorkoutSessionCard(Context context) {
        super(context);
    }

    public WorkoutSessionCard(Context context, WorkoutSession workoutSession) {
        super(context);
        this.workoutSession = workoutSession;
    }

    @Override
    public int getLayout(){
        return R.layout.workout_session_card_layout;
    }

    /**
     *
     * @return
     */
    public WorkoutSession getSession() {
        return this.workoutSession;
    }

    /**
     *
     * @return
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     *
     * @param complete
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}