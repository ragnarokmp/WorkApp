package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paride on 21/06/15.
 */
public class UserWorkout extends Workout{
    private int userID;
    private ArrayList<UserWorkoutSession> woSessions = new ArrayList<UserWorkoutSession>();
    public UserWorkout(String name,int userID,String type,String difficulty,WorkoutSerializer aserializer){
        super(name,type,difficulty,aserializer);
        this.userID =    userID;
    }
    public UserWorkout(int id, String name,int userID,String type,String difficulty,WorkoutSerializer aserializer){
        super(id, name, type, difficulty, aserializer);
        this.userID =   userID;
    }
    public void addWorkoutSession(WorkoutSession aSession,boolean saveToDb,int progressive){
        if(aSession.getClass()==UserWorkoutSession.class){
            this.addUserWorkoutSession((UserWorkoutSession)aSession,progressive);
        }
        else{
            super.addWorkoutSession(aSession,saveToDb,progressive);
        }
    }
    public void addUserWorkoutSession(UserWorkoutSession aWorkoutSession,int progressive){
        if(progressive>woSessions.size()){
            progressive =   woSessions.size();
        }
        this.woSessions.add(progressive,aWorkoutSession);
    }

    public ArrayList<UserWorkoutSession> getWoSessions(){
        ArrayList<UserWorkoutSession> userWorkoutSessionArrayList   =   new ArrayList<>();
        for(int i=0;i<this.woSessions.size();i++){
            userWorkoutSessionArrayList.add(this.woSessions.get(i).clone());
        }
        return userWorkoutSessionArrayList;
    }

    public int getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return super.toString()+"UserWorkout{" +
                "woSessions=" + woSessions +
                '}';
    }
}
