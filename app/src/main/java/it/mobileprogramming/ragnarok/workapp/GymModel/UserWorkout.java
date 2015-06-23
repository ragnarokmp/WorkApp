package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paride on 21/06/15.
 */
public class UserWorkout extends Workout{
    private ArrayList<UserWorkoutSession> woSessions = new ArrayList<UserWorkoutSession>();
    public UserWorkout(String name,String type,String difficulty,WorkoutSerializer aserializer){
        super(name,type,difficulty,aserializer);
    }
    public UserWorkout(int id, String name,String type,String difficulty,WorkoutSerializer aserializer){
        super(id, name, type, difficulty, aserializer);
    }
    public void addWorkoutSession(WorkoutSession aSession,boolean saveToDb){
        if(aSession.getClass()==UserWorkoutSession.class){
            this.addUserWorkoutSession((UserWorkoutSession)aSession);
        }
        else{
            super.addWorkoutSession(aSession,saveToDb);
        }
    }
    public void addUserWorkoutSession(UserWorkoutSession aWorkoutSession){
        this.woSessions.add(aWorkoutSession);
    }

    public ArrayList<UserWorkoutSession> getWoSessions(){
        ArrayList<UserWorkoutSession> userWorkoutSessionArrayList   =   new ArrayList<>();
        for(int i=0;i<this.woSessions.size();i++){
            userWorkoutSessionArrayList.add(this.woSessions.get(i).clone());
        }
        return userWorkoutSessionArrayList;
    }

    @Override
    public String toString() {
        return super.toString()+"UserWorkout{" +
                "woSessions=" + woSessions +
                '}';
    }
}
