package it.mobileprogramming.ragnarok.workapp.GymModel;

/**
 * Created by paride on 18/06/15.
 */
public class UserExcercise extends Exercise{
    private boolean done;
    private String comment;

    public UserExcercise(UserExerciseSerializer uSerializer,ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, String comment, boolean done,String muscles) {
        super(aSerializer, frequency, name, recovery, repetition, series, usedWeight,muscles);
        this.comment = comment;
        this.done = done;
        //TODO do this constructor like the others...
    }

    public UserExcercise(int eid, ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, boolean completed, String comment,String muscles) {
        super(eid, aSerializer, frequency, name, recovery, repetition, series, usedWeight, muscles);
        this.comment = comment;
        this.done = completed;
    }

    public String getComment() {
        return comment;
    }

    public boolean isDone() {
        return done;
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
                "comment='" + comment + '\'' +
                ", done=" + done +
                '}';
    }
}
