package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private int intUserID;
    private String strPwdHash;
    private String strName;
    private String strSurname;
    private int    intGender;
    private Date dateBirth;
    private ArrayList<WeightItem>   weightHistory;
    private ArrayList<UserWorkout>      workoutHistory;
    private UserSerializer  usSerializer;

    public User(String name,String surname,int sex,Date birthDate,String password,UserSerializer aSerializer){
        this.strName        =   name;
        this.strSurname     =   surname;
        this.intGender         =   sex;
        this.dateBirth      =   birthDate;
        this.strPwdHash     =   hashAndSalt(password);
        this.usSerializer   =   aSerializer;
        this.weightHistory  =   new ArrayList<WeightItem>();
        this.workoutHistory =   new ArrayList<UserWorkout>();
        this.intUserID  =   aSerializer.createNewUser(name,surname,this.strPwdHash,sex,birthDate);
    }

    //TODO hash and salt password
    private String hashAndSalt(String password) {
        return "example";
    }

    public User(int uId, String name,String surname,int gender,Date birthDate,String pwdHash,UserSerializer aSerializer,ArrayList workouts,ArrayList<WeightItem> weightStory){
        this.strName        =   name;
        this.strSurname     =   surname;
        this.intGender      =   gender;
        this.dateBirth      =   birthDate;
        this.usSerializer   =   aSerializer;
        this.weightHistory  =   weightStory;
        this.workoutHistory =   workouts;
        this.strPwdHash     =   pwdHash;
        this.intUserID      =   uId;
    }


    public Date getDateBirth() {
        return (Date) dateBirth.clone();
    }

    public int getIntSex() {
        return intGender;
    }

    public int getIntUserID() {
        return intUserID;
    }

    public String getStrName() {
        return strName+"";
    }

    public String getStrSurname() {
        return strSurname+"";
    }

    public ArrayList<WeightItem> getWeightHistory() {
        ArrayList<WeightItem> returnlist    =   new ArrayList<WeightItem>();
        for(int i=0;i<this.weightHistory.size();i++){
            returnlist.add(this.weightHistory.get(i));
        }
        return returnlist;
    }

    public ArrayList<Workout> getWorkoutHistory() {
        ArrayList<Workout> templist =   new ArrayList<Workout>();
        for(int i=0;i<this.workoutHistory.size();i++){
            templist.add(this.workoutHistory.get(i));
        }
        return templist;
    }

    public void addToWeightHistory(WeightItem anItem, boolean saveToDb){
        this.weightHistory.add(anItem);
        if(saveToDb==true){
            this.usSerializer.addToWeightHistory(this.intUserID,anItem);
        }
    }

    public void removeFromWeightHistory(WeightItem anItem){
        this.usSerializer.removeFromWeightHistory(this.intUserID, anItem.date);
        this.weightHistory.remove(anItem);
    }

    public void addToWorkouts(UserWorkout anItem, boolean saveToDb){
        this.workoutHistory.add(anItem);
        System.out.println("aggiungo ad history");
        if(saveToDb==true){
            System.out.println("vedo se salvare "+saveToDb+" true");
            this.usSerializer.addWorkoutForUser(this.intUserID, anItem.getIntWOID());
        }
    }

    public void removeFromWorkouts(Workout anItem){
        this.usSerializer.removeWorkoutForUser(this.intUserID, anItem.getIntWOID());
        this.weightHistory.remove(anItem);
    }

    public static User loadUserfromDatabase(SQLiteSerializer serializer,int intID){
        User myUser                                 =   serializer.loadUser(intID);
        return myUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "dateBirth=" + dateBirth +
                ", intUserID=" + intUserID +
                ", strPwdHash='" + strPwdHash + '\'' +
                ", strName='" + strName + '\'' +
                ", strSurname='" + strSurname + '\'' +
                ", intGender=" + intGender +
                ", weightHistory=" + weightHistory +
                ", workoutHistory=" + workoutHistory +
                ", usSerializer=" + usSerializer +
                '}';
    }
}
