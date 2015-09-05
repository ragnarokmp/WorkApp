package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

public interface UserSerializer {
    /**
     * adds a new User entry on DB
     * @param strName user name
     * @param strEmail user email
     * @param avatar encoded avatar
     * @param intSex user gender
     * @param dateReg registration date
     * @return userID
     */
    public int createNewUser(String strName, String strEmail,String avatar, int intSex, Date dateReg);

    /**
     * returns all users (SELECT *)
     * @return all users from DB
     */
    public ArrayList<User> getAllUsers();

    /**
     * loads user with an ID from DB
     * @param id id of the user to be loaded
     * @return user instance
     */
    User loadUser(int id);

    /**
     * loads user from email
     * @param email emailaddress
     * @return corresponding user
     */
    User loadUser(String email);

    /**
     * updates an existing user
     * @param id id of the user (PK)
     * @param strName name
     * @param strEmail surname
     * @param intSex gender
     * @param dateReg registration date
     */

    void updateUser(int id,String strName, String strEmail, int intSex, Date dateReg);

    /**
     * remove from DB user with an ID
     * @param id id of the user to be deleted
     */
    void deleteUser(int id);

    /**
     * loads the weight history of the user
     * @param id id of the user
     * @return arraylist of weightItem (story)
     */
    ArrayList<WeightItem> loadWeightHistory(int id);                      //weight history added to user features

    /**
     * removes an entry from user weight history
     * @param userId id of user
     * @param date date of item to be deleted
     */
    void removeFromWeightHistory(int userId,Date date);

    /**
     * adds end entry to user weight history
     * @param intUserID user to be updated
     * @param anItem item to be saved
     */
    void addToWeightHistory(int intUserID, WeightItem anItem);

    /**
     * loads all the workouts for an user
     * @param id user id
     * @return array of workouts
     */
    ArrayList<UserWorkout> loadWorkoutsForUser(int id);                    //workout history added to user features (note:load the correct istance from relation table)

    /**
     * adds a workout for an user
     * @param userID user to be edited
     * @param WorkoutID workout to be added
     */
    void addWorkoutForUser(int userID,int WorkoutID);

    /**
     * removes a workout from list of a user
     * @param UserID user to be edited
     * @param WorkoutID workout id to be removed
     */
    void removeWorkoutForUser(int UserID,int WorkoutID);
}
