package it.mobileprogramming.ragnarok.workapp.GymModel;

/**
 * This class models a workout session made by exercises
 */
public interface WorkoutSessionSerializer {
    /**
     * saves a new workout session entry on DB
     * @param filepath
     * @return
     */
    int createNewWorkoutSession(String filepath);

    /**
     * loads a workout session from DB
     * @param id session id
     * @return corresponding workout session
     */
    WorkoutSession loadWorkoutSession(int id);

    /**
     * delete a workout session
     * @param id id of the session to be deleted
     */
    void deleteWorkoutSession (int id);

    /**
     * update an existing workout session
     * @param id id of the session to be updated
     * @param photopath path of the image for session file
     */
    void updateWorkoutSession(int id,String photopath);

    /**
     * adds an exercise to a session
     * @param intIDSession id of the session
     * @param intIDExercise id of the exercise
     */
    void addExerciseForWorkoutSession(int intIDSession, int intIDExercise);
}
