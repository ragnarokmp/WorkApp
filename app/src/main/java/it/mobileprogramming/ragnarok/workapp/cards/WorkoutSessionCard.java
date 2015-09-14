package it.mobileprogramming.ragnarok.workapp.cards;

import android.content.Context;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;

import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.R;

/**
 * Card for WorkoutSession.
 */
public class WorkoutSessionCard extends SimpleCard {

    private OnButtonPressListener buttonPressListener;
    private String titleDetailButon;
    private boolean statusDetailButton;

    public WorkoutSessionCard(Context context, WorkoutSession workoutSession) {
        super(context);
        // Set the tag of the card as explained here https://github.com/dexafree/MaterialList/wiki/Recovering-data-from-the-Cards
        this.setTag(workoutSession);
    }

    @Override
    public int getLayout() {
        // Set the right layout of the card
        return R.layout.workout_session_card_layout;
    }

    public void setOnDataClickListener (OnButtonPressListener buttonPressListener){

        this.buttonPressListener = buttonPressListener;
    }

    public OnButtonPressListener getOnDataClickListener (){

        return this.buttonPressListener;
    }

    public boolean getStatusDetailButton(){
        return this.statusDetailButton;
    }

    public void setStatusDetailButton(boolean status){
        this.statusDetailButton = status;
    }

    public void setTitleDetailButton(String value){
        this.titleDetailButon = value;
    }

    public String getTitleDetailButon(){
        return titleDetailButon;
    }
}