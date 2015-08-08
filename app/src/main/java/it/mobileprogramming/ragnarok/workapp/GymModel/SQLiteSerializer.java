package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by paride on 18/06/15.
 * this class manages the persistence implementing all the interfaces declared in the model
 * for further infos about each method please read the comments in the incerface file
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
    private final static String User_Surname_column         =   "Surname";
    private final static String User_Gender_column          =   "Gender";
    private final static String User_BirthDate_column       =   "Birthdate";
    private final static String Workout_tablename           =   "Workout";
    private final static String Workout_ID                  =   "WorkoutID";
    private final static String Workout_label               =   "Label";
    private final static String Workout_kind                =   "Kind";
    private final static String Workout_Level               =   "Level";
    private final static String Exercise_tablename          =   "Exercise";
    private final static String Exercise_label              =   "Name";
    private final static String Exercise_ID                 =   "ExerciseID";
    private final static String Exercise_series             =   "Series";
    private final static String Exercise_repetitions        =   "Repetitions";
    private final static String Exercise_pause              =   "Pause";
    private final static String Exercise_muscles            =   "Muscles";
    private final static String Exercise_frequency          =   "Frequency";
    private final static String Exercise_usedWeights        =   "UsedWeight";
    private final static String UserWeight_tablename        =   "UserWeight";
    private final static String UserWeight_userid           =   "UserID";
    private final static String UserWeight_date             =   "Date";
    private final static String UserWeight_value            =   "Weight";
    private final static String UserMakesWorkout_tablename  =   "UserMakesWorkout";
    private final static String UserMakesWorkoutUserID      =   "UserID";
    private final static String UserMakesWorkoutWorkoutID   =   "WorkoutID";
    private final static String WorkoutSession_tablename    =   "WorkoutSession";
    private final static String WorkoutSession_photopath    =   "Photopath";
    private final static String WorkoutSession_ID           =   "WorkoutSessionID";
    private final static String UserMakesSession_tablename  =   "UserMakesSession";
    private final static String UserMakesSession_WorkoutSID =   "WorkoutSessionID";
    private final static String UserMakesSession_Comment    =   "Comment";
    private final static String UserMakesSession_ExecutionD =   "ExecutionDate";
    private final static String UserMakesSession_UserID     =   "UserID";
    private final static String UserMakesSession_rating     =   "Rating";
    private final static String SessionMadeByExercises_tablename        =   "SessionMadeByExercises";
    private final static String SessionMadeByExercises_WorkoutSessionID =   "WorkoutSessionID";
    private final static String SessionMadeByExercises_ExerciseID       =   "ExerciseID";
    private final static String SessionMadeByExercises_Progressive      =   "Progressive";
    private final static String WorkoutMadeBySessions_tablename         =   "WorkoutMadeBySessions";
    private final static String WorkoutMadeBySessions_workoutID         =   "WorkoutID";
    private final static String WorkoutMadeBySessions_workoutSessionID  =   "WorkoutSessionID";
    private final static String WorkoutMadeBySessions_progressive       =   "Progressive";
    private final static String UserMakesExercise_tablename             =   "UserMakesExercise";
    private final static String UserMakesExercise_userID                =   "UserID";
    private final static String UserMakesExercise_comment               =   "Comment";
    private final static String UserMakesExercise_execution_date        =   "ExecutionDate";
    private final static String UserMakesExercise_done                  =   "Done";
    private final static String UserMakesExercise_rating                  =   "Rating";
    private final static String UserMakesExercise_exID =   "ExerciseID";

    /**
     * constructor
     * @param aContext application context
     * @param dbName path of the .db file
     */
    public SQLiteSerializer (Context aContext,String dbName){
        this.strdbName  =   dbName;
        this.appContext =   aContext;
    }

    /**
     * open the DB
     */
    public void open(){
        anHelper            =   new GymSQLiteHelper(this.appContext,strdbName);
        this.sqlGymDatabase =   anHelper.getWritableDatabase();
    }

    /**
     * close the DB
     */
    public void close(){
        sqlGymDatabase.close();
        anHelper.close();
    }

    @Override
    public int createNewExercise(int series, int repetition, int frequency, int recovery, String name,String muscle,String usedWeight) {
        ContentValues   values  = new ContentValues();
        values.put(Exercise_label,name);
        values.put(Exercise_series,series);
        values.put(Exercise_repetitions,repetition);
        values.put(Exercise_frequency,frequency);
        values.put(Exercise_pause, recovery);
        values.put(Exercise_muscles, muscle);
        values.put(Exercise_usedWeights,usedWeight);
        return  (int)this.sqlGymDatabase.insert(Exercise_tablename,null,values);
    }

    @Override
    public void deleteExercise(int idEx) {
        String field        =   Exercise_ID+"=?";
        String filter   []  =   {String.valueOf(idEx)};
        int rows            =   this.sqlGymDatabase.delete(Exercise_tablename, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public Exercise loadExercise(int id) {
        String field        =       Exercise_ID+"=?";
        String filter   []  =       {String.valueOf(id)};
        Cursor result       =       this.sqlGymDatabase.query(Exercise_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        if(result.getCount()>0) {
            int eid         =   result.getInt(result.getColumnIndex(Exercise_ID));
            String ename    =   result.getString(result.getColumnIndex(Exercise_label));
            int frequency   =   result.getInt(result.getColumnIndex(Exercise_frequency));
            int recovery    =   result.getInt(result.getColumnIndex(Exercise_pause));
            int repetition  =   result.getInt(result.getColumnIndex(Exercise_repetitions));
            int series      =   result.getInt(result.getColumnIndex(Exercise_series));
            String weights  =   result.getString(result.getColumnIndex(Exercise_usedWeights));
            String muscles  =   result.getString(result.getColumnIndex(Exercise_muscles));
            result.close();
            Exercise myEx   =   new Exercise(eid, this, frequency, ename, recovery, repetition, series, weights, muscles);
            System.out.println(myEx.toString());
            return myEx;
        }
        else return null;
    }


    @Override
    public void updateExercise(int id, String usedWeight, int series, int repetition, int frequency, int recovery, String name,String muscle) {
        ContentValues   values  =   new ContentValues();
        values.put(Exercise_label,name);
        values.put(Exercise_usedWeights, usedWeight);
        values.put(Exercise_series,series);
        values.put(Exercise_repetitions,repetition);
        values.put(Exercise_frequency,frequency);
        values.put(Exercise_pause,recovery);
        values.put(Exercise_muscles,muscle);
        String field            =   Exercise_ID;
        String filter   []      =   {String.valueOf(id)};
        int rows                =   this.sqlGymDatabase.update(Exercise_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public ArrayList<Exercise> loadAll() {
        Cursor result   =   this.sqlGymDatabase.query(Exercise_tablename,null,null,null,null,null,null);
        ArrayList<Exercise>returnList   =   new ArrayList<>();
        result.moveToFirst();
            for(int i=0;i<result.getCount();i++){
                int     eid         =   result.getInt(result.getColumnIndex(Exercise_ID));
                Exercise loaded         =   loadExercise(eid);
                //System.out.println("Loaded "+loaded.toString());
                returnList.add(loaded);
                result.moveToNext();
            }
        result.close();
        return returnList;
    }

    @Override
    public ArrayList<Exercise> loadExercisesOfASession(int intIDWorkoutSession) {
        String MY_QUERY = "SELECT * FROM "+SessionMadeByExercises_tablename+" NATURAL JOIN "+Exercise_tablename+" WHERE "+SessionMadeByExercises_WorkoutSessionID+"=? ORDER BY "+SessionMadeByExercises_Progressive;
        Cursor result   =   this.sqlGymDatabase.rawQuery(MY_QUERY, new String[]{String.valueOf(intIDWorkoutSession)});
        result.moveToFirst();
        ArrayList<Exercise> exercises   =   new ArrayList<>();
        for(int i=0;i<result.getCount();i++){
            int     exerciseID   =   result.getInt(result.getColumnIndex(Exercise_ID));
            int     frequency    =   result.getInt(result.getColumnIndex(Exercise_frequency));
            String  name         =   result.getString(result.getColumnIndex(Exercise_label));
            int     recovery     =   result.getInt(result.getColumnIndex(Exercise_pause));
            int     series       =   result.getInt(result.getColumnIndex(Exercise_series));
            int     repetition   =   result.getInt(result.getColumnIndex(Exercise_repetitions));
            String  usedWeight   =   result.getString(result.getColumnIndex(Exercise_usedWeights));
            String  muscles      =   result.getString(result.getColumnIndex(Exercise_muscles));
            Exercise anExercise  =   new Exercise(exerciseID,this,frequency,name,recovery,repetition,series,usedWeight,muscles);
            exercises.add(anExercise);
            result.moveToNext();
        }
        result.close();
        return exercises;
    }

    /**
     * insert a new user entry on DB
     * @param strName user name
     * @param strSurname user surname
     * @param pwd user password
     * @param intGender
     * @param dateBirth
     * @return
     */
    @Override
    public int createNewUser(String strName, String strSurname, String pwd, int intGender, Date dateBirth) {
        ContentValues   values  = new ContentValues();
        values.put(User_Name_column,strName);
        values.put(User_Surname_column,strSurname);
        values.put(User_Gender_column,intGender);
        values.put(User_Password_column, pwd);
        values.put(User_BirthDate_column, Singletons.getStringFromDate(dateBirth));
        return  (int)this.sqlGymDatabase.insert(User_USER_tablename,null,values);

    }

    @Override
    public ArrayList<User> getAllUsers() {
        Cursor result   =   this.sqlGymDatabase.query(User_USER_tablename,null,null,null,null,null,null);
        //System.out.println("found users "+result.getCount());
        ArrayList<User>returnList   =   new ArrayList<>();
        result.moveToFirst();
        for(int i=0;i<result.getCount();i++){
            int     uid             =   result.getInt(result.getColumnIndex(User_USERID_column));
            User loaded             =   loadUser(uid);
            returnList.add(loaded);
            result.moveToNext();
        }
        result.close();
        return returnList;
    }

    @Override
    public User loadUser(int id) {
        String field        =       User_USERID_column+"=?";
        String filter   []  =       {String.valueOf(id)};
        Cursor result       =       this.sqlGymDatabase.query(User_USER_tablename,null,field,filter,null,null,null);
        result.moveToFirst();
        if(result.getCount()>0) {
            int uid = result.getInt(result.getColumnIndex(User_USERID_column));
            String uname = result.getString(result.getColumnIndex(User_Name_column));
            String usurname = result.getString(result.getColumnIndex(User_Surname_column));
            String upassword = result.getString(result.getColumnIndex(User_Password_column));
            int ugender = result.getInt(result.getColumnIndex(User_Gender_column));
            Date ubirth = Singletons.formatFromString(result.getString(result.getColumnIndex(User_BirthDate_column)));
            ArrayList<UserWorkout> workoutArrayList = loadWorkoutsForUser(id);
            ArrayList<WeightItem> story = loadWeightHistory(id);
            User anUser = new User(uid, uname, usurname, ugender, ubirth, upassword, this, workoutArrayList, story);
            result.close();
            return anUser;
        }
        else return null;
    }

    @Override
    public void updateUser(int id, String strName, String strSurname, int intSex, Date dateBirth) {
        ContentValues   values  =   new ContentValues();
        values.put(User_Name_column, strName);
        values.put(User_Surname_column,strSurname);
        values.put(User_Gender_column,intSex);
        values.put(User_BirthDate_column,Singletons.getStringFromDate(dateBirth));
        String field            =       User_USERID_column+"=?";
        String filter   []      =       {String.valueOf(id)};
        int rows                =   this.sqlGymDatabase.update(User_USER_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void deleteUser(int id) {
        String field        =   User_USERID_column+"=?";
        String filter   []  =   {String.valueOf(id)};
        int rows            =   this.sqlGymDatabase.delete(User_USER_tablename, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public ArrayList<WeightItem> loadWeightHistory(int id) {
        String field                        =   UserWeight_userid + "=?";
        String filter[]                     =   {String.valueOf(id)};
        Cursor result                       =   this.sqlGymDatabase.query(UserWeight_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        ArrayList<WeightItem>   returnList  =   new ArrayList<>();
        for(int i=0;i<result.getCount();i++){
            Date date           =   Singletons.formatFromString(result.getString(result.getColumnIndex(UserWeight_date)));
            float weight        =   result.getFloat(result.getColumnIndex(UserWeight_value));
            WeightItem  anItem  =   new WeightItem();
            anItem.date         =   date;
            anItem.value        =   weight;
            returnList.add(anItem);
            result.moveToNext();
        }
        result.close();
        return returnList;
    }

    @Override
    public void removeFromWeightHistory(int userId, Date date) {
        String field        =   UserWeight_userid+"=? AND "+UserWeight_date+"=?";
        String filter   []  =   new String[2];
        filter[0]           =   String.valueOf(userId);
        filter[1]           =   String.valueOf(Singletons.getStringFromDate(date));
        int rows            =   this.sqlGymDatabase.delete(UserWeight_tablename,field,filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void addToWeightHistory(int intUserID, WeightItem anItem) {
        ContentValues   values  =   new ContentValues();
        values.put(UserWeight_userid,intUserID);
        values.put(UserWeight_value,anItem.value);
        values.put(UserWeight_date, Singletons.getStringFromDate(anItem.date));
        this.sqlGymDatabase.insert(UserWeight_tablename, null, values);
    }

    @Override
    public ArrayList<UserWorkout> loadWorkoutsForUser(int id) {
        ArrayList<UserWorkout> userWorkouts = new ArrayList<>();
        String field = UserMakesWorkoutUserID + "=?";
        String filter[] = {String.valueOf(id)};
        Cursor result = this.sqlGymDatabase.query(UserMakesWorkout_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        for(int i=0;i<result.getCount();i++){
            try {
                System.out.println("Loading workout n " + result.getInt(result.getColumnIndex(UserMakesWorkoutWorkoutID))+ " for user "+id);
                System.out.println("ID WORKOUT"+result.getInt(result.getColumnIndex(UserMakesWorkoutWorkoutID))+" ID USER "+result.getInt(result.getColumnIndex(UserMakesWorkoutUserID)));
                userWorkouts.add(loadUserWorkout(result.getInt(result.getColumnIndex(UserMakesWorkoutWorkoutID)), id));
            } catch (DataException e) {
                e.printStackTrace();
            }
            result.moveToNext();
        }
        result.close();
        return userWorkouts;
    }

    @Override
    public UserWorkout loadUserWorkout(int woId,int userID) throws DataException {
        //TODO check if there is THAT workout in the user workout DV
        String field        =       Workout_ID+"=?";
        String filter   []  =       {String.valueOf(woId)};
        Cursor result       =       this.sqlGymDatabase.query(Workout_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        if(result.getCount()>0) {
            int uid                                         =   result.getInt(result.getColumnIndex(Workout_ID));
            String label                                    =   result.getString(result.getColumnIndex(Workout_label));
            String level                                    =   result.getString(result.getColumnIndex(Workout_Level));
            String kind                                     =   result.getString(result.getColumnIndex(Workout_kind));
            UserWorkout aWorkout                            =   new UserWorkout(uid, label,userID, kind, level, this);
            ArrayList<UserWorkoutSession> workoutSessions   =   this.loadAllSessionsForUserWorkout(userID,woId);
            System.out.println("Workout made by "+workoutSessions.size()+" sessions");
            for(int i=0;i<workoutSessions.size();i++){
                aWorkout.addWorkoutSession(workoutSessions.get(i), false, i);
            }
            result.close();
            return aWorkout;
        }
        else throw new DataException("item not found on DB");
    }

    @Override
    public void addWorkoutForUser(int userID, int WorkoutID) {
        ContentValues   values      =   new ContentValues();
        values.put(UserMakesWorkoutUserID,userID);
        values.put(UserMakesWorkoutWorkoutID,WorkoutID);
        this.sqlGymDatabase.insert(UserMakesWorkout_tablename, null, values);
    }

    @Override
    public void removeWorkoutForUser(int userID, int workoutID) {
        String field        =   UserMakesWorkoutUserID+"=? AND "+UserMakesWorkoutWorkoutID+"=?";
        String filter   []  =   new String[2];
        filter[0]           =   String.valueOf(userID);
        filter[1]           =   String.valueOf(workoutID);
        int rows            =   this.sqlGymDatabase.delete(UserMakesWorkout_tablename,field,filter);
        System.out.println("updated rows: " + rows);

    }

    @Override
    public int createNewWorkout(String name, String type, String difficulty) {
        ContentValues   values  =   new ContentValues();
        values.put(Workout_label,name);
        values.put(Workout_kind,type);
        values.put(Workout_Level,difficulty);
        return  (int)this.sqlGymDatabase.insert(Workout_tablename,null,values);
    }

    @Override
    public void addSessiontoWorkout(int idWorkout, int idSession,int progressive) {
        ContentValues   values      =   new ContentValues();
        values.put(WorkoutMadeBySessions_workoutID,idWorkout);
        values.put(WorkoutMadeBySessions_workoutSessionID,idSession);
        values.put(WorkoutMadeBySessions_progressive,progressive);

        this.sqlGymDatabase.insert(WorkoutMadeBySessions_tablename, null, values);

    }

    @Override
    public Workout loadWorkout(int id) {
        String field        =       Workout_ID+"=?";
        String filter   []  =       {String.valueOf(id)};
        Cursor result       =       this.sqlGymDatabase.query(Workout_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        int     uid         =   result.getInt(result.getColumnIndex(Workout_ID));
        String  label       =   result.getString(result.getColumnIndex(Workout_label));
        String  level       =   result.getString(result.getColumnIndex(Workout_Level));
        String  kind        =   result.getString(result.getColumnIndex(Workout_kind));
        Workout aWorkout    =   new Workout(uid,label,kind,level,this);
        ArrayList<WorkoutSession>   sessionsList    =   this.loadAllWorkoutSessionsForWorkout(uid);
        for(int i=0;i<sessionsList.size();i++){
            aWorkout.addWorkoutSession(sessionsList.get(i),false,i);
        }
        result.close();
        return  aWorkout;
    }

    @Override
    public void deleteWorkout(int id) {
        String field        =       Workout_ID+"=?";
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.delete(Workout_tablename, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public ArrayList<WorkoutSession> loadAllWorkoutSessionsForWorkout(int woID) {
        String field            =       WorkoutMadeBySessions_workoutID+"=?";
        String filter   []      =       {String.valueOf(woID)};
        Cursor result           =       this.sqlGymDatabase.query(WorkoutMadeBySessions_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        ArrayList<WorkoutSession> sessionArrayList  =   new ArrayList<>();
        for(int i=0;i<result.getCount();i++){
                int     sid             =   result.getInt(result.getColumnIndex(WorkoutMadeBySessions_workoutSessionID));
                WorkoutSession  aSession=   this.loadWorkoutSession(sid);
                sessionArrayList.add(aSession);
                result.moveToNext();
        }
        result.close();
        return sessionArrayList;
    }

    @Override
    public void updateWorkout(int id, String name, String type, String difficulty) {
        ContentValues   values  = new ContentValues();
        values.put(Workout_label,name);
        values.put(Workout_kind,type);
        values.put(Workout_Level,difficulty);
        String field        =   Workout_ID+"=?";
        String filter   []  =   {String.valueOf(id)};
        int rows            =   this.sqlGymDatabase.update(Workout_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void removeSessionFromWorkout(int id, int seId) {
        String field        =   WorkoutMadeBySessions_workoutID+"=? AND "+WorkoutMadeBySessions_workoutSessionID+"=?";
        String filter   []  =   {String.valueOf(id),String.valueOf(seId)};
        int rows            =   this.sqlGymDatabase.delete(WorkoutMadeBySessions_tablename, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public ArrayList<Workout> loadAllWorkouts(boolean includeCustom) {
        Cursor result                   =   this.sqlGymDatabase.query(Workout_tablename,null,null,null,null,null,null);
        ArrayList<Workout>returnList    =   new ArrayList<>();
        result.moveToFirst();
        for(int i=0;i<result.getCount();i++){
            int     uid         =   result.getInt(result.getColumnIndex(Workout_ID));
            Workout aWorkout    =   loadWorkout(uid);
            System.out.println(aWorkout);
            String custom       =   Singletons.customString;
            if(!((aWorkout.getType().equals(custom))&&(!includeCustom))){
                returnList.add(aWorkout);
            }
            result.moveToNext();
        }
        result.close();
        return returnList;
    }

    @Override
    public int createNewWorkoutSession(String filepath) {
        ContentValues   values  =   new ContentValues();
        values.put(WorkoutSession_photopath, filepath);
        return  (int)this.sqlGymDatabase.insert(WorkoutSession_tablename,null,values);
    }

    @Override
    public WorkoutSession loadWorkoutSession(int id) {
        String field            =   WorkoutSession_ID+"=?";
        String filter   []      =   {String.valueOf(id)};
        Cursor result           =   this.sqlGymDatabase.query(WorkoutSession_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        if(result.getCount()>0){
            int     sid             =   result.getInt(result.getColumnIndex(WorkoutSession_ID));
            String  path            =   result.getString(result.getColumnIndex(WorkoutSession_photopath));
            WorkoutSession aSession =   new WorkoutSession(path,sid,this);
            ArrayList<Exercise> exerciseList    =   this.loadExercisesOfASession(id);
            for(int j=0;j<exerciseList.size();j++){
                aSession.addExerciseToWorkoutSession(exerciseList.get(j),j,false);
                System.out.println("Added to session " + id + " the following exercise " + exerciseList.get(j).toString());
            }
            result.close();
            return aSession;
        }
        result.close();
        System.out.println("Session "+id+"without exercises");
        return null;
    }

    @Override
    public void deleteWorkoutSession(int id) {
        String field        =       WorkoutSession_ID+"=?";
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.delete(Workout_tablename, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void updateWorkoutSession(int id, String photopath) {
        ContentValues   values  =   new ContentValues();
        values.put(WorkoutSession_photopath,photopath);
        String field            =   WorkoutSession_ID+"=?";
        String filter   []      =   {String.valueOf(id)};
        int rows                =   this.sqlGymDatabase.update(WorkoutSession_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void addExerciseForWorkoutSession(int intIDSession, int intIDExercise) {
        ContentValues   values  =   new ContentValues();
        values.put(SessionMadeByExercises_WorkoutSessionID,intIDSession);
        values.put(SessionMadeByExercises_ExerciseID,intIDExercise);
        this.sqlGymDatabase.insert(SessionMadeByExercises_tablename,null,values);
    }

    @Override
    public UserWorkoutSession loadSession(int intUserSessionID,User anUser,Date sessionDate) {
        String MY_QUERY = "SELECT * "+SessionMadeByExercises_tablename+" NATURAL JOIN "+Exercise_tablename+" NATURAL JOIN "+UserMakesExercise_tablename+" WHERE "+SessionMadeByExercises_WorkoutSessionID+"=? AND "+UserMakesExercise_execution_date+"=? AND "+UserMakesExercise_userID+"=?";
        Cursor result   =   this.sqlGymDatabase.rawQuery(MY_QUERY, new String[]{String.valueOf(intUserSessionID), Singletons.getStringFromDate(sessionDate), String.valueOf(anUser.getIntUserID())});
        String  photopath    =   result.getString(result.getColumnIndex(WorkoutSession_photopath));
        int     sessionID    =   result.getInt(result.getColumnIndex(UserMakesSession_WorkoutSID));
        String  comment      =   result.getString(result.getColumnIndex(UserMakesSession_Comment));
        int     rating       =   result.getInt(result.getColumnIndex(UserMakesSession_rating));
        UserWorkoutSession aSession =    new UserWorkoutSession(photopath,intUserSessionID,this,this,sessionDate,sessionID,comment,true,rating);
        ArrayList<UserExcercise>    userExcercises  =   this.getExercisesOfAUserSession(intUserSessionID,anUser.getIntUserID(),sessionDate);
        for (int j=0;j<userExcercises.size();j++){
            System.out.println("Exercise in workout " + userExcercises.get(j).toString());
            aSession.addExerciseToWorkoutSession(userExcercises.get(j),j,false);
        }
        result.close();
        return aSession;
    }

    @Override
    public void deleteSession(int intUserSessionID, int anUserID,Date sessionDate) {
        ArrayList<UserExcercise>    exercisesOfSession  =   this.getExercisesOfAUserSession(intUserSessionID,anUserID,sessionDate);
        for(int i=0;i<exercisesOfSession.size();i++){
            this.deleteUserExercise(anUserID,exercisesOfSession.get(i).getId(),sessionDate);
        }
        String field        =   UserMakesSession_WorkoutSID+"=? AND "+UserMakesSession_UserID+"=? AND "+UserMakesExercise_execution_date+"=?";
        String filter   []  =   {String.valueOf(intUserSessionID),String.valueOf(anUserID),Singletons.getStringFromDate(sessionDate)};
        int rows            =   this.sqlGymDatabase.delete(UserMakesSession_tablename, field, filter);
        System.out.println("updated rows: " + rows);
    }


    @Override
    public void updateSession(int intUserSessionID, Date sessionDate, String strComment, int intIDUser,int rating) {
        ContentValues   values  =   new ContentValues();
        values.put(UserMakesSession_Comment,strComment);
        values.put(UserMakesSession_rating,rating);
        String field        =   UserMakesSession_WorkoutSID+"=? AND "+UserMakesSession_UserID+"=? AND "+UserMakesSession_ExecutionD+"=?";
        String filter   []  =   {String.valueOf(intUserSessionID),String.valueOf(intIDUser),Singletons.getStringFromDate(sessionDate)};
        int rows            =   this.sqlGymDatabase.update(UserMakesSession_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public int createSession(Date sessionDate, String strComment, int intIDUser, int idSWorkout,int rating) {
        ContentValues   values  = new ContentValues();
        values.put(UserMakesSession_WorkoutSID,idSWorkout);
        values.put(UserMakesSession_UserID,intIDUser);
        values.put(UserMakesSession_Comment,strComment);
        values.put(UserMakesSession_rating,rating);
        values.put(UserMakesSession_ExecutionD,Singletons.getStringFromDate(sessionDate));
        return  (int)this.sqlGymDatabase.insert(UserMakesSession_tablename,null,values);
    }

    @Override
    public ArrayList<UserWorkoutSession> loadAllSessionsForUserWorkout(int myUserID,int workoutID){
        String MY_QUERY = "SELECT * FROM "+UserMakesSession_tablename+" NATURAL JOIN "+WorkoutSession_tablename+" NATURAL JOIN "+WorkoutMadeBySessions_tablename+" WHERE "+UserMakesSession_UserID+"=? AND "+UserMakesWorkoutWorkoutID+"=? ORDER BY "+WorkoutMadeBySessions_progressive;
        System.out.println("Query: "+MY_QUERY);
        Cursor result   =   this.sqlGymDatabase.rawQuery(MY_QUERY, new String[]{String.valueOf(myUserID),String.valueOf(workoutID)});
        ArrayList<UserWorkoutSession> userWorkoutSessionArrayList   =   new ArrayList<>();
        result.moveToFirst();
        for(int i=0;i<result.getCount();i++){
            //System.out.println(result.getColumnNames());
            int     progressive  =   result.getInt(result.getColumnIndex(WorkoutMadeBySessions_progressive));
            String  photopath    =   result.getString(result.getColumnIndex(WorkoutSession_photopath));
            Date    sessionDate  =   Singletons.formatFromString(result.getString(result.getColumnIndex(UserMakesSession_ExecutionD)));
            int     sessionID    =   result.getInt(result.getColumnIndex(UserMakesSession_WorkoutSID));
            int     rating       =   result.getInt(result.getColumnIndex(UserMakesSession_rating));
            String  comment      =   result.getString(result.getColumnIndex(UserMakesSession_Comment));
            UserWorkoutSession   aSession   =   new UserWorkoutSession(photopath,myUserID,this,this,sessionDate,sessionID,comment,true,rating);
            System.out.println("SESSIONE N "+progressive+" ARRAY DIM "+userWorkoutSessionArrayList.size());
            userWorkoutSessionArrayList.add(aSession);
            ArrayList<UserExcercise>    userExcercises  =   this.getExercisesOfAUserSession(sessionID,myUserID,sessionDate);
            System.out.println("Number of exercises in this userSession: for user"+myUserID+" session "+sessionID+" "+userExcercises.size());
            for (int j=0;j<userExcercises.size();j++){
                System.out.println("Exercise in workout " + userExcercises.get(j).toString());
                aSession.addExerciseToWorkoutSession(userExcercises.get(j),j,false);
            }
            result.moveToNext();
        }
        result.close();
        return userWorkoutSessionArrayList;
    }

    @Override
    public int createUserExercise(int intIDExercise, boolean boolIsDone, String strComment,int userID,Date executionDate,int rating) {
        ContentValues   values  = new ContentValues();
        values.put(UserMakesExercise_exID,intIDExercise);
        values.put(UserMakesExercise_done,boolIsDone);
        values.put(UserMakesExercise_comment, strComment);
        values.put(UserMakesSession_UserID, userID);
        values.put(UserMakesExercise_execution_date,Singletons.getStringFromDate(executionDate));
        values.put(UserMakesExercise_rating,rating);
        return (int) this.sqlGymDatabase.insert(UserMakesExercise_tablename, null, values);
    }

    @Override
    public ArrayList<UserExcercise> getExercisesOfAUserSession(int intIDUserWorkoutSession,int userID,Date executionDate) {//TODO controllare, non ho bisogno di id user???
        String  MY_QUERY    =   "SELECT * FROM "+UserMakesExercise_tablename+" JOIN "+Exercise_tablename+" ON "+UserMakesExercise_tablename+"."+UserMakesExercise_exID+"="+Exercise_tablename+"."+Exercise_ID+" NATURAL JOIN "+SessionMadeByExercises_tablename+" WHERE "+UserMakesExercise_tablename+"."+UserMakesSession_UserID+"=? AND "+UserMakesExercise_tablename+"."+UserMakesExercise_execution_date+"=\""+Singletons.getStringFromDate(executionDate)+"\" AND "+SessionMadeByExercises_tablename+"."+SessionMadeByExercises_WorkoutSessionID+"=? ORDER BY "+SessionMadeByExercises_Progressive;
        System.out.println("Query: "+MY_QUERY);
        //String MY_QUERY = "SELECT * FROM "+UserMakesSession_tablename+" NATURAL JOIN "+SessionMadeByExercises_tablename+" NATURAL JOIN "+Exercise_tablename;//+" WHERE "+SessionMadeByExercises_WorkoutSessionID+"=?"
        Cursor result   =   this.sqlGymDatabase.rawQuery(MY_QUERY, new String[]{String.valueOf(userID),String.valueOf(intIDUserWorkoutSession)});//TODO clean
        ArrayList<UserExcercise> userWorkoutSessionArrayList = new ArrayList<>();
        System.out.println("number of exercises of this userworkoutsession "+intIDUserWorkoutSession+" "+result.getCount());
        result.moveToFirst();
        for(int i=0;i<result.getCount();i++){
            int     exerciseID   =   result.getInt(result.getColumnIndex(Exercise_ID));
            int     frequency    =   result.getInt(result.getColumnIndex(Exercise_frequency));
            String  name         =   result.getString(result.getColumnIndex(Exercise_label));
            int     recovery     =   result.getInt(result.getColumnIndex(Exercise_pause));
            int     series       =   result.getInt(result.getColumnIndex(Exercise_series));
            int     repetition   =   result.getInt(result.getColumnIndex(Exercise_repetitions));
            String  usedWeight   =   result.getString(result.getColumnIndex(Exercise_usedWeights));
            String  muscles      =   result.getString(result.getColumnIndex(Exercise_muscles));
            String  comment      =   result.getString(result.getColumnIndex(UserMakesExercise_comment));
            int     progressive  =   result.getInt(result.getColumnIndex(SessionMadeByExercises_Progressive));
            //TODO handle boolean with int
            boolean completed    =   false;
            userWorkoutSessionArrayList.add(new UserExcercise(exerciseID,executionDate,userID,this,frequency,name,recovery,repetition,series,usedWeight,completed,comment,muscles,this));
            //TODO variable "progressive" needs to be readded
            result.moveToNext();
        }
        result.close();
        return userWorkoutSessionArrayList;
    }

    @Override
    public void updateUserExercise(int intIDUser,int intIDExercise,Date adate, boolean boolIsDone, String strComment,int rating) {
        ContentValues   values  = new ContentValues();
        values.put(UserMakesExercise_done,boolIsDone);
        values.put(UserMakesExercise_comment,strComment);
        String field        =       UserMakesExercise_userID+"=? AND "+ UserMakesExercise_exID +"=? AND "+UserMakesExercise_execution_date+"=?";
        String filter   []  =       {String.valueOf(intIDUser),String.valueOf(intIDExercise),Singletons.getStringFromDate(adate)};
        int rows    =   this.sqlGymDatabase.update(UserMakesExercise_tablename, values, field, filter);
        values.put(UserMakesExercise_rating, rating);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void deleteUserExercise(int intIDUser, int intIDExercise, Date date) {
        String field        =       UserMakesExercise_userID+"=? AND "+UserMakesExercise_exID+"=? AND "+UserMakesExercise_execution_date+"=?";
        String filter   []  =       {String.valueOf(intIDUser),String.valueOf(intIDExercise),Singletons.getStringFromDate(date)};
        int rows    =   this.sqlGymDatabase.delete(UserMakesExercise_tablename, field, filter);
        System.out.println("updated rows: " + rows);
    }
}
