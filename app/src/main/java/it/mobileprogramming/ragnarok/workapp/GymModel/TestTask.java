package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by paride on 20/06/15.
 * testing task for all classes in data model
 */
public class TestTask extends AsyncTask {
    Context myContext;
    public TestTask(Context aContext){
        this.myContext  =   aContext;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        System.out.println("Hello, i'm a testing Asynctask!");
        SQLiteSerializer    sqLiteSerializer    =   new SQLiteSerializer(this.myContext,"workapp.db");
        sqLiteSerializer.open();
        System.out.println("LOADING USERS");
        ArrayList<User> allUsers    =   sqLiteSerializer.getAllUsers();
        System.out.println("NEW USER");
        sqLiteSerializer.createNewUser("Francesco", "Totti", "AS Roma", 0, Singletons.formatFromString("27/09/1976"));
        sqLiteSerializer.getAllUsers();
        sqLiteSerializer.createNewWorkout("Workout di test", "tipo pro", "pippa");
        //for(int i=2;i<129;i++){
        //    sqLiteSerializer.deleteWorkout(i);
        //}
        ArrayList<Workout> workouts =   sqLiteSerializer.loadAllWorkouts(false);
        for(int i=0;i<workouts.size();i++){
            System.out.println(workouts.get(i).toString());
            ArrayList<WorkoutSession> sessions  =   workouts.get(i).getWorkoutSessions();
            System.out.println("Workout composto da " + sessions.size() + " sessioni");
            for(int k=0;k<sessions.size();k++){
                System.out.println(sessions.get(k).toString());
            }
        }
        for(int i=0;i<allUsers.size();i++){
            System.out.println(allUsers.get(i).toString());
            ArrayList<UserWorkout> wouts    =   sqLiteSerializer.loadWorkoutsForUser(allUsers.get(i).getIntUserID());
            System.out.println("User "+allUsers.get(i).getIntUserID()+" has "+wouts.size()+" workouts");
            for(int j=0;j<wouts.size();j++){
                System.out.println(wouts.get(j).toString());
            }
            ArrayList<WeightItem> wHistory  =   sqLiteSerializer.loadWeightHistory(allUsers.get(i).getIntUserID());
            for(int j=0;j<wHistory.size();j++){
                System.out.println(wHistory.get(j).toString());
            }
        }
        sqLiteSerializer.close();
        return null;
    }
}
