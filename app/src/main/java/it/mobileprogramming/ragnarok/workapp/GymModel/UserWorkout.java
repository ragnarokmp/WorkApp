package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class models a user workout instance
 */
public class UserWorkout extends Workout{
    private int userID;
    private ArrayList<UserWorkoutSession> woSessions = new ArrayList<UserWorkoutSession>();
    private WorkoutSerializer serializer;

    /**
     * default constructor (not needed to save, user makes it after calling this), creates a new Workout
     * and saves it on DB
     * @param name workout name
     * @param userID user id owning this exercise
     * @param type workout type
     * @param difficulty workout difficulty
     * @param aserializer instance saving
     */
    public UserWorkout(String name,int userID,String type,String difficulty,WorkoutSerializer aserializer,UserSerializer userSerializer){
        super(name,type,difficulty,aserializer);
        this.userID     =    userID;
        this.serializer =   aserializer;
        userSerializer.addWorkoutForUser(userID,this.getIntWOID());
    }

    /**
     * constructor using an existing workout
     * @param id workout id
     * @param name workout name
     * @param userID owner
     * @param type type of workout
     * @param difficulty workout difficulty
     * @param aserializer instance saving
     */
    public UserWorkout(int id, String name,int userID,String type,String difficulty,WorkoutSerializer aserializer){
        super(id, name, type, difficulty, aserializer);
        this.userID =   userID;
    }

    /**
     * adds a session to a workout
     * @param aSession session to be added
     * @param saveToDb set true if is a new entry
     * @param progressive progressive order counter
     */
    public void addWorkoutSession(WorkoutSession aSession,boolean saveToDb,int progressive){
        if(aSession.getClass()==UserWorkoutSession.class){
            this.addUserWorkoutSession((UserWorkoutSession)aSession,progressive);
            if(saveToDb==true){
                super.addWorkoutSession(aSession,saveToDb,progressive);
            }
        }
        else{
            super.addWorkoutSession(aSession, saveToDb, progressive);
        }
    }

    /**
     * adds a UserWorkoutSession to this userWorkout
     * @param aWorkoutSession session to be added
     * @param progressive progressive order id
     */
    public void addUserWorkoutSession(UserWorkoutSession aWorkoutSession,int progressive){
        if(progressive>woSessions.size()){
            progressive =   woSessions.size();
        }
        this.woSessions.add(progressive,aWorkoutSession);
    }

    /**
     * returns all the sessions of this workout
     * @return arraylist of sessions
     */
    public ArrayList<UserWorkoutSession> getWoSessions(){
        ArrayList<UserWorkoutSession> userWorkoutSessionArrayList   =   new ArrayList<>();
        for(int i=0;i<this.woSessions.size();i++){
            userWorkoutSessionArrayList.add(this.woSessions.get(i).clone());
        }
        return userWorkoutSessionArrayList;
    }

    /**
     * getter of user id owner
     * @return id of the owner
     */
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
