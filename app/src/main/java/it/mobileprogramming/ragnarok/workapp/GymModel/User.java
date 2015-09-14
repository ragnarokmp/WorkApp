package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class User {
    private int intUserID;
    private String strAvatar;
    private String strName;
    private String strEmail;
    private int    intGender;
    private Date   dateRegistration;
    private ArrayList<WeightItem>   weightHistory;
    private ArrayList<UserWorkout>      workoutHistory;
    private UserSerializer  usSerializer;

    /**
     * constructor that saves a new User instance on DB, don't use for loading an User from database
     * @param name name
     * @param email email
     * @param gender gender 0 male 1 female
     * @param regDate date registration
     * @param avatar encoded avatar
     * @param aSerializer serializer to save data
     */
    public User(String name,String email,int gender,Date regDate,String avatar,UserSerializer aSerializer){
        this.strName        =   name;
        this.strEmail     =   email;
        this.intGender      =   gender;
        this.dateRegistration      =   regDate;
        this.strAvatar      =   avatar;
        this.usSerializer   =   aSerializer;
        this.weightHistory  =   new ArrayList<>();
        this.workoutHistory =   new ArrayList<>();
        this.intUserID  =   aSerializer.createNewUser(name,email,this.strAvatar,gender,regDate);
    }

    /**
     * constructor used to load an User instance from Database
     * @param uId user id
     * @param name user name
     * @param email user email
     * @param gender user gender 0 male 1 female
     * @param regDate user registration date
     * @param avatar encoded avatar
     * @param aSerializer serializer
     * @param workouts arraylist of workouts
     * @param weightStory arraylist of weight history
     */
    public User(int uId, String name,String email,int gender,Date regDate,String avatar,UserSerializer aSerializer,ArrayList workouts,ArrayList<WeightItem> weightStory){
        this.strName        =   name;
        this.strEmail       =   email;
        this.intGender      =   gender;
        this.dateRegistration   =   regDate;
        this.usSerializer   =   aSerializer;
        this.weightHistory  =   weightStory;
        this.workoutHistory =   workouts;
        this.strAvatar      =   avatar;
        this.intUserID      =   uId;
    }

    /**
     * returns user registration date
     * @return date registration
     */
    public Date getDateRegistration() {
        return (Date) dateRegistration.clone();
    }

    /**
     * returns the user gender 0 male 1 female
     * @return user gender
     */
    public int getIntGender() {
        return intGender;
    }

    /**
     * returns the user ID
     * @return int userID
     */
    public int getIntUserID() {
        return intUserID;
    }

    /**
     * returns user name
     * @return String name
     */
    public String getStrName() {
        return strName+"";
    }

    /**
     * returns the encoded avatar
     * @return String encoded avatar
     */
    public String getStrAvatar() {
        return strAvatar;
    }

    /**
     * returns user surname
     * @return String surname
     */
    public String getStrEmail() {
        return strEmail+"";
    }

    /**
     * returns user weight log
     * @return arraylist items
     */
    public ArrayList<WeightItem> getWeightHistory() {
        ArrayList<WeightItem> returnlist    =   new ArrayList<>();
        for(int i=0;i<this.weightHistory.size();i++){
            returnlist.add(this.weightHistory.get(i));
        }
        return returnlist;
    }

    /**
     * returns all the user workouts
     * @return
     */
    public ArrayList<Workout> getWorkoutHistory() {
        ArrayList<Workout> templist =   new ArrayList<Workout>();
        for(int i=0;i<this.workoutHistory.size();i++){
            templist.add(this.workoutHistory.get(i));
        }
        return templist;
    }

    /**
     * add an item to user history, if saveToDb is enabled saved the entry on DB (this
     * parameter is set to false only if loading instance from DB)
     * @param anItem item to be saved
     * @param saveToDb save or not to DB
     */
    public void addToWeightHistory(WeightItem anItem, boolean saveToDb){
        this.weightHistory.add(anItem);
        if(saveToDb==true){
            this.usSerializer.addToWeightHistory(this.intUserID,anItem);
        }
    }

    /**
     * remove an item from weight history and delete the entry from DB
     * @param anItem item to be removed
     */
    public void removeFromWeightHistory(WeightItem anItem){
        this.usSerializer.removeFromWeightHistory(this.intUserID, anItem.date);
        this.weightHistory.remove(anItem);
    }

    /**
     * add a workout to user history, if saveToDb is enabled saved the entry on DB (this
     * parameter is set to false only if loading instance from DB)
     * @param anItem item to be added
     * @param saveToDb set true if save to DB
     */
    public void addToWorkouts(UserWorkout anItem, boolean saveToDb){
        this.workoutHistory.add(anItem);
        System.out.println("aggiungo ad history");
        if(saveToDb==true){
            //System.out.println("vedo se salvare "+saveToDb+" true");
            this.usSerializer.addWorkoutForUser(this.intUserID, anItem.getIntWOID());
        }
    }

    /**
     * remove an item from workout history and delete the entry from DB
     * @param anItem item to be removed
     */
    public void removeFromWorkouts(Workout anItem){
        this.usSerializer.removeWorkoutForUser(this.intUserID, anItem.getIntWOID());
        this.weightHistory.remove(anItem);
    }

    /**
     * load an User from database
     * @param serializer serializer to save other stuffs
     * @param intID user id
     * @return loaded User instance
     */
    public static User loadUserfromDatabase(SQLiteSerializer serializer,int intID){
        User myUser                                 =   serializer.loadUser(intID);
        return myUser;
    }

    /**
     * load all the users from DB
     * @param serializer serializer to save other stuffs
     * @return arraylist with all users inside
     */
    public static ArrayList<User> loadAllUsersFromDatabase(SQLiteSerializer serializer){
        return serializer.getAllUsers();
    }

    /**
     * returns all the dates where the user has a UserWorkoutSession
     * @return dates list
     */
    public ArrayList<Calendar> allSessionsDates(){

        ArrayList<Calendar> returnlist = new ArrayList<Calendar>();                         //returnlist
        ArrayList<UserWorkoutSession> mysessions  = new ArrayList<UserWorkoutSession>();    //all sessions in all workouts made by user
        for(int i=0;i<this.workoutHistory.size();i++){                                          //getting all workouts
            for(int j=0;j<this.workoutHistory.get(i).getWoSessions().size();j++){               //getting all workout's sessions
                mysessions.add(this.workoutHistory.get(i).getWoSessions().get(j));
            }
        }

        for (int i=0;i<mysessions.size();i++){

            Calendar sessionDate = Calendar.getInstance();
            sessionDate.setTime(mysessions.get(i).getDateSessionDate());

            boolean insert  =   true;
            for(int j=0;j<returnlist.size();j++){
                if(returnlist.get(j).equals(sessionDate))
                    insert  =   false;
            }
            if(insert){
                returnlist.add(sessionDate);
            }
        }
        return returnlist;
    }

    @Override
    public String toString() {
        return "User{" +
                "dateRegistration=" + dateRegistration +
                ", intUserID=" + intUserID +
                ", avatar='" + strAvatar + '\'' +
                ", strName='" + strName + '\'' +
                ", strSurname='" + strEmail + '\'' +
                ", intGender=" + intGender +
                ", weightHistory=" + weightHistory +
                ", workoutHistory=" + workoutHistory +
                ", usSerializer=" + usSerializer +
                '}';
    }
}
