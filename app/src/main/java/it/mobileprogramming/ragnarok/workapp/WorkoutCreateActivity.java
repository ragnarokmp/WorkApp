package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.cards.UserWorkoutSessionCreateCard;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;
import it.mobileprogramming.ragnarok.workapp.util.MyMaterialListView;

public class WorkoutCreateActivity extends BaseActivityWithToolbar {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_workout_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get MaterialListView
        final MyMaterialListView workoutListView = (MyMaterialListView) findViewById(R.id.session_list_view);

        // Get divider for MaterialListView
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getDrawable(R.drawable.divider);
        } else {
            //noinspection deprecation
            drawable = getResources().getDrawable(R.drawable.divider);
        }

        // Set divider to MaterialListView
        workoutListView.setDivider(drawable); //TODO doesn't work well in landscape mode..

        // Set onItemTouchListener
        workoutListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(CardItemView cardItemView, int i) {

            }

            @Override
            public void onItemLongClick(CardItemView cardItemView, int i) {

            }
        });

        //UserWorkout newWorkout = new UserWorkout("Workout a runtime",1,"test","facile",dbSerializer,dbSerializer);
        //newWorkout.addWorkoutSession(newSession,true,0);
        //Exercise ex1  =   new Exercise(dbSerializer,2,"esercizio1",1,1,1,"nulla","bicipite");
        //UserExercise anExercise =   new UserExercise(1,new Date(),ex1,false,"so sempre bone",dbSerializer,dbSerializer,2);
        //newSession.addExerciseToWorkoutSession(anExercise, 0, true);
        //Exercise ex2  =   new Exercise(dbSerializer,2,"esercizio2",1,1,1,"nulla","gambe");
        //UserExercise anExercise2 =   new UserExercise(1,new Date(),ex2,false,"ce piace",dbSerializer,dbSerializer,3);
        //newSession.addExerciseToWorkoutSession(anExercise2, 0, true);

        /*FloatingActionButton addWorkout = (FloatingActionButton) findViewById(R.id.action_add);
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(this, WorkoutCreateActivity.class);
                //startActivity(intent);
            }
        });*/

        final SQLiteSerializer dbSerializer = ((App) getApplication()).getDBSerializer();
        dbSerializer.open();

        Button addSession = (Button) findViewById(R.id.button);
        addSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View move = findViewById(R.id.moveLayout);
                if (move.getVisibility() == View.VISIBLE)
                    move.setVisibility(View.GONE);

                WorkoutSession testSession = new WorkoutSession("",dbSerializer);
                UserWorkoutSession newSession = new UserWorkoutSession("",1,dbSerializer,dbSerializer,new Date(),testSession.getId(),"",true,0);

                UserWorkoutSessionCreateCard card = new UserWorkoutSessionCreateCard(getApplicationContext(), newSession);
                workoutListView.add(card);
            }
        });
    }

}
