package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paride on 18/06/15.
 */
public interface UserWorkoutSessionSerializer {
    UserWorkoutSession          loadSession(int intUserSessionID,User anUser);
    void                        deleteSession(int intUserSessionID,User anUser);
    void                        updateSession(int intUserSessionID,Date sessionDate,String strComment,int intIDUser,int idWorkout);
    int                         createSession(Date sessionDate,String strComment,int intIDUser,int idWorkoutS);
    ArrayList<UserWorkoutSession>   loadAllSessionsForUserWorkout(int myUserID,int workoutID);
}
