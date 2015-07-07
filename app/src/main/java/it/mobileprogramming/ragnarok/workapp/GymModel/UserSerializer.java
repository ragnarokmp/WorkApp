package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

public interface UserSerializer {
    /**
     * adds a new User entry on DB
     * @param strName user name
     * @param strSurname user surname
     * @param hashPWD hash of password
     * @param intSex user gender
     * @param dateBirth birth date
     * @return userID
     */
    public int createNewUser(String strName, String strSurname,String hashPWD, int intSex, Date dateBirth);

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
     * updates an existing user
     * @param id id of the user (PK)
     * @param strName name
     * @param strSurname surname
     * @param intSex gender
     * @param dateBirth birthdate
     */
    void updateUser(int id,String strName, String strSurname, int intSex, Date dateBirth);

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
