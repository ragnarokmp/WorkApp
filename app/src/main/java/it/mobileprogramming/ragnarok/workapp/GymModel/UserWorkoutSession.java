package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

/**
 *
 */
public class UserWorkoutSession extends WorkoutSession{
    private String  strComment;
    private Date    dateSessionDate;
    private int     usrSessionUserId;
    private int     sessionID;
    private WorkoutSessionSerializer        workoutSessionSerializer;
    private UserWorkoutSessionSerializer    userWorkoutSessionSerializer;

    /**
     *
     * if loadedFrom db is set to false creates a new entry in DB
     * @param filepath
     * @param progressive
     * @param workoutSessionSerializer
     * @param userWorkoutSessionSerializer
     * @param dateSessionDate
     * @param sessionID
     * @param strComment
     * @param loadedFromDB
     */
    public UserWorkoutSession(String filepath, int userID, int progressive, WorkoutSessionSerializer workoutSessionSerializer, UserWorkoutSessionSerializer userWorkoutSessionSerializer, Date dateSessionDate, int sessionID, String strComment, boolean loadedFromDB) {
        super(filepath, sessionID, progressive, workoutSessionSerializer);
        this.dateSessionDate = dateSessionDate;
        this.sessionID      = sessionID;
        this.strComment     = strComment;
        this.usrSessionUserId =   userID;
        this.workoutSessionSerializer       =   workoutSessionSerializer;
        this.userWorkoutSessionSerializer   =   userWorkoutSessionSerializer;
        if(loadedFromDB == false) {
            userWorkoutSessionSerializer.createSession(dateSessionDate, strComment, this.usrSessionUserId, sessionID);
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


    @Override
    public String toString() {
        return "UserWorkoutSession{" +
                "dateSessionDate=" + dateSessionDate +
                ", strComment='" + strComment + '\'' +
                ", usrSessionUser=" + usrSessionUserId +
                ", sessionID=" + sessionID +
                '}';
    }

    public UserWorkoutSession clone(){
        return new UserWorkoutSession(this.getFilepath(),this.usrSessionUserId,this.getProgressive(),this.workoutSessionSerializer,this.userWorkoutSessionSerializer,this.dateSessionDate,this.sessionID,this.strComment,true);
    }
}
