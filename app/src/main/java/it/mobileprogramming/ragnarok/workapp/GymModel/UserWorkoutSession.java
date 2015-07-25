package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 *
 */
public class UserWorkoutSession extends WorkoutSession implements Parcelable {
    private String  strComment;
    private Date    dateSessionDate;
    private int     usrSessionUserId;
    private WorkoutSessionSerializer        workoutSessionSerializer;
    private UserWorkoutSessionSerializer    userWorkoutSessionSerializer;

    /**
     *
     * if loadedFrom db is set to false creates a new entry in DB
     * @param filepath path of the photo file
     * @param workoutSessionSerializer instance serializing
     * @param userWorkoutSessionSerializer instance serializing
     * @param dateSessionDate date of the session
     * @param sessionID id of the session
     * @param strComment comment to a session
     * @param loadedFromDB if set to false saves a new entry on DB
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

    /**
     * getter of session date
     * @return sessiondate
     */
    public Date getDateSessionDate() {
        return dateSessionDate;
    }

    /**
     * getter of session comment
     * @return comment of session
     */
    public String getStrComment() {
        return strComment;
    }

    /**
     * getter of owner
     * @return id of the owner
     */
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

    /**
     * clone function
     * @return a clone
     */
    public UserWorkoutSession clone(){
        return new UserWorkoutSession(this.getFilepath(),this.usrSessionUserId,this.workoutSessionSerializer,this.userWorkoutSessionSerializer,this.dateSessionDate,this.getId(),this.strComment,true);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.strComment);
        dest.writeLong(dateSessionDate != null ? dateSessionDate.getTime() : -1);
        dest.writeInt(this.usrSessionUserId);
    }

    protected UserWorkoutSession(Parcel in) {
        this.strComment = in.readString();
        long tmpDateSessionDate = in.readLong();
        this.dateSessionDate = tmpDateSessionDate == -1 ? null : new Date(tmpDateSessionDate);
        this.usrSessionUserId = in.readInt();
    }

    public static final Parcelable.Creator<UserWorkoutSession> CREATOR = new Parcelable.Creator<UserWorkoutSession>() {
        public UserWorkoutSession createFromParcel(Parcel source) {
            return new UserWorkoutSession(source);
        }

        public UserWorkoutSession[] newArray(int size) {
            return new UserWorkoutSession[size];
        }
    };
}
