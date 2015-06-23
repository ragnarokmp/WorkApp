package it.mobileprogramming.ragnarok.workapp.GymModel;

/**
 * Created by paride on 18/06/15.
 */
public interface WorkoutSessionSerializer {
    int createNewWorkoutSession(int progressive, String filepath);
    WorkoutSession loadWorkoutSession(int id);
    void deleteWorkoutSession (int id);
    void updateWorkoutSession(int id,int progressive,String photopath);
    void addExerciseForWorkoutSession(int intIDSession, int intIDExercise);
}
