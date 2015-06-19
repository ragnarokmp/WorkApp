package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

/**
 *
 */
public class UserWorkoutSession extends WorkoutSession{
    private WorkoutSession wsaSession;
    private String  strComment;
    private Date    dateSessionDate;
    private User    usrSessionUser;
    private int     sessionID;


    /**
     * this constructor is used if loading from DB
     * @param filepath
     * @param id
     * @param progressive
     * @param workoutSessionSerializer
     * @param dateSessionDate
     * @param sessionID
     * @param strComment
     * @param usrSessionUser
     * @param wsaSession
     */
    public UserWorkoutSession(String filepath, int id, int progressive, WorkoutSessionSerializer workoutSessionSerializer, Date dateSessionDate, int sessionID, String strComment, User usrSessionUser, WorkoutSession wsaSession) {
        super(filepath, id, progressive, workoutSessionSerializer);
        this.dateSessionDate = dateSessionDate;
        this.sessionID = sessionID;
        this.strComment = strComment;
        this.usrSessionUser = usrSessionUser;
        this.wsaSession = wsaSession;
    }

    public Date getDateSessionDate() {
        return dateSessionDate;
    }

    public int getSessionID() {
        return sessionID;
    }

    public String getStrComment() {
        return strComment;
    }

    public User getUsrSessionUser() {
        return usrSessionUser;
    }

    public WorkoutSession getWsaSession() {
        return wsaSession;
    }

}
