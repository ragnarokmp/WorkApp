package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

/**
 * Created by paride on 18/06/15.
 */
public class UserExcercise extends Exercise{
    private boolean done;
    private String comment;
    private UserExerciseSerializer  userExerciseSerializer;
    private Date exerciseDate;
    private int myUserID;

    public UserExcercise(int anUser, Date exDate, Exercise anExercise, boolean done, String aComment, UserExerciseSerializer userExerciseSerializer,ExerciseSerializer exerciseSerializer) {
        super(anExercise.getId(),exerciseSerializer, anExercise.getFrequency(), anExercise.getName(), anExercise.getRecovery(), anExercise.getRepetition(), anExercise.getSeries(), anExercise.getUsedWeight(),anExercise.getMuscles());
        this.comment                =   aComment;
        this.done                   =   done;
        this.userExerciseSerializer =   userExerciseSerializer;
        this.exerciseDate           =   exDate;
        this.myUserID               =   anUser;
        this.userExerciseSerializer.createUserExercise(anExercise.getId(),done,aComment,myUserID,exDate);
    }

    public UserExcercise(int eid,Date exerciseDate,int anUser,ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, boolean completed, String comment,String muscles,UserExerciseSerializer userExerciseSerializer) {
        super(eid, aSerializer, frequency, name, recovery, repetition, series, usedWeight, muscles);
        this.comment                =   comment;
        this.done                   =   completed;
        this.myUserID               =   anUser;
        this.exerciseDate           =   exerciseDate;
        this.userExerciseSerializer =   userExerciseSerializer;
    }

    public Date getExerciseDate() {
        return exerciseDate;
    }

    public int getMyUserID() {
        return myUserID;
    }

    public String getComment() {
        return comment;
    }

    public boolean isDone() {
        return done;
    }

    public void setComment(String comment) {
        this.comment = comment;
        System.out.println(this.myUserID+" "+this.getId()+" "+this.exerciseDate+" "+this.done+" "+this.comment);
        userExerciseSerializer.updateUserExercise(this.myUserID, this.getId(), this.exerciseDate, this.done, this.comment);
    }

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
