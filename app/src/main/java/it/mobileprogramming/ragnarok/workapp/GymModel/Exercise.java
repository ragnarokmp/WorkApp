package it.mobileprogramming.ragnarok.workapp.GymModel;

public class Exercise {
    private int intExId;
    private String  usedWeight;
    private int     series;
    private int     repetition;
    private int     frequency;
    private int     recovery;
    private String  name;
    private ExerciseSerializer aSerializer;
    private boolean completed   =   false;

    /**
     * constructor for a new User, saves also to DB don't use this for DB loaded users
     * @param aSerializer   object saving with persistence an exercise
     * @param frequency     frequency of the exercise (hz)
     * @param name          name of the exercise
     * @param recovery      recovery time
     * @param repetition    number of repetitions
     * @param series        number of series
     * @param usedWeight    value of the weight used
     */
    public Exercise(ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight) {
        this.aSerializer = aSerializer;
        this.frequency = frequency;
        this.name = name;
        this.recovery = recovery;
        this.repetition = repetition;
        this.series = series;
        this.usedWeight = usedWeight;
        this.intExId    =   this.aSerializer.createNewExercise(usedWeight,series,repetition,frequency,recovery,name);
        this.completed  =   false;
    }

    /**
     * constructors with id are used for loading instances from DB
     * @param eid
     * @param aSerializer
     * @param frequency
     * @param name
     * @param recovery
     * @param repetition
     * @param series
     * @param usedWeight
     */
    public Exercise(int eid,ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight,boolean completed) {
        this.aSerializer = aSerializer;
        this.frequency = frequency;
        this.name = name;
        this.recovery = recovery;
        this.repetition = repetition;
        this.series = series;
        this.usedWeight = usedWeight;
        this.intExId    =  eid;
        this.completed  =   completed;
    }

    /**
     * returns the execise frequency in hz
     * @return frequency
     */
    public int getFrequency() {
        return frequency;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        if(this.completed==false&&completed==true){
            this.completed = completed;
        }
    }

    /**
     * returns the exercise name
     * @return returns the name of the exercise
     */
    public String getName() {
        return name;
    }

    public int getRecovery() {
        return recovery;
    }

    public int getRepetition() {
        return repetition;
    }

    public int getSeries() {
        return series;
    }

    public String getUsedWeight() {
        return usedWeight;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "aSerializer=" + aSerializer +
                ", intExId=" + intExId +
                ", usedWeight='" + usedWeight + '\'' +
                ", series=" + series +
                ", repetition=" + repetition +
                ", frequency=" + frequency +
                ", recovery=" + recovery +
                ", name='" + name + '\'' +
                ", completed=" + completed +
                '}';
    }

    //TODO create user exercise from this one
}
