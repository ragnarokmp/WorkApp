package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by paride on 18/06/15.
 */
public class SQLiteSerializer implements ExerciseSerializer,UserExerciseSerializer,UserSerializer,WorkoutSerializer,WorkoutSessionSerializer,UserWorkoutSessionSerializer {
    private String          strdbName;
    private GymSQLiteHelper anHelper;
    private SQLiteDatabase  sqlGymDatabase;
    private Context         appContext;
    private final static String User_USER_tablename         =   "User";
    private final static String User_USERID_column          =   "UserID";
    private final static String User_Password_column        =   "Password";
    private final static String User_Name_column            =   "Name";
    private final static String USERMAKESWORKOUT_tablename  =   "UserMakesWorkout";
    private final static String User_Surname_column         =   "Surname";
    private final static String User_Gender_column          =   "Gender";
    private final static String User_BirthDate_column       =   "Birthdate";
    public SQLiteSerializer (Context aContext,String dbName){
        this.strdbName  =   dbName;
        this.appContext =   aContext;
    }
    public void open(){
        anHelper            =   new GymSQLiteHelper(this.appContext,strdbName);
        this.sqlGymDatabase =   anHelper.getWritableDatabase();
    }

    public void close(){
        sqlGymDatabase.close();
        anHelper.close();
    }
    @Override
    public int createNewExercise(String usedWeight, int series, int repetition, int frequency, int recovery, String name) {
        return 0;
    }

    @Override
    public Exercise loadExercise(int id) {
        return null;
    }

    @Override
    public void updateExercise(int id, String usedWeight, int series, int repetition, int frequency, int recovery, String name) {

    }

    @Override
    public ArrayList<Exercise> loadAll() {
        return null;
    }

    @Override
    public int createNewUser(String strName, String strSurname, String pwd, int intSex, Date dateBirth) {
        ContentValues   values  = new ContentValues();
        values.put(this.User_Name_column,strName);
        values.put(this.User_Surname_column,strSurname);
        values.put(this.User_Gender_column,intSex);
        values.put(this.User_Password_column,pwd);
        values.put(this.User_BirthDate_column,Singletons.getStringFromDate(dateBirth));
        return  (int)this.sqlGymDatabase.insert(this.User_USER_tablename,null,values);
    }

    @Override
    public ArrayList<User> getAllUsers() {
        Cursor result   =   this.sqlGymDatabase.query(this.User_USER_tablename,null,null,null,null,null,null);
        if(result.getCount()==0){
            return null;
        }
        else{
            ArrayList<User>returnList   =   new ArrayList<User>();
            result.moveToFirst();
            for(int i=0;i<result.getCount();i++){
                int     uid         =   result.getInt(result.getColumnIndex(this.User_USERID_column));
                String  uname       =   result.getString(result.getColumnIndex(this.User_Name_column));
                String  usurname    =   result.getString(result.getColumnIndex(this.User_Surname_column));
                String  upassword   =   result.getString(result.getColumnIndex(this.User_Password_column));
                int     ugender     =   result.getInt(result.getColumnIndex(this.User_Gender_column));
                Date    ubirth      =   Singletons.formatFromString(result.getString(result.getColumnIndex(this.User_BirthDate_column)));
                User    anUser      =   new User(uid,uname,usurname,ugender,ubirth,upassword,this);
                returnList.add(anUser);
                System.out.println("User: " + anUser.toString());
                ArrayList<Workout> workoutsForUser  =   new ArrayList<Workout>();
            }
            return returnList;
        }
    }

    @Override
    public User loadUser(int id) {
        String field        =       this.User_USERID_column+"=?";
        String filter   []  =       {String.valueOf(id)};
        Cursor result       =       this.sqlGymDatabase.query(this.User_USER_tablename,null,field,filter,null,null,null);
        result.moveToFirst();
        int     uid         =   result.getInt(result.getColumnIndex(this.User_USERID_column));
        String  uname       =   result.getString(result.getColumnIndex(this.User_Name_column));
        String  usurname    =   result.getString(result.getColumnIndex(this.User_Surname_column));
        String  upassword   =   result.getString(result.getColumnIndex(this.User_Password_column));
        int     ugender     =   result.getInt(result.getColumnIndex(this.User_Gender_column));
        Date    ubirth      =   Singletons.formatFromString(result.getString(result.getColumnIndex(this.User_BirthDate_column)));
        User    anUser      =   new User(uid,uname,usurname,ugender,ubirth,upassword,this);
        return  anUser;
    }

    @Override
    public void updateUser(int id, String strName, String strSurname, int intSex, Date dateBirth) {
        ContentValues   values  = new ContentValues();
        values.put(this.User_Name_column,strName);
        values.put(this.User_Surname_column,strSurname);
        values.put(this.User_Gender_column,intSex);
        values.put(this.User_BirthDate_column,Singletons.getStringFromDate(dateBirth));
        String field        =       this.User_USERID_column+"=?";
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.update(this.User_USER_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void deleteUser(int id) {
        String field        =       this.User_USERID_column+"=?";
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.delete(this.User_USER_tablename,field,filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public ArrayList<WeightItem> loadWeightHistory(int id) {
        return null;
    }


    @Override
    public void removeFromWeightHistory(int userId, Date date) {

    }

    @Override
    public void addToWeightHistory(int intUserID, WeightItem anItem) {

    }

    @Override
    public ArrayList<Workout> loadWorkoutsForUser(int id) {
        return null;
    }

    @Override
    public void addWorkoutForUser(int userID, int WorkoutID) {

    }

    @Override
    public void removeWorkoutForUser(int UserID, int WorkoutID) {

    }

    @Override
    public int createNewWorkout(String name, String type, String difficulty) {
        return 0;
    }

    @Override
    public Workout loadWorkout(int id) {
        return null;
    }

    @Override
    public Workout updateWorkout(int id, String name, String type, String difficulty) {
        return null;
    }

    @Override
    public ArrayList<Workout> loadAllWorkouts() {
        return null;
    }

    @Override
    public int createNewWorkoutSession(int id, int progressive, String filepath) {
        return 0;
    }

    @Override
    public WorkoutSession loadWorkoutSession(int id) {
        return null;
    }

    @Override
    public void deleteWorkoutSession(int id) {

    }

    @Override
    public void updateWorkout(int id, int progressive, String photopath) {

    }

    @Override
    public void addExerciseForWorkoutSession(int intIDSession, int intIDExercise) {

    }

    @Override
    public UserWorkoutSession loadSession(int intUserSessionID) {
        return null;
    }

    @Override
    public void deleteSession(int intUserSessionID) {

    }

    @Override
    public void updateSession(int intUserSessionID, Date sessionDate, String strComment, int intIDUser, int idWorkout) {

    }

    @Override
    public int createSession(Date sessionDate, String strComment, int intIDUser, int idWorkout) {
        return 0;
    }

    @Override
    public ArrayList<WorkoutSession> loadAllSessionsForUser(int intIDUser) {
        return null;
    }

    @Override
    public int createUserExercise(int intIDExercise, int intIDUserWorkoutSession, boolean boolIsDone, String strComment) {
        return 0;
    }

    @Override
    public void updateUserExercise(int intIDUserWorkoutSession, boolean boolIsDone, String strComment) {

    }
}
