package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;

public class WorkoutSession {
    private int id;
    private int progressive;
    private String filepath;
    private WorkoutSessionSerializer workoutSessionSerializer;
    private ArrayList<Exercise> exercisesOfSession  =   new ArrayList<Exercise>();

    /**
     * use this constructor to create a new workout session and save it to DB
     * @param filepath
     * @param progressive
     * @param workoutSessionSerializer
     */
    public WorkoutSession(String filepath, int progressive, WorkoutSessionSerializer workoutSessionSerializer) {
        this.filepath = filepath;
        this.progressive = progressive;
        this.workoutSessionSerializer = workoutSessionSerializer;
        this.id =   workoutSessionSerializer.createNewWorkoutSession(progressive,filepath);
    }

    /**
     * use this constructor only for loading from database
     * @param filepath
     * @param id
     * @param progressive
     * @param workoutSessionSerializer
     */
    public WorkoutSession(String filepath, int id, int progressive, WorkoutSessionSerializer workoutSessionSerializer) {
        this.filepath = filepath;
        this.id = id;
        this.progressive = progressive;
        this.workoutSessionSerializer = workoutSessionSerializer;
    }

    @Override
    public String toString() {
        return "WorkoutSession{" +
                "exercisesOfSession=" + exercisesOfSession +
                ", id=" + id +
                ", progressive=" + progressive +
                ", filepath='" + filepath + '\'' +
                ", workoutSessionSerializer=" + workoutSessionSerializer +
                '}';
    }
//TODO create user workout session from this session
}
