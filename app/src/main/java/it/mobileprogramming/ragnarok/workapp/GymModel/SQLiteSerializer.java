package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

/**
 * Created by paride on 18/06/15.
 */
public class SQLiteSerializer implements ExerciseSerializer,UserSerializer,WorkoutSerializer,WorkoutSessionSerializer {
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
    public int createNewUser(String strName, String strSurname, int intSex, Date dateBirth) {
        return 0;
    }

    @Override
    public User loadUser(int id) {
        return null;
    }

    @Override
    public void updateUser(int id, String strName, String strSurname, int intSex, Date dateBirth) {

    }

    @Override
    public void loadWeightHistory(int id) {

    }

    @Override
    public void removeFromWeightHistory(int userId, Date date) {

    }

    @Override
    public void addToWeightHistory(int intUserID, WeightItem anItem) {

    }

    @Override
    public void loadWorkoutsForUser(int id) {

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
}
