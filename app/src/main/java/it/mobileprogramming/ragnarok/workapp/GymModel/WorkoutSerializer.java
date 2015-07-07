package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;
import java.util.Date;

public interface WorkoutSerializer {
    /**
     * creates a new workout entry on DB
     * @param name workout name
     * @param type workout type
     * @param difficulty workout difficulty
     * @return new entry ID
     */
    int createNewWorkout(String name, String type, String difficulty);

    /**
     * adds a session to a workout
     * @param idWorkout workout id
     * @param idSession session id
     * @param progressive int progressive
     */
    void addSessiontoWorkout(int idWorkout,int idSession,int progressive);

    /**
     * loads a workout from DB
     * @param id workout ID
     * @return new workout
     */
    Workout loadWorkout(int id);

    /**
     * deletes a workout from DB
     * @param id workout ID
     */
    void deleteWorkout(int id);

    /**
     * load all sessions for a workout
     * @param woID
     * @return
     */
    ArrayList<WorkoutSession> loadAllWorkoutSessionsForWorkout(int woID);

    /**
     * updates an existing workout
     * @param id workout ID
     * @param name workout name
     * @param type workout type
     * @param difficulty workout difficulty
     */
    void updateWorkout(int id,String name, String type, String difficulty);

    /**
     * removes a session from relationship with a workout
     * @param id workout id
     * @param seId session id
     */
    void removeSessionFromWorkout(int id,int seId);

    /**
     * load all workouts existing from DB
     * @param includeCustom set true if you want to load also custom workouts
     * @return arraylist of workout
     */
    ArrayList<Workout> loadAllWorkouts(boolean includeCustom);

    /**
     * loads an userworkout
     * @param woId workout id
     * @param userID workout owner
     * @return userworkout
     * @throws DataException throw exception in case of DB errors
     */
    UserWorkout loadUserWorkout(int woId,int userID) throws DataException;
}
