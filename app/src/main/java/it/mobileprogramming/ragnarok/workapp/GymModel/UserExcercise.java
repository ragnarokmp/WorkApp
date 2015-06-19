package it.mobileprogramming.ragnarok.workapp.GymModel;

/**
 * Created by paride on 18/06/15.
 */
public class UserExcercise extends Exercise{
    UserWorkoutSession mySession;
    boolean done;
    String comment;
    Exercise anExercise;

    public UserExcercise(ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, Exercise anExercise, String comment, boolean done, UserWorkoutSession mySession) {
        super(aSerializer, frequency, name, recovery, repetition, series, usedWeight);
        this.anExercise = anExercise;
        this.comment = comment;
        this.done = done;
        this.mySession = mySession;
    }

    public UserExcercise(int eid, ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, boolean completed, Exercise anExercise, String comment, boolean done, UserWorkoutSession mySession) {
        super(eid, aSerializer, frequency, name, recovery, repetition, series, usedWeight, completed);
        this.anExercise = anExercise;
        this.comment = comment;
        this.done = done;
        this.mySession = mySession;
    }

    public Exercise getAnExercise() {
        return anExercise;
    }

    public String getComment() {
        return comment;
    }

    public boolean isDone() {
        return done;
    }

    public UserWorkoutSession getMySession() {
        return mySession;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
