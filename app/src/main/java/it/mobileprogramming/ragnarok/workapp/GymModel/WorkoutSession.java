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

    public void addExerciseToWorkoutSession(Exercise e){
        this.exercisesOfSession.add(e);
        System.out.println("Added exercise " + e.toString() + " session made with " + this.exercisesOfSession.size() + " exercises");
    }

    public int getId() {
        return id;
    }

    public ArrayList<Exercise> getExercisesOfSession(){
        ArrayList<Exercise> returnList  =   new ArrayList<>();
        for(int i=0;i<this.exercisesOfSession.size();i++){
            Exercise temp   =   exercisesOfSession.get(i).clone();
            returnList.add(temp);
        }
        return returnList;
    }
    @Override
    public String toString() {
        return "WorkoutSession{" +
                "exercisesOfSession=" + exercisesOfSession.size() +
                ", id=" + id +
                ", progressive=" + progressive +
                ", filepath='" + filepath + '\'' +
                ", workoutSessionSerializer=" + workoutSessionSerializer +
                '}';
    }

    public WorkoutSession clone(){
        WorkoutSession aSession =   new WorkoutSession(this.filepath,this.id,this.progressive,this.workoutSessionSerializer);
        for(int i=0;i<this.exercisesOfSession.size();i++){
            aSession.addExerciseToWorkoutSession(this.exercisesOfSession.get(i).clone());
        }
        return aSession;
    }
//TODO create user workout session from this session

    public String getFilepath() {
        return filepath;
    }

    public int getProgressive() {
        return progressive;
    }
}
