package it.mobileprogramming.ragnarok.workapp;

import android.os.AsyncTask;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.Singletons;
import it.mobileprogramming.ragnarok.workapp.GymModel.Workout;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.util.App;

/**
 * Created by paride on 16/09/15.
 */
public class DefaultsLoader extends AsyncTask {
    App application;
    public DefaultsLoader(App application){
        super();
        this.application    =   application;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        System.out.println("DefaultsLoader starting loading defaults");
        SQLiteSerializer serializer1        =   application.getDBSerializer();
        ArrayList<Workout> allWorkouts      =   serializer1.loadAllWorkouts(true);
        ArrayList<Exercise> allExercises    =   serializer1.loadAll();
        if(allWorkouts.size()==0&&allExercises.size()>0) {
            System.out.println("DefaultsLoader empty workouts list and exercises present, loading defaults");
            Workout default1            =   new Workout("default beginner workout", Singletons.customString,"facile",serializer1);
            WorkoutSession session1     =   new WorkoutSession("",serializer1);
            WorkoutSession session2     =   new WorkoutSession("",serializer1);
            Exercise test1              =   new Exercise(serializer1,1,"Panca piana",30,2,2,"test","biceps");
            Exercise test2              =   new Exercise(serializer1,1,"Lat machine",30,2,3,"test","triceps");
            Exercise test3              =   new Exercise(serializer1,1,"Leg extension",30,1,2,"test","biceps");
            Exercise test4              =   new Exercise(serializer1,1,"Leg press",30,2,1,"test","triceps");
            session1.addExerciseToWorkoutSession(test1,0,true);
            session1.addExerciseToWorkoutSession(test2,1,true);
            session2.addExerciseToWorkoutSession(test3,0,true);
            session2.addExerciseToWorkoutSession(test4, 1, true);
            default1.addWorkoutSession(session1, true, 0);
            default1.addWorkoutSession(session2,true,1);

            default1            =   new Workout("default medium workout", Singletons.customString,"medio",serializer1);
            session1     =   new WorkoutSession("",serializer1);
            session2     =   new WorkoutSession("",serializer1);
            test1              =   new Exercise(serializer1,1,"Spinta ai cavi",20,2,1,"test","biceps");
            test2              =   new Exercise(serializer1,1,"Crunch",10,2,1,"test","triceps");
            test3              =   new Exercise(serializer1,1,"Trazioni alla sbarra",20,1,1,"test","biceps");
            test4              =   new Exercise(serializer1,1,"Vogatore",10,2,1,"test","triceps");
            session1.addExerciseToWorkoutSession(test1, 0, true);
            session1.addExerciseToWorkoutSession(test2, 1, true);
            session2.addExerciseToWorkoutSession(test3,0,true);
            session2.addExerciseToWorkoutSession(test4, 1, true);
            default1.addWorkoutSession(session1, true, 0);
            default1.addWorkoutSession(session2,true,1);

            default1            =   new Workout("default high workout", Singletons.customString,"high",serializer1);
            session1     =   new WorkoutSession("",serializer1);
            session2     =   new WorkoutSession("",serializer1);
            test1              =   new Exercise(serializer1,1,"Ellittica",20,2,1,"test","biceps");
            test2              =   new Exercise(serializer1,1,"Bici",10,2,1,"test","triceps");
            test3              =   new Exercise(serializer1,1,"Step",20,1,1,"test","biceps");
            test4              =   new Exercise(serializer1,1,"Calf machine",10,2,1,"test","triceps");
            session1.addExerciseToWorkoutSession(test1, 0, true);
            session1.addExerciseToWorkoutSession(test2, 1, true);
            session2.addExerciseToWorkoutSession(test3,0,true);
            session2.addExerciseToWorkoutSession(test4, 1, true);
            default1.addWorkoutSession(session1, true, 0);
            default1.addWorkoutSession(session2,true,1);

            default1            =   new Workout("default expert workout", Singletons.customString,"very high",serializer1);
            session1     =   new WorkoutSession("",serializer1);
            session2     =   new WorkoutSession("",serializer1);
            session1.addExerciseToWorkoutSession(test1, 0, true);
            session1.addExerciseToWorkoutSession(test2, 1, true);
            session2.addExerciseToWorkoutSession(test3,0,true);
            session2.addExerciseToWorkoutSession(test4, 1, true);
            default1.addWorkoutSession(session1, true, 0);
            default1.addWorkoutSession(session2,true,1);

            default1            =   new Workout("default pro workout", Singletons.customString,"pro",serializer1);
            session1     =   new WorkoutSession("",serializer1);
            session2     =   new WorkoutSession("",serializer1);
            session1.addExerciseToWorkoutSession(test1,0,true);
            session1.addExerciseToWorkoutSession(test2,1,true);
            session2.addExerciseToWorkoutSession(test3,0,true);
            session2.addExerciseToWorkoutSession(test4, 1, true);
            default1.addWorkoutSession(session1, true, 0);
            default1.addWorkoutSession(session2,true,1);

            allWorkouts   =  serializer1.loadAllWorkouts(true);
            System.out.println("DefaultsLoader check created workouts: "+allWorkouts.size());
            for(int i=0;i<allWorkouts.size();i++){
                ArrayList<WorkoutSession> sessions  =   allWorkouts.get(i).getWorkoutSessions();
                for(int j=0;j<sessions.size();j++){
                    ArrayList<Exercise> exercises   =   sessions.get(j).getExercisesOfSession();
                    System.out.println("DefaultsLoader workout "+i+" session "+j+" contains "+exercises.size()+"exercises");
                    for(int k=0;k<exercises.size();k++){
                        System.out.println("DefaultsLoader esercizio " + exercises.get(k) + " muscles " + exercises.get(k).getMuscles());
                    }
                }
            }
        }
        else{
            System.out.println("DefaultsLoader workout list not empty or exercise list empty, returning...");
        }
        return null;
    }
}
