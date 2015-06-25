package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paride on 18/06/15.
 */
public interface UserExerciseSerializer {
    int createUserExercise(int intIDExercise, boolean boolIsDone, String strComment,int userID,Date executionDate) ;
    ArrayList<UserExcercise> getExercisesOfAUserSession(int intIDUserWorkoutSession,int userID,Date executionDate);
    void updateUserExercise(int intIDUser,int intIDExercise ,Date executionDate,boolean boolIsDone,String strComment);
    //UserExcercise loadExercise(int intIDExercise,int intUserID,Date executionDate);
}
