package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

public interface WorkoutSerializer {
    int createNewWorkout(String name, String type, String difficulty);
    void addSessiontoWorkout(int idWorkout,int idSession,int progressive);
    Workout loadWorkout(int id);
    void deleteWorkout(int id);
    ArrayList<WorkoutSession> loadAllWorkoutSessionsForWorkout(int woID);
    void updateWorkout(int id,String name, String type, String difficulty);
    void removeSessionFromWorkout(int id,int seId);
    ArrayList<Workout> loadAllWorkouts(boolean includeCustom);
    UserWorkout loadUserWorkout(int woId,int userID) throws DataException;
}
