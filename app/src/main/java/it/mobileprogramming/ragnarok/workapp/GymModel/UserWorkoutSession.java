package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

/**
 *
 */
public class UserWorkoutSession extends WorkoutSession{
    private String  strComment;
    private Date    dateSessionDate;
    private User    usrSessionUser;
    private int     sessionID;


    /**
     *
     * if loadedFrom db is set to false creates a new entry in DB
     * @param filepath
     * @param user
     * @param progressive
     * @param workoutSessionSerializer
     * @param userWorkoutSessionSerializer
     * @param dateSessionDate
     * @param sessionID
     * @param strComment
     * @param loadedFromDB
     */
    public UserWorkoutSession(String filepath, User user, int progressive, WorkoutSessionSerializer workoutSessionSerializer, UserWorkoutSessionSerializer userWorkoutSessionSerializer, Date dateSessionDate, int sessionID, String strComment, boolean loadedFromDB) {
        super(filepath, sessionID, progressive, workoutSessionSerializer);
        this.dateSessionDate = dateSessionDate;
        this.sessionID      = sessionID;
        this.strComment     = strComment;
        this.usrSessionUser =   user;
        if(loadedFromDB == false) {
            userWorkoutSessionSerializer.createSession(dateSessionDate, strComment, usrSessionUser.getIntUserID(), sessionID);
        }
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


    @Override
    public String toString() {
        return "UserWorkoutSession{" +
                "dateSessionDate=" + dateSessionDate +
                ", strComment='" + strComment + '\'' +
                ", usrSessionUser=" + usrSessionUser +
                ", sessionID=" + sessionID +
                '}';
    }
}
