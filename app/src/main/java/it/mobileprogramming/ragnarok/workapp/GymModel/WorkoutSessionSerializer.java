package it.mobileprogramming.ragnarok.workapp.GymModel;

/**
 * Created by paride on 18/06/15.
 */
public interface WorkoutSessionSerializer {
    public int createNewWorkoutSession(int id, int progressive, String filepath);
    public WorkoutSession loadWorkoutSession(int id);
    public void deleteWorkoutSession (int id);
    public void updateWorkout(int id,int progressive,String photopath);
}
