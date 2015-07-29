package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Interface needed to save a workout session on persistence layer
 */
public interface UserWorkoutSessionSerializer {
    /**
     * loads session from DB
     * @param intUserSessionID id of workout session
     * @param anUser id of the user
     * @param sessionDate date of the session
     * @return  sessionID
     */
    UserWorkoutSession          loadSession(int intUserSessionID,User anUser,Date sessionDate);

    /**
     * deletes a session from DB
     * @param intUserSessionID id of workout session
     * @param anUserId id of the user
     * @param sessionDate date of the session
     */
    void                        deleteSession(int intUserSessionID,int anUserId,Date sessionDate);

    /**
     * updates an existing session
     * @param intUserSessionID id of workout session
     * @param sessionDate date of the session
     * @param strComment comment to the session
     * @param intIDUser id of the user
     * @param rating session rating
     */
    void                        updateSession(int intUserSessionID,Date sessionDate,String strComment,int intIDUser,int rating);

    /**
     * creates a new user workout session
     * @param sessionDate date of the session
     * @param strComment comment to this session
     * @param intIDUser id of the owner
     * @param idWorkoutS id of workout
     * @param rating
     * @return returns sessionid
     */
    int                         createSession(Date sessionDate,String strComment,int intIDUser,int idWorkoutS,int rating);

    /**
     * loads sessions of user workout
     * @param myUserID user id owner of the session
     * @param workoutID id of workout
     * @return list of all sessions
     */
    ArrayList<UserWorkoutSession>   loadAllSessionsForUserWorkout(int myUserID,int workoutID);
}
