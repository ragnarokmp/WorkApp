package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

/**
 * Class modeling an instance of user exercise
 *
 */
public class UserExcercise extends Exercise{
    private boolean done;
    private String comment;
    private UserExerciseSerializer  userExerciseSerializer;
    private Date exerciseDate;
    private int myUserID;

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
    public UserExcercise(int anUser, Date exDate, Exercise anExercise, boolean done, String aComment, UserExerciseSerializer userExerciseSerializer,ExerciseSerializer exerciseSerializer) {
        super(anExercise.getId(),exerciseSerializer, anExercise.getFrequency(), anExercise.getName(), anExercise.getRecovery(), anExercise.getRepetition(), anExercise.getSeries(), anExercise.getUsedWeight(),anExercise.getMuscles());
        this.comment                =   aComment;
        this.done                   =   done;
        this.userExerciseSerializer =   userExerciseSerializer;
        this.exerciseDate           =   exDate;
        this.myUserID               =   anUser;
        this.userExerciseSerializer.createUserExercise(anExercise.getId(),done,aComment,myUserID,exDate);
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
     */
    public UserExcercise(int eid,Date exerciseDate,int anUser,ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, boolean completed, String comment,String muscles,UserExerciseSerializer userExerciseSerializer) {
        super(eid, aSerializer, frequency, name, recovery, repetition, series, usedWeight, muscles);
        this.comment                =   comment;
        this.done                   =   completed;
        this.myUserID               =   anUser;
        this.exerciseDate           =   exerciseDate;
        this.userExerciseSerializer =   userExerciseSerializer;
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
        userExerciseSerializer.updateUserExercise(this.myUserID, this.getId(), this.exerciseDate, this.done, this.comment);
    }

    /**
     * setter method
     * @param done new state
     */
    public void setDone(boolean done) {
        this.done = done;
        userExerciseSerializer.updateUserExercise(this.myUserID,this.getId(),this.exerciseDate,this.done,this.comment);
    }


    @Override
    public String toString() {
        return "UserExcercise{" +
                "comment='" + comment + '\'' +
                ", done=" + done +
                '}';
    }
}
