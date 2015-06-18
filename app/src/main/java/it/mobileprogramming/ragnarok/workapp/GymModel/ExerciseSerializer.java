package it.mobileprogramming.ragnarok.workapp.GymModel;

public interface ExerciseSerializer {
    public int createNewExercise(String usedWeight,int series,int repetition,int frequency,int recovery,String name);
    public Exercise loadExercise(int id);
    public void updateExercise(int id,String usedWeight,int series,int repetition,int frequency,int recovery,String name);
}
