package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /**
     * constructor that saves a new User instance on DB, don't use for loading an User from database
     * @param name name
     * @param surname surname
     * @param gender gender 0 male 1 female
     * @param birthDate date birthdate
     * @param password password (will be processed)
     * @param aSerializer serializer to save data
     */
    public User(String name,String surname,int gender,Date birthDate,String password,UserSerializer aSerializer){
        this.strName        =   name;
        this.strSurname     =   surname;
        this.intGender      =   gender;
        this.dateBirth      =   birthDate;
        this.strPwdHash     =   hashAndSalt(password);
        this.usSerializer   =   aSerializer;
        this.weightHistory  =   new ArrayList<>();
        this.workoutHistory =   new ArrayList<>();
        this.intUserID  =   aSerializer.createNewUser(name,surname,this.strPwdHash,gender,birthDate);
    }

    /**
     * constructor used to load an User instance from Database
     * @param uId user id
     * @param name user name
     * @param surname user surname
     * @param gender user gender 0 male 1 female
     * @param birthDate user birth date
     * @param pwdHash pass hash
     * @param aSerializer serializer
     * @param workouts arraylist of workouts
     * @param weightStory arraylist of weight history
     */
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

    /**
     * returns user birthdate
     * @return date birthdate
     */
    public Date getDateBirth() {
        return (Date) dateBirth.clone();
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
     * returns user surname
     * @return String surname
     */
    public String getStrSurname() {
        return strSurname+"";
    }

    /**
     * function used to SHA-1 and salt the password
     * @param password password to be salted
     * @return digest
     */
    //TODO hash and salt password
    private String hashAndSalt(String password) {
        String hash = null;
        try
        {
            String myPassword   =   password+"workapp~2015";
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = myPassword.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex( bytes );
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }

    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
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
