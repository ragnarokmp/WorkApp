package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 *
 */
public class UserWorkoutSession extends WorkoutSession implements Parcelable,Commentable {
    private String  strComment="";
    private Date    dateSessionDate;
    private int     usrSessionUserId;
    private int     rating=0;
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
    public UserWorkoutSession(String filepath, int userID, WorkoutSessionSerializer workoutSessionSerializer, UserWorkoutSessionSerializer userWorkoutSessionSerializer, Date dateSessionDate, int sessionID, String strComment, boolean loadedFromDB,int rating) {
        super(filepath, sessionID, workoutSessionSerializer);
        this.dateSessionDate = dateSessionDate;
        this.strComment     = strComment;
        this.usrSessionUserId =   userID;
        this.workoutSessionSerializer       =   workoutSessionSerializer;
        this.userWorkoutSessionSerializer   =   userWorkoutSessionSerializer;
        this.rating =   rating;
        if(loadedFromDB == false) {
            userWorkoutSessionSerializer.createSession(dateSessionDate, strComment, this.usrSessionUserId, sessionID,rating);
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
                ", usrSessionUserId=" + usrSessionUserId +
                ", rating=" + rating +
                ", workoutSessionSerializer=" + workoutSessionSerializer +
                ", userWorkoutSessionSerializer=" + userWorkoutSessionSerializer +
                super.toString()+'}';
    }

    /**
     * clone function
     * @return a clone
     */
    public UserWorkoutSession clone(){
        UserWorkoutSession workoutSession   =   new UserWorkoutSession(this.getFilepath(),this.usrSessionUserId,this.workoutSessionSerializer,this.userWorkoutSessionSerializer,this.dateSessionDate,this.getId(),this.strComment,true,rating);
        for(int i=0;i<this.exercisesOfSession.size();i++){
            //TODO implement clone UserExercise/Exercise
            workoutSession.addExerciseToWorkoutSession(this.exercisesOfSession.get(i),i,false);
        }
        return workoutSession;
    }

    /**checks if all the exercises of the session are done
     *
     * @return true if all the exercises of this session are completed, false otherwise
     * @throws Exception if there is a wrong exercise type
     */
    public float allExerciseDone() throws Exception {
        boolean result = true;
        float exDone = 0;
        for(int i=0;i < this.exercisesOfSession.size();i++){
            if(this.exercisesOfSession.get(i) instanceof UserExercise){
                UserExercise anEx  =   (UserExercise)this.exercisesOfSession.get(i);
                if(anEx.isDone() == true){
                    exDone += 1;
                }
            }
            else throw new Exception("UserWorkoutSession.java Exercise wrong type exception");
        }
        return exDone/this.exercisesOfSession.size();
    }

    /**
     * return the rating given to this workout session
     * @return rating
     */
    public int getRating() {
        return rating;
    }

    @Override
    public void setComment(String comment) {
        this.strComment =   comment;
        this.userWorkoutSessionSerializer.updateSession(this.getId(),this.dateSessionDate,comment,this.getUserId(),this.rating);

    }

    @Override
    public void setSerializer(Object serializer) {
        this.userWorkoutSessionSerializer   =   (UserWorkoutSessionSerializer)serializer;
    }

    @Override
    public void setComment(String comment, int rating) {
        this.strComment =   comment;
        this.rating     =   rating;
        this.userWorkoutSessionSerializer.updateSession(this.getId(),this.dateSessionDate,comment,this.getUserId(),rating);
    }

    /**
     * set the session rating
     * @param rating
     */
    public void setRating(int rating) {
        this.rating = rating;
        this.userWorkoutSessionSerializer.updateSession(this.getId(),this.dateSessionDate,this.strComment,this.getUserId(),rating);
    }

    @Override
    public String getComment() {
        return this.strComment+"";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.strComment);
        dest.writeLong(dateSessionDate != null ? dateSessionDate.getTime() : -1);
        dest.writeInt(this.usrSessionUserId);
        dest.writeInt(this.rating);
    }

    protected UserWorkoutSession(Parcel in) {
        super(in);
        this.strComment = in.readString();
        long tmpDateSessionDate = in.readLong();
        this.dateSessionDate = tmpDateSessionDate == -1 ? null : new Date(tmpDateSessionDate);
        this.usrSessionUserId = in.readInt();
        this.rating = in.readInt();
    }

    public static final Creator<UserWorkoutSession> CREATOR = new Creator<UserWorkoutSession>() {
        public UserWorkoutSession createFromParcel(Parcel source) {
            return new UserWorkoutSession(source);
        }

        public UserWorkoutSession[] newArray(int size) {
            return new UserWorkoutSession[size];
        }
    };
}
