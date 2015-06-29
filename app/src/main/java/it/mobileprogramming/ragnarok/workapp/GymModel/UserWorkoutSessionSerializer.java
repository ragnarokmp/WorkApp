package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paride on 18/06/15.
 */
public interface UserWorkoutSessionSerializer {
    UserWorkoutSession          loadSession(int intUserSessionID,User anUser,Date sessionDate);
    void                        deleteSession(int intUserSessionID,int anUserId,Date sessionDate);
    void                        updateSession(int intUserSessionID,Date sessionDate,String strComment,int intIDUser);
    int                         createSession(Date sessionDate,String strComment,int intIDUser,int idWorkoutS);
    ArrayList<UserWorkoutSession>   loadAllSessionsForUserWorkout(int myUserID,int workoutID);
}
