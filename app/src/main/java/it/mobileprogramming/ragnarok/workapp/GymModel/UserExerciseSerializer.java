package it.mobileprogramming.ragnarok.workapp.GymModel;

/**
 * Created by paride on 18/06/15.
 */
public interface UserExerciseSerializer {
    int createUserExercise(int intIDExercise,int intIDUserWorkoutSession,boolean boolIsDone,String strComment);
    void updateUserExercise(int intIDUserWorkoutSession,boolean boolIsDone,String strComment);
}
