package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;

/**
 * Serializer class for Exercise, declares all CRUD methods needed
 */
public interface ExerciseSerializer {

    /**
     * create new exercise entry in DB
     * @param series number of series of repetitions of this exercise
     * @param repetition number of repetitions of this exercise
     * @param frequency exercise frequency
     * @param recovery exercise recovery time
     * @param name exercise name
     * @param muscle muscles involved in this exercise
     * @param usedWeight description of the used weights
     * @return return the new exercise ID in DB
     */
    int createNewExercise(int series,int repetition,int frequency,int recovery,String name,String muscle,String usedWeight);

    /**
     * loads an exercise from DB
     * @param id id of exercise
     * @return exercise istance
     */
    Exercise loadExercise(int id);

    /**
     * updates an existing exercise
     * @param id id of the exercise
     * @param usedWeight description of the used weights
     * @param series number of series
     * @param repetition number of repetitions
     * @param frequency exercise frequency
     * @param recovery exercise recovery time
     * @param name exercise name
     * @param muscle involved muscles
     */
    void updateExercise(int id,String usedWeight,int series,int repetition,int frequency,int recovery,String name,String muscle);

    /**
     * load all the existing exercises from DB
     * @return arraylist with all exercises
     */
    ArrayList<Exercise> loadAll();

    /**
     * returns the whole list of exercises of a session
     * @param intIDWorkoutSession id of the session
     * @return lists of exercises
     */
    ArrayList<Exercise> loadExercisesOfASession(int intIDWorkoutSession);

    /**
     * deletes an existing exercise from DB
     * @param idEx exercise id
     */
    void deleteExercise(int idEx);
}
