package it.mobileprogramming.ragnarok.workapp.GymModel;

public interface WorkoutSerializer {
    public int createNewWorkout(String name, String type, String difficulty);
    public Workout loadWorkout(int id);
    public Workout updateWorkout(int id,String name, String type, String difficulty);
}
