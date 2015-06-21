package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.ArrayList;

public interface ExerciseSerializer {
    public int createNewExercise(int series,int repetition,int frequency,int recovery,String name,String muscle);
    public Exercise loadExercise(int id);
    public void updateExercise(int id,String usedWeight,int series,int repetition,int frequency,int recovery,String name,String muscle);
    public ArrayList<Exercise> loadAll();
}
