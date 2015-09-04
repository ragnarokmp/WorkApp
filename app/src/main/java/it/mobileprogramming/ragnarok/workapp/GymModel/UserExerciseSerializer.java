package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paride on 18/06/15.
 */
public interface UserExerciseSerializer {
    /**
     * add a new UserExercise entry on DB
     * @param intIDExercise ID of the related exercise
     * @param boolIsDone set the state (0 not completed, 1 completed)
     * @param strComment set a comment for user session
     * @param userID set the userid of exercise
     * @param executionDate set the execution date
     * @param rating int from 0 to five (stars)
     * @return return the ID of the userExercise on DB
     */
    int createUserExercise(int intIDExercise, boolean boolIsDone, String strComment,int userID,Date executionDate,int rating) ;

    /**
     * get all the exercises of a user workout session
     * @param intIDUserWorkoutSession id of the session
     * @param userID userid
     * @param executionDate date of execution
     * @return id of user exercise
     */
    ArrayList<UserExercise> getExercisesOfAUserSession(int intIDUserWorkoutSession,int userID,Date executionDate);

    /**
     * update an user exercise on db
     * @param intIDUser user id
     * @param intIDExercise exercise id
     * @param executionDate date of execution
     * @param boolIsDone state of exercise (1 done 0 not done)
     * @param strComment set comment about this exercise
     * @param rating int from 0 to five (stars)
     */
    void updateUserExercise(int intIDUser,int intIDExercise ,Date executionDate,boolean boolIsDone,String strComment,int rating);

    /**
     * delete an user exercise
     * @param intIDUser userid
     * @param intIDExercise exercise id
     * @param date execution date
     */
    void deleteUserExercise(int intIDUser,int intIDExercise ,Date date);
    //UserExercise loadExercise(int intIDExercise,int intUserID,Date executionDate);
}
