package it.mobileprogramming.ragnarok.workapp.GymModel;

/**
 * Created by paride on 18/06/15.
 */
public class UserExcercise extends Exercise{
    private UserWorkoutSession mySession;
    private boolean done;
    private String comment;
    private Exercise anExercise;

    public UserExcercise(UserExerciseSerializer uSerializer,ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, Exercise anExercise, String comment, boolean done, UserWorkoutSession mySession) {
        super(aSerializer, frequency, name, recovery, repetition, series, usedWeight,null);
        this.anExercise = anExercise;
        this.comment = comment;
        this.done = done;
        this.mySession = mySession;
        //TODO do this constructor like the others...
    }

    public UserExcercise(int eid, ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, boolean completed, Exercise anExercise, String comment, boolean done, UserWorkoutSession mySession) {
        super(eid, aSerializer, frequency, name, recovery, repetition, series, usedWeight, completed,null);
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

    @Override
    public String toString() {
        return "UserExcercise{" +
                "anExercise=" + anExercise +
                ", mySession=" + mySession +
                ", done=" + done +
                ", comment='" + comment + '\'' +
                '}';
    }
}
