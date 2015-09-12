package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

public class Workout {
    private int intWOID;
    private String name;
    private String type;
    private String difficulty;
    private WorkoutSerializer           woSerializer;
    private ArrayList<WorkoutSession>   woSessions = new ArrayList<WorkoutSession>();

    /**
     * default constructor, saves new entry on DB
     * @param name workout name
     * @param type workout type
     * @param difficulty workout difficulty
     * @param aserializer instance serializing
     */
    public Workout(String name,String type,String difficulty,WorkoutSerializer aserializer){
        this.name           =   name;
        this.type           =   type;
        this.difficulty     =   difficulty;
        this.woSerializer   =   aserializer;
        this.intWOID        =   aserializer.createNewWorkout(name,type,difficulty);
    }

    /**
     * constructor for instance loaded from DB
     * @param id workout ID
     * @param name workout name
     * @param type workout type
     * @param difficulty workout difficulty
     * @param aserializer instance saving this element
     */
    public Workout(int id, String name,String type,String difficulty,WorkoutSerializer aserializer){
        this.name           =   name;
        this.type           =   type;
        this.difficulty     =   difficulty;
        this.woSerializer   =   aserializer;
        this.intWOID        =   id;
    }

    /**
     * adds workout session to workout
     * @param aSession session to be added
     * @param saveonDB boolean to be checked if want to save to DB
     * @param progressive progressive order of workout session
     */
    public void addWorkoutSession(WorkoutSession aSession,boolean saveonDB,int progressive){
        if(progressive>woSessions.size()) {
            progressive =   woSessions.size();
        }
        this.woSessions.add(progressive,aSession);
        if(saveonDB==true){
            this.woSerializer.addSessiontoWorkout(this.intWOID,aSession.getId(),progressive);
        }
    }

    /**
     * gets sessions of workout
     * @return arraylist of sessions
     */
    public ArrayList<WorkoutSession> getWorkoutSessions(){
        ArrayList<WorkoutSession> returnList    =   new ArrayList<>();
        for(int i=0;i<this.woSessions.size();i++){
            returnList.add(this.woSessions.get(i).clone());
        }
        return returnList;
    }

    /**
     * getter workout session id
     * @return id
     */
    public int getIntWOID() {
        return intWOID;
    }

    /**
     * getter workout name
     * @return workout name
     */
    public String getName() {
        return name+"";
    }

    /**
     * getter workout difficulty
     * @return difficulty
     */
    public String getDifficulty() {
        return difficulty+"";
    }

    /**
     * getter of workout type
     * @return
     */
    public String getType() {
        return type+"";
    }

    /**
     * generates an user workout from this workout
     * @param anUser user subject
     * @return userworkout
     */
    public UserWorkout createFromThisWorkout(User anUser){
        SQLiteSerializer aSerializer    =   (SQLiteSerializer)this.woSerializer;
        UserWorkout res =   new UserWorkout(this.intWOID,this.name,anUser.getIntUserID(),this.getType(),this.difficulty,this.woSerializer);
        for(int i=0;i<this.woSessions.size();i++){
            Date aDate  =   Singletons.randomDate();//TODO ok for testing purpose, replace with new Date() in case of deploy
            WorkoutSession  aSession                =   woSessions.get(i);
            UserWorkoutSession userWorkoutSession   =   new UserWorkoutSession(aSession.getFilepath(),anUser.getIntUserID(),aSerializer,aSerializer,aDate,aSession.getId(),"",false,0);
            ArrayList<Exercise> exercises           =   aSession.exercisesOfSession;
            for(int j=0;j<exercises.size();j++){
                UserExercise    userExercise        =   new UserExercise(anUser.getIntUserID(),aDate,exercises.get(j),false,"",aSerializer,aSerializer,0);
                userWorkoutSession.addExerciseToWorkoutSession(userExercise,0,false);
            }
            res.addWorkoutSession(userWorkoutSession,true,i);
        }
        UserSerializer userSerializer   =   aSerializer;
        userSerializer.addWorkoutForUser(anUser.getIntUserID(),this.getIntWOID());
        return res;
    }
    @Override
    public String toString() {
        return "Workout{" +
                "difficulty='" + difficulty + '\'' +
                ", intWOID=" + intWOID +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", woSerializer=" + woSerializer +
                ", woSessions=" + woSessions +
                '}';

    }
}
