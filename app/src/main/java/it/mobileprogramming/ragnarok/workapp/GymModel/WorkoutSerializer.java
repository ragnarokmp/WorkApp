package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;

public interface WorkoutSerializer {
    public int createNewWorkout(String name, String type, String difficulty);
    public void addExercisetoWorkout(int idWorkout,int idExercise);
    public Workout loadWorkout(int id);
    public void deleteWorkout(int id);
    public void updateWorkout(int id,String name, String type, String difficulty);
    public void removeExerciseFromWorkout(int id,int exId);
    public ArrayList<Workout> loadAllWorkouts(boolean includeCustom);
}
