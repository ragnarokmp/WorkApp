package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paride on 18/06/15.
 */
public interface UserWorkoutSessionSerializer {
    UserWorkoutSession          loadSession(int intUserSessionID);
    void                        deleteSession(int intUserSessionID);
    void                        updateSession(int intUserSessionID,Date sessionDate,String strComment,int intIDUser,int idWorkout);
    int                         createSession(Date sessionDate,String strComment,int intIDUser,int idWorkout);
    ArrayList<WorkoutSession>   loadAllSessionsForUser(int intIDUser);
}
