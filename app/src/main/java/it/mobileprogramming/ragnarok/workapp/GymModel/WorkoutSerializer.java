package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;

public interface WorkoutSerializer {
    public int createNewWorkout(String name, String type, String difficulty);
    public void addSessiontoWorkout(int idWorkout,int idSession);
    public Workout loadWorkout(int id);
    public void deleteWorkout(int id);
    public void updateWorkout(int id,String name, String type, String difficulty);
    public void removeSessionFromWorkout(int id,int seId);
    public ArrayList<Workout> loadAllWorkouts(boolean includeCustom);
    public UserWorkout loadUserWorkout(int woId,int userID);
}
