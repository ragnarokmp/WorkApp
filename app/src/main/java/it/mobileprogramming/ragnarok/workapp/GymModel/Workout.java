package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;

public class Workout {
    private int intWOID;
    private String name;
    private String type;
    private String difficulty;
    private WorkoutSerializer           woSerializer;
    private ArrayList<WorkoutSession>   woSessions = new ArrayList<WorkoutSession>();

    public Workout(String name,String type,String difficulty,WorkoutSerializer aserializer){
        this.name           =   name;
        this.type           =   type;
        this.difficulty     =   difficulty;
        this.woSerializer   =   aserializer;
        this.intWOID        =   aserializer.createNewWorkout(name,type,difficulty);
    }

    public Workout(int id, String name,String type,String difficulty,WorkoutSerializer aserializer){
        this.name           =   name;
        this.type           =   type;
        this.difficulty     =   difficulty;
        this.woSerializer   =   aserializer;
        this.intWOID        =   id;
    }

    public void addWorkoutSession(WorkoutSession aSession,boolean saveonDB){
        this.woSessions.add(aSession);
        if(saveonDB==true){
            this.woSerializer.addSessiontoWorkout(this.intWOID,aSession.getId());
        }
    }

    public ArrayList<WorkoutSession> getWorkoutSessions(){
        ArrayList<WorkoutSession> returnList    =   new ArrayList<>();
        for(int i=0;i<this.woSessions.size();i++){
            returnList.add(this.woSessions.get(i).clone());
        }
        return returnList;
    }
    public int getIntWOID() {
        return intWOID;
    }

    public String getName() {
        return name+"";
    }

    public String getDifficulty() {
        return difficulty+"";
    }

    public String getType() {
        return type+"";
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
