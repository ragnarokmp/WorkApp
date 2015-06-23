package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;

/**
 * Created by paride on 18/06/15.
 */
public interface UserExerciseSerializer {
    int createUserExercise(int intIDExercise,int intIDUserWorkoutSession,boolean boolIsDone,String strComment);
    ArrayList<UserExcercise> getExercisesOfASession(int intIDUserWorkoutSession);
    void updateUserExercise(int intIDUserWorkoutSession,int intIDExercise ,boolean boolIsDone,String strComment);
}
