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
        super (id,name,type,difficulty,aserializer);
    }
}
