package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * model class for Exercise, includes method for CRUD
 */
public class Exercise implements Parcelable{
    protected int intExId;
    protected ExerciseSerializer aSerializer;
    protected String  usedWeight;
    protected int     series;
    protected int     repetition;
    protected int     frequency;
    protected int     recovery;
    protected String  name;
    protected String  muscles;


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
    public Exercise(ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight,String muscles) {
        this.aSerializer = aSerializer;
        this.frequency = frequency;
        this.name = name;
        this.recovery = recovery;
        this.repetition = repetition;
        this.series = series;
        this.usedWeight = usedWeight;
        this.muscles    =   muscles;
        this.intExId    =   this.aSerializer.createNewExercise(series,repetition,frequency,recovery,name,muscles,usedWeight);
    }

    /**
     * constructors with id are used for loading instances from DB
     * @param eid id of exercise
     * @param aSerializer class serializing
     * @param frequency frequency of the exercise
     * @param name exercise name
     * @param recovery exercise recovery time
     * @param repetition number of repetitions
     * @param series number of series
     * @param usedWeight description of the used weight
     */
    public Exercise(int eid, ExerciseSerializer aSerializer, int frequency, String name, int recovery, int repetition, int series, String usedWeight, String muscles) {
        this.aSerializer = aSerializer;
        this.frequency = frequency;
        this.name = name;
        this.recovery = recovery;
        this.repetition = repetition;
        this.series = series;
        this.usedWeight = usedWeight;
        this.intExId    =  eid;
        this.muscles    =   muscles;
    }

    /**
     * default constructor requested by subclass
     */
    public Exercise(){
        super();
    }
    /**
     * clone the exercise
     * @return returns a new exercise
     */
    public Exercise clone(){
        return new Exercise(this.intExId, this.aSerializer, this.frequency, this.name, this.recovery, this.repetition, this.series, this.usedWeight, this.muscles);
    }
    /**
     * returns the execise frequency in hz
     * @return exercise
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * returns the exercise name
     * @return returns the name of the exercise
     */
    public String getName() {
        return name;
    }

    /**
     * get the recovery time of the exercise
     * @return int with recovery time
     */
    public int getRecovery() {
        return recovery;
    }

    /**
     * returns the number of repetition of exercise
     * @return number of repetitions
     */
    public int getRepetition() {
        return repetition;
    }

    /**
     * returns the number of series of exercise
     * @return number of series
     */
    public int getSeries() {
        return series;
    }

    /**
     * returns the description of the used weight
     * @return description of used weight
     */
    public String getUsedWeight() {
        return usedWeight;
    }

    /**
     * returns the description of the muscles involved in this exercise
     * @return description of the muscles involved in this exercise
     */
    public String getMuscles() {
        return muscles+"";
    }

    /**
     * returns the id of the exercise
     * @return exercise id
     */
    public int getId() {
        return intExId;
    }

    /**
     * set the frequency of the exercise and update DB
     * @param frequency exercise frequency
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
        aSerializer.updateExercise(this.intExId,this.usedWeight,this.series,this.repetition,this.frequency,this.recovery,this.name,this.muscles);
    }

    /**
     * set the muscles involved in this exercise and update DB
     * @param muscles string describing exercise
     */
    public void setMuscles(String muscles) {
        this.muscles = muscles;
        aSerializer.updateExercise(this.intExId,this.usedWeight,this.series,this.repetition,this.frequency,this.recovery,this.name,this.muscles);
    }

    /**
     * set the name of the exercise and update DB
     * @param name string name
     */
    public void setName(String name) {
        this.name = name;
        aSerializer.updateExercise(this.intExId,this.usedWeight,this.series,this.repetition,this.frequency,this.recovery,this.name,this.muscles);
    }

    /**
     * set recovery time of the exercise and update DB
     * @param recovery int recovery time
     */
    public void setRecovery(int recovery) {
        this.recovery = recovery;
        aSerializer.updateExercise(this.intExId,this.usedWeight,this.series,this.repetition,this.frequency,this.recovery,this.name,this.muscles);
    }

    /**
     * set the number of repetitions of the exercise and update DB
     * @param repetition in repetition number
     */
    public void setRepetition(int repetition) {
        this.repetition = repetition;
        aSerializer.updateExercise(this.intExId,this.usedWeight,this.series,this.repetition,this.frequency,this.recovery,this.name,this.muscles);
    }

    /**
     * set the number of series and update DB
     * @param series number of series
     */
    public void setSeries(int series) {
        this.series = series;
        aSerializer.updateExercise(this.intExId,this.usedWeight,this.series,this.repetition,this.frequency,this.recovery,this.name,this.muscles);
    }

    /**
     * set the description of the used weights and update DB
     * @param usedWeight description of weights
     */
    public void setUsedWeight(String usedWeight) {
        this.usedWeight = usedWeight;
        aSerializer.updateExercise(this.intExId,this.usedWeight,this.series,this.repetition,this.frequency,this.recovery,this.name,this.muscles);
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
                '}';
    }


    //TODO create user exercise from this one


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.intExId);
        dest.writeString(this.usedWeight);
        dest.writeInt(this.series);
        dest.writeInt(this.repetition);
        dest.writeInt(this.frequency);
        dest.writeInt(this.recovery);
        dest.writeString(this.name);
        dest.writeString(this.muscles);
    }

    protected Exercise(Parcel in) {
        this.intExId = in.readInt();
        this.usedWeight = in.readString();
        this.series = in.readInt();
        this.repetition = in.readInt();
        this.frequency = in.readInt();
        this.recovery = in.readInt();
        this.name = in.readString();
        this.muscles = in.readString();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
