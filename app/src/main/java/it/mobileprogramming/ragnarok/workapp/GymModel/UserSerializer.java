package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

public interface UserSerializer {
    public int createNewUser(String strName, String strSurname, int intSex, Date dateBirth);
    public User loadUser(int id);
    public void updateUser(int id,String strName, String strSurname, int intSex, Date dateBirth);
    public void loadWeightHistory(int id);                      //weight history added to user features
    public void removeFromWeightHistory(int userId,Date date);
    public void addToWeightHistory(int intUserID, WeightItem anItem);
    public void loadWorkoutsForUser(int id);                    //workout history added to user features (note:load the correct istance from relation table)
    public void addWorkoutForUser(int userID,int WorkoutID);
    public void removeWorkoutForUser(int UserID,int WorkoutID);
}
