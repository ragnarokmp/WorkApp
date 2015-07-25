package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;

public class WorkoutSession {
    private int id;
    private String filepath;
    private WorkoutSessionSerializer workoutSessionSerializer;
    private ArrayList<Exercise> exercisesOfSession  =   new ArrayList<Exercise>();

    public WorkoutSession() {
    }

    /**
     * use this constructor to create a new workout session and save it to DB
     * @param filepath
     * @param workoutSessionSerializer
     */
    public WorkoutSession(String filepath, WorkoutSessionSerializer workoutSessionSerializer) {
        this.filepath = filepath;
        this.workoutSessionSerializer = workoutSessionSerializer;
        this.id =   workoutSessionSerializer.createNewWorkoutSession(filepath);
    }

    /**
     * use this constructor only for loading from database
     * @param filepath
     * @param id
     * @param workoutSessionSerializer
     */
    public WorkoutSession(String filepath, int id, WorkoutSessionSerializer workoutSessionSerializer) {
        this.filepath = filepath;
        this.id = id;
        this.workoutSessionSerializer = workoutSessionSerializer;
    }

    /**
     * adds an exercise to a workout session
     * @param e exercise to be added
     * @param position position (progressive) on exercise list
     * @param saveOnDB set true if you want to save this item to DB
     */
    public void addExerciseToWorkoutSession(Exercise e,int position,boolean saveOnDB){
        if(position>exercisesOfSession.size()){ //protection for out of index
            position    =  exercisesOfSession.size();
        }
        this.exercisesOfSession.add(position,e);
        if(saveOnDB==true){
            this.workoutSessionSerializer.addExerciseForWorkoutSession(this.id,e.getId());
        }
        System.out.println("Added exercise " + e.toString() + " session made with " + this.exercisesOfSession.size() + " exercises");
    }

    /**
     * getter id
     * @return id of workout session
     */
    public int getId() {
        return id;
    }

    /**
     * getter of exercises list of this session
     * @return arraylist with all exercises within
     */
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
                ", filepath='" + filepath + '\'' +
                ", workoutSessionSerializer=" + workoutSessionSerializer +
                '}';
    }

    public WorkoutSession clone(){
        WorkoutSession aSession =   new WorkoutSession(this.filepath,this.id,this.workoutSessionSerializer);
        for(int i=0;i<this.exercisesOfSession.size();i++){
            aSession.addExerciseToWorkoutSession(this.exercisesOfSession.get(i).clone(),i,false);
        }
        return aSession;
    }
//TODO create user workout session from this session??

    public String getFilepath() {
        return filepath;
    }


}
