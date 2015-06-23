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
    private final static String WorkoutSession_Progressive  =   "Progressive";
    private final static String UserMakesSession_tablename  =   "UserMakesSession";
    private final static String UserMakesSession_WorkoutSID =   "WorkoutSessionID";
    private final static String UserMakesSession_Comment    =   "Comment";
    private final static String UserMakesSession_ExecutionD =   "ExecutionDate";
    private final static String UserMakesSession_UserID     =   "UserID";
    private final static String SessionMadeByExercises_tablename        =   "SessionMadeByExercises";
    private final static String SessionMadeByExercises_WorkoutSessionID =   "WorkoutSessionID";
    private final static String SessionMadeByExercises_ExerciseID       =   "ExerciseID";
    private final static String SessionMadeByExercises_Completed        =   "Completed";
    private final static String SessionMadeByExercises_Comment          =   "Comment";
    private final static String WorkoutMadeBySessions_tablename         =   "WorkoutMadeBySessions";
    private final static String WorkoutMadeBySessions_workoutID         =   "WorkoutID";
    private final static String WorkoutMadeBySessions_workoutSessionID  =   "WorkoutSessionID";
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
    public int createNewExercise(int series, int repetition, int frequency, int recovery, String name,String muscle) {
        ContentValues   values  = new ContentValues();
        values.put(Exercise_label,name);
        values.put(Exercise_series,series);
        values.put(Exercise_repetitions,repetition);
        values.put(Exercise_frequency,frequency);
        values.put(Exercise_pause, recovery);
        values.put(Exercise_muscles, muscle);
        return  (int)this.sqlGymDatabase.insert(Exercise_tablename,null,values);
    }

    @Override
    public Exercise loadExercise(int id) {
        String field        =       Exercise_ID+"=?";
        String filter   []  =       {String.valueOf(id)};
        Cursor result       =       this.sqlGymDatabase.query(User_USER_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        int     eid             =   result.getInt(result.getColumnIndex(Exercise_ID));
        String  ename           =   result.getString(result.getColumnIndex(Exercise_label));
        int     frequency       =   result.getInt(result.getColumnIndex(Exercise_frequency));
        int     recovery        =   result.getInt(result.getColumnIndex(Exercise_pause));
        int     repetition      =   result.getInt(result.getColumnIndex(Exercise_repetitions));
        int     series          =   result.getInt(result.getColumnIndex(Exercise_series));
        String  weights         =   result.getString(result.getColumnIndex(Exercise_usedWeights));
        String  muscles         =   result.getString(result.getColumnIndex(Exercise_muscles));
        result.close();
        return  new Exercise(eid,this,frequency,ename,recovery,repetition,series,weights,muscles);
    }

    @Override
    public void updateExercise(int id, String usedWeight, int series, int repetition, int frequency, int recovery, String name,String muscle) {
        ContentValues   values  = new ContentValues();
        values.put(Exercise_label,name);
        values.put(Exercise_usedWeights, usedWeight);
        values.put(Exercise_series,series);
        values.put(Exercise_repetitions,repetition);
        values.put(Exercise_frequency,frequency);
        values.put(Exercise_pause,recovery);
        values.put(Exercise_muscles,muscle);
        String field        =       Exercise_ID;
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.update(Exercise_tablename, values, field, filter);
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
    public ArrayList<Exercise> getExercisesOfASession(int intIDWorkoutSession) {
        String MY_QUERY = "SELECT * FROM "+SessionMadeByExercises_tablename+" NATURAL JOIN "+Exercise_tablename+" WHERE "+SessionMadeByExercises_WorkoutSessionID+"=?";
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

    @Override
    public int createNewUser(String strName, String strSurname, String pwd, int intSex, Date dateBirth) {
        ContentValues   values  = new ContentValues();
        values.put(User_Name_column,strName);
        values.put(User_Surname_column,strSurname);
        values.put(User_Gender_column,intSex);
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
            int     uid         =   result.getInt(result.getColumnIndex(User_USERID_column));
            User loaded         =   loadUser(uid);
            //System.out.println("Loaded "+loaded.toString());
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
        int     uid         =   result.getInt(result.getColumnIndex(User_USERID_column));
        String  uname       =   result.getString(result.getColumnIndex(User_Name_column));
        String  usurname    =   result.getString(result.getColumnIndex(User_Surname_column));
        String  upassword   =   result.getString(result.getColumnIndex(User_Password_column));
        int     ugender     =   result.getInt(result.getColumnIndex(User_Gender_column));
        Date    ubirth      =   Singletons.formatFromString(result.getString(result.getColumnIndex(User_BirthDate_column)));
        ArrayList<UserWorkout>  workoutArrayList    =   loadWorkoutsForUser(id);
        ArrayList<WeightItem>   story   =   loadWeightHistory(id);
        User    anUser      =   new User(uid,uname,usurname,ugender,ubirth,upassword,this,workoutArrayList,story);
         result.close();
        return  anUser;
    }

    @Override
    public void updateUser(int id, String strName, String strSurname, int intSex, Date dateBirth) {
        ContentValues   values  = new ContentValues();
        values.put(User_Name_column, strName);
        values.put(User_Surname_column,strSurname);
        values.put(User_Gender_column,intSex);
        values.put(User_BirthDate_column,Singletons.getStringFromDate(dateBirth));
        String field        =       User_USERID_column+"=?";
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.update(User_USER_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void deleteUser(int id) {
        String field        =       User_USERID_column+"=?";
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.delete(User_USER_tablename, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public ArrayList<WeightItem> loadWeightHistory(int id) {
        String field = UserWeight_userid + "=?";
        String filter[] = {String.valueOf(id)};
        Cursor result = this.sqlGymDatabase.query(UserWeight_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        ArrayList<WeightItem>   returnList  =   new ArrayList<>();
        for(int i=0;i<result.getCount();i++){
            Date date   =   Singletons.formatFromString(result.getString(result.getColumnIndex(UserWeight_date)));
            float weight  =   result.getFloat(result.getColumnIndex(UserWeight_value));
            WeightItem  anItem  =   new WeightItem();
            anItem.date     =   date;
            anItem.value    =   weight;
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
        int rows    =   this.sqlGymDatabase.delete(UserWeight_tablename,field,filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void addToWeightHistory(int intUserID, WeightItem anItem) {
        ContentValues   values  = new ContentValues();
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
            int uid = result.getInt(result.getColumnIndex(Workout_ID));
            String label = result.getString(result.getColumnIndex(Workout_label));
            String level = result.getString(result.getColumnIndex(Workout_Level));
            String kind = result.getString(result.getColumnIndex(Workout_kind));
            UserWorkout aWorkout = new UserWorkout(uid, label, kind, level, this);
            ArrayList<UserWorkoutSession> workoutSessions   =   this.loadAllSessionsForUserWorkout(userID,woId);
            for(int i=0;i<workoutSessions.size();i++){
                aWorkout.addUserWorkoutSession(workoutSessions.get(i));
            }
            //TODO load data for lists
            result.close();
            return aWorkout;
        }
        else throw new DataException("item not found on DB");
    }

    @Override
    public void addWorkoutForUser(int userID, int WorkoutID) {
        ContentValues   values  = new ContentValues();
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
        int rows    =   this.sqlGymDatabase.delete(UserMakesWorkout_tablename,field,filter);
        System.out.println("updated rows: " + rows);

    }

    @Override
    public int createNewWorkout(String name, String type, String difficulty) {
        ContentValues   values  = new ContentValues();
        values.put(Workout_label,name);
        values.put(Workout_kind,type);
        values.put(Workout_Level,difficulty);
        return  (int)this.sqlGymDatabase.insert(Workout_tablename,null,values);
    }

    @Override
    public void addSessiontoWorkout(int idWorkout, int idSession) {

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
            aWorkout.addWorkoutSession(sessionsList.get(i),false);
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
        String field        =       Workout_ID+"=?";
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.update(Workout_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void removeSessionFromWorkout(int id, int seId) {

    }

    @Override
    public ArrayList<Workout> loadAllWorkouts(boolean includeCustom) {
        Cursor result   =   this.sqlGymDatabase.query(Workout_tablename,null,null,null,null,null,null);
        ArrayList<Workout>returnList   =   new ArrayList<>();
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
    public int createNewWorkoutSession(int progressive, String filepath) {
        ContentValues   values  = new ContentValues();
        values.put(WorkoutSession_photopath, filepath);
        values.put(WorkoutSession_Progressive, progressive);
        return  (int)this.sqlGymDatabase.insert(Workout_tablename,null,values);
    }

    @Override
    public WorkoutSession loadWorkoutSession(int id) {
        String field            =       WorkoutSession_ID+"=?";
        String filter   []      =       {String.valueOf(id)};
        Cursor result           =       this.sqlGymDatabase.query(WorkoutSession_tablename, null, field, filter, null, null, null);
        result.moveToFirst();
        if(result.getCount()>0){
            int     sid             =   result.getInt(result.getColumnIndex(WorkoutSession_ID));
            String  path            =   result.getString(result.getColumnIndex(WorkoutSession_photopath));
            int     progressive     =   result.getInt(result.getColumnIndex(WorkoutSession_Progressive));
            WorkoutSession aSession =   new WorkoutSession(path,sid,progressive,this);
            ArrayList<Exercise> exerciseList    =   this.getExercisesOfASession(id);
            for(int j=0;j<exerciseList.size();j++){
                aSession.addExerciseToWorkoutSession(exerciseList.get(j));
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
    public void updateWorkoutSession(int id, int progressive, String photopath) {
        ContentValues   values  = new ContentValues();
        values.put(WorkoutSession_Progressive,progressive);
        values.put(WorkoutSession_photopath,photopath);
        String field        =       WorkoutSession_ID+"=?";
        String filter   []  =       {String.valueOf(id)};
        int rows    =   this.sqlGymDatabase.update(WorkoutSession_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }

    @Override
    public void addExerciseForWorkoutSession(int intIDSession, int intIDExercise) {
        ContentValues   values  = new ContentValues();
        values.put(SessionMadeByExercises_WorkoutSessionID,intIDSession);
        values.put(SessionMadeByExercises_ExerciseID,intIDExercise);
        this.sqlGymDatabase.insert(SessionMadeByExercises_tablename,null,values);
    }

    @Override
    public UserWorkoutSession loadSession(int intUserSessionID,User anUser) {
        String MY_QUERY = "SELECT * "+SessionMadeByExercises_tablename+" NATURAL JOIN "+Exercise_tablename+" WHERE "+SessionMadeByExercises_WorkoutSessionID+"=?";
        Cursor result   =   this.sqlGymDatabase.rawQuery(MY_QUERY, new String[]{String.valueOf(intUserSessionID)});
        int     progressive  =   result.getInt(result.getColumnIndex(WorkoutSession_Progressive));
        String  photopath    =   result.getString(result.getColumnIndex(WorkoutSession_photopath));
        Date    sessionDate  =   Singletons.formatFromString(result.getString(result.getColumnIndex(UserMakesSession_ExecutionD)));
        int     sessionID    =   result.getInt(result.getColumnIndex(UserMakesSession_WorkoutSID));
        String  comment      =   result.getString(result.getColumnIndex(UserMakesSession_Comment));
        return new UserWorkoutSession(photopath,intUserSessionID,progressive,this,this,sessionDate,sessionID,comment,true);
    }

    @Override
    public void deleteSession(int intUserSessionID, User anUser) {
        //TODO delete session from workout, delete all exercise istances for that session
    }


    @Override
    public void updateSession(int intUserSessionID, Date sessionDate, String strComment, int intIDUser, int idWorkout) {
        ContentValues   values  = new ContentValues();
        values.put(UserMakesSession_Comment,strComment);
        //TODO replace with raw query
        /*
        values.put(SessionMadeByExercises_Comment,strComment);
        String field        =       SessionMadeByExercises_WorkoutSessionID+"=? AND "+SessionMadeByExercises_ExerciseID+"=?";
        String filter   []  =       {String.valueOf(intIDUserWorkoutSession),String.valueOf(intIDExercise)};
        int rows    =   this.sqlGymDatabase.update(SessionMadeByExercises_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);*/
    }

    @Override
    public int createSession(Date sessionDate, String strComment, int intIDUser, int idSWorkout) {
        ContentValues   values  = new ContentValues();
        values.put(UserMakesSession_WorkoutSID,idSWorkout);
        values.put(UserMakesSession_UserID,intIDUser);
        values.put(UserMakesSession_Comment,strComment);
        values.put(UserMakesSession_ExecutionD,Singletons.getStringFromDate(sessionDate));
        return  (int)this.sqlGymDatabase.insert(UserMakesSession_tablename,null,values);
    }

    @Override
    public ArrayList<UserWorkoutSession> loadAllSessionsForUserWorkout(int myUserID,int workoutID){
        String MY_QUERY = "SELECT * FROM "+UserMakesSession_tablename+" NATURAL JOIN "+WorkoutSession_tablename+" NATURAL JOIN "+WorkoutMadeBySessions_tablename+" WHERE "+UserMakesSession_UserID+"=? AND "+UserMakesWorkoutWorkoutID+"=?";
        Cursor result   =   this.sqlGymDatabase.rawQuery(MY_QUERY, new String[]{String.valueOf(myUserID),String.valueOf(workoutID)});
        ArrayList<UserWorkoutSession> userWorkoutSessionArrayList   =   new ArrayList<>();
        result.moveToFirst();
        for(int i=0;i<result.getCount();i++){
            int     progressive  =   result.getInt(result.getColumnIndex(WorkoutSession_Progressive));
            String  photopath    =   result.getString(result.getColumnIndex(WorkoutSession_photopath));
            Date    sessionDate  =   Singletons.formatFromString(result.getString(result.getColumnIndex(UserMakesSession_ExecutionD)));
            int     sessionID    =   result.getInt(result.getColumnIndex(UserMakesSession_WorkoutSID));
            String  comment      =   result.getString(result.getColumnIndex(UserMakesSession_Comment));
            userWorkoutSessionArrayList.add(new UserWorkoutSession(photopath,myUserID,progressive,this,this,sessionDate,sessionID,comment,true));
            result.moveToNext();
        }
        result.close();
        return userWorkoutSessionArrayList;
    }

    @Override
    public int createUserExercise(int intIDExercise, int intIDUserWorkoutSession, boolean boolIsDone, String strComment) {
        ContentValues   values  = new ContentValues();
        values.put(SessionMadeByExercises_ExerciseID,intIDExercise);
        values.put(SessionMadeByExercises_WorkoutSessionID,intIDUserWorkoutSession);
        values.put(SessionMadeByExercises_Completed,boolIsDone);
        values.put(SessionMadeByExercises_Comment, strComment);
        return (int) this.sqlGymDatabase.insert(SessionMadeByExercises_tablename, null, values);
    }

    @Override
    public ArrayList<UserExcercise> getExercisesOfAUserSession(int intIDUserWorkoutSession) {
        String MY_QUERY = "SELECT * "+UserMakesSession_tablename+" NATURAL JOIN "+SessionMadeByExercises_tablename+" NATURAL JOIN "+Exercise_tablename+" WHERE "+UserMakesWorkoutWorkoutID+"=?";
        Cursor result   =   this.sqlGymDatabase.rawQuery(MY_QUERY, new String[]{String.valueOf(intIDUserWorkoutSession)});
        ArrayList<UserExcercise> userWorkoutSessionArrayList   =   new ArrayList<>();
        for(int i=0;i<result.getCount();i++){
            int     exerciseID   =   result.getInt(result.getColumnIndex(Exercise_ID));
            int     frequency    =   result.getInt(result.getColumnIndex(Exercise_frequency));
            String  name         =   result.getString(result.getColumnIndex(Exercise_label));
            int     recovery     =   result.getInt(result.getColumnIndex(Exercise_pause));
            int     series       =   result.getInt(result.getColumnIndex(Exercise_series));
            int     repetition   =   result.getInt(result.getColumnIndex(Exercise_repetitions));
            String  usedWeight   =   result.getString(result.getColumnIndex(Exercise_usedWeights));
            String  muscles      =   result.getString(result.getColumnIndex(Exercise_muscles));
            String  comment      =   result.getString(result.getColumnIndex(SessionMadeByExercises_Comment));
            //TODO handle boolean with int
            boolean completed    =   false;
            //boolean completed    =   (boolean)result.getInt(result.getColumnIndex(SessionMadeByExercises_Completed));
            int     progressive  =   result.getInt(result.getColumnIndex(WorkoutSession_Progressive));
            String  photopath    =   result.getString(result.getColumnIndex(WorkoutSession_photopath));
            Date    sessionDate  =   Singletons.formatFromString(result.getString(result.getColumnIndex(UserMakesSession_ExecutionD)));
            int     sessionID    =   result.getInt(result.getColumnIndex(UserMakesSession_WorkoutSID));
            userWorkoutSessionArrayList.add(new UserExcercise(exerciseID,this,frequency,name,recovery,repetition,series,usedWeight,completed,comment,muscles));
            result.moveToNext();
        }
        result.close();
        return userWorkoutSessionArrayList;
    }

    @Override
    public void updateUserExercise(int intIDUserWorkoutSession,int intIDExercise, boolean boolIsDone, String strComment) {
        ContentValues   values  = new ContentValues();
        values.put(SessionMadeByExercises_Completed,boolIsDone);
        values.put(SessionMadeByExercises_Comment,strComment);
        String field        =       SessionMadeByExercises_WorkoutSessionID+"=? AND "+SessionMadeByExercises_ExerciseID+"=?";
        String filter   []  =       {String.valueOf(intIDUserWorkoutSession),String.valueOf(intIDExercise)};
        int rows    =   this.sqlGymDatabase.update(SessionMadeByExercises_tablename, values, field, filter);
        System.out.println("updated rows: " + rows);
    }
}
