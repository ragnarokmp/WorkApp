package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

/**
 *
 */
public class UserWorkoutSession extends WorkoutSession{
    private String  strComment;
    private Date    dateSessionDate;
    private int     usrSessionUserId;
    private WorkoutSessionSerializer        workoutSessionSerializer;
    private UserWorkoutSessionSerializer    userWorkoutSessionSerializer;

    /**
     *
     * if loadedFrom db is set to false creates a new entry in DB
     * @param filepath
     * @param workoutSessionSerializer
     * @param userWorkoutSessionSerializer
     * @param dateSessionDate
     * @param sessionID
     * @param strComment
     * @param loadedFromDB
     */
    public UserWorkoutSession(String filepath, int userID, WorkoutSessionSerializer workoutSessionSerializer, UserWorkoutSessionSerializer userWorkoutSessionSerializer, Date dateSessionDate, int sessionID, String strComment, boolean loadedFromDB) {
        super(filepath, sessionID, workoutSessionSerializer);
        this.dateSessionDate = dateSessionDate;
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


    public String getStrComment() {
        return strComment;
    }

    public int getUserId() {
        return usrSessionUserId;
    }
    @Override
    public String toString() {
        return "UserWorkoutSession{" +
                "dateSessionDate=" + dateSessionDate +
                ", strComment='" + strComment + '\'' +
                ", usrSessionUser=" + usrSessionUserId +
                '}'+super.toString();
    }

    public UserWorkoutSession clone(){
        return new UserWorkoutSession(this.getFilepath(),this.usrSessionUserId,this.workoutSessionSerializer,this.userWorkoutSessionSerializer,this.dateSessionDate,this.getId(),this.strComment,true);
    }
}
