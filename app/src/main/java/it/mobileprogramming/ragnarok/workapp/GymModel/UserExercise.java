package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Class modeling an instance of user exercise
 *
 */
public class UserExercise extends Exercise implements Commentable,Parcelable{
    private boolean done;
    private String comment="";
    private UserExerciseSerializer  userExerciseSerializer;
    private Date exerciseDate;
    private int myUserID;
    private int rating=0;

    /**
     * default constructor, saves the entry on DB
     * @param anUser user owner of the exercise
     * @param exDate date of execution
     * @param anExercise exercise to be executed
     * @param done state
     * @param aComment comment about exercise
     * @param userExerciseSerializer instance that saves this exercise
     * @param exerciseSerializer instance that saves the original exercise
     */
    public UserExercise(int anUser, Date exDate, Exercise anExercise, boolean done, String aComment, UserExerciseSerializer userExerciseSerializer, ExerciseSerializer exerciseSerializer, int rating) {
        super(anExercise.getId(),exerciseSerializer, anExercise.getFrequency(), anExercise.getName(), anExercise.getRecovery(), anExercise.getRepetition(), anExercise.getSeries(), anExercise.getUsedWeight(),anExercise.getMuscles());
        this.comment                =   aComment;
        this.done                   =   done;
        this.userExerciseSerializer =   userExerciseSerializer;
        this.exerciseDate           =   exDate;
        this.myUserID               =   anUser;
        this.rating                 =   rating;
        this.userExerciseSerializer.createUserExercise(anExercise.getId(), done, aComment, myUserID, exDate, rating);
    }

    /**
     * constructor used to load instances from DB
     * @param eid exercise ID
     * @param exerciseDate execution date
     * @param anUser user owner
     * @param aSerializer object that saves on DB
     * @param frequency exercise frequency
     * @param name exercise name
     * @param recovery recovery time
     * @param repetition number of repetitions
     * @param series number of series
     * @param usedWeight weights used
     * @param completed exercise state
     * @param comment comment to this exercise
     * @param muscles muscles involved
     * @param userExerciseSerializer instance that saves state
     * @param rating exercise rating
     */
    public UserExercise(int eid, Date exerciseDate, int anUser, ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, boolean completed, String comment, String muscles, UserExerciseSerializer userExerciseSerializer, int rating) {
        super(eid, aSerializer, frequency, name, recovery, repetition, series, usedWeight, muscles);
        this.comment                =   comment;
        this.done                   =   completed;
        this.myUserID               =   anUser;
        this.exerciseDate           =   exerciseDate;
        this.userExerciseSerializer =   userExerciseSerializer;
        this.rating                 =   rating;
    }

    /**
     * default constructor used for parcelable
     */
    public UserExercise(){
        super();
    }
    /**
     * getter method
     * @return exercise date
     */
    public Date getExerciseDate() {
        return exerciseDate;
    }

    /**
     * getter method
     * @return owner
     */
    public int getMyUserID() {
        return myUserID;
    }

    /**
     * getter method
     * @return exercise comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * exercise state
     * @return state
     */
    public boolean isDone() {
        return done;
    }

    /**
     * setter method
     * @param comment new exercise comment
     */
    public void setComment(String comment) {
        this.comment = comment;
        System.out.println(this.myUserID+" "+this.getId()+" "+this.exerciseDate+" "+this.done+" "+this.comment);
        userExerciseSerializer.updateUserExercise(this.myUserID, this.getId(), this.exerciseDate, this.done, this.comment,this.rating);
    }

    /**
     * setter method necessary to avoid tons of code with non parcelable classes
     * @param userExerciseSerializer
     */
    public void setSerializer(Object userExerciseSerializer) {
        this.userExerciseSerializer = (UserExerciseSerializer) userExerciseSerializer;
    }

    @Override
    public void setComment(String comment, int rating) {
        this.comment    =   comment;
        this.rating     =   rating;
        userExerciseSerializer.updateUserExercise(this.myUserID, this.getId(), this.exerciseDate, this.done, this.comment,this.rating);

    }

    /**
     * setter method
     * @param done new state
     */
    public void setDone(boolean done) {
        this.done = done;
        userExerciseSerializer.updateUserExercise(this.myUserID,this.getId(),this.exerciseDate,this.done,this.comment,this.rating);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        this.userExerciseSerializer.updateUserExercise(this.myUserID,this.getId(),this.exerciseDate,this.done,this.comment,this.rating);
    }

    @Override
    public String toString() {
        return "UserExercise{" +
                "comment='" + comment + '\'' +
                ", done=" + done +
                ", userExerciseSerializer=" + userExerciseSerializer +
                ", exerciseDate=" + exerciseDate +
                ", myUserID=" + myUserID +
                ", rating=" + rating +
                '}'+super.toString();
    }

    @Override
    public Exercise clone() {
        return new UserExercise(this.intExId,this.exerciseDate,this.myUserID,this.aSerializer,this.frequency,this.name,this.recovery,this.repetition,this.series,this.usedWeight,this.done,this.comment,this.muscles,this.userExerciseSerializer,this.rating);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(done ? (byte) 1 : (byte) 0);
        dest.writeString(this.comment);
        dest.writeLong(exerciseDate != null ? exerciseDate.getTime() : -1);
        dest.writeInt(this.myUserID);
        dest.writeInt(this.rating);
    }

    protected UserExercise(Parcel in) {
        super(in);
        this.done = in.readByte() != 0;
        this.comment = in.readString();
        long tmpExerciseDate = in.readLong();
        this.exerciseDate = tmpExerciseDate == -1 ? null : new Date(tmpExerciseDate);
        this.myUserID = in.readInt();
        this.rating = in.readInt();
    }

    public static final Creator<UserExercise> CREATOR = new Creator<UserExercise>() {
        public UserExercise createFromParcel(Parcel source) {
            return new UserExercise(source);
        }

        public UserExercise[] newArray(int size) {
            return new UserExercise[size];
        }
    };
}
