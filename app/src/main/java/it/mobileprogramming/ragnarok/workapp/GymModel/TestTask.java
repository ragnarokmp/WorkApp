package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

//TODO rimuovere stammerda in fase di consegna
/**
 * Created by paride on 20/06/15.
 * testing task for all classes in data model
 * MONNEZZA!!!!
 */
public class TestTask extends AsyncTask implements TextToSpeech.OnInitListener {

    Context myContext;
    public TestTask(Context aContext){
        this.myContext  =   aContext;
    }
    public TextToSpeech textToSpeech;
    @Override
    protected Object doInBackground(Object[] params) {
        textToSpeech    =  new TextToSpeech(this.myContext,this);

/*
        System.out.println("Hello, i'm a testing Asynctask!");
        SQLiteSerializer    sqLiteSerializer    =   new SQLiteSerializer(this.myContext,"workapp.db");
        sqLiteSerializer.open();
        System.out.println("LOADING USERS");
        ArrayList<User> allUsers    =   sqLiteSerializer.getAllUsers();
        System.out.println("NEW USER");
        sqLiteSerializer.createNewUser("Francesco", "Totti", "AS Roma", 0, Singletons.formatFromString("27/09/1976"));
        sqLiteSerializer.getAllUsers();
        sqLiteSerializer.createNewWorkout("Workout di test", "tipo pro", "pippa");
        //for(int i=2;i<129;i++){
        //    sqLiteSerializer.deleteWorkout(i);
        //}
        ArrayList<Workout> workouts =   sqLiteSerializer.loadAllWorkouts(false);
        for(int i=0;i<workouts.size();i++){
            System.out.println(workouts.get(i).toString());
            ArrayList<WorkoutSession> sessions  =   workouts.get(i).getWorkoutSessions();
            System.out.println("Workout composto da " + sessions.size() + " sessioni");
            for(int k=0;k<sessions.size();k++){
                System.out.println(sessions.get(k).toString());
            }
        }
        for(int i=0;i<allUsers.size();i++){
            System.out.println(allUsers.get(i).toString());
            ArrayList<UserWorkout> wouts    =   sqLiteSerializer.loadWorkoutsForUser(allUsers.get(i).getIntUserID());
            System.out.println("User "+allUsers.get(i).getIntUserID()+" has "+wouts.size()+" workouts");
            for(int j=0;j<wouts.size();j++){
                System.out.println(wouts.get(j).toString());
                ArrayList<UserWorkoutSession> userWorkoutSessions   =   wouts.get(j).getWoSessions();
                for (int k=0;k<userWorkoutSessions.size();k++){
                    System.out.println("UserWorkoutSession: "+userWorkoutSessions.get(k).toString());
                }
            }
            ArrayList<WeightItem> wHistory  =   sqLiteSerializer.loadWeightHistory(allUsers.get(i).getIntUserID());
            for(int j=0;j<wHistory.size();j++){
                System.out.println(wHistory.get(j).toString());
            }
        }
        Exercise anExercise =   new Exercise(sqLiteSerializer,10,"nuovo",3,4,3,"pippo","schiena");
        System.out.println("ID nuovo esercizio " + anExercise.getId());
        Exercise load   =   sqLiteSerializer.loadExercise(anExercise.getId());
        System.out.println("Risultato caricamento ");
        System.out.println(load.toString());
        User aUser  =   sqLiteSerializer.loadUser(1);
        Date    d=   new Date();
        UserExercise userExcercise    =   new UserExercise(aUser.getIntUserID(),d,load,false,"stocazzo",sqLiteSerializer,sqLiteSerializer);
        WorkoutSession aSession =   new WorkoutSession("pippo",sqLiteSerializer);
        System.out.println("Created new WO Session " + aSession.getId());
        WorkoutSession loaded   =   sqLiteSerializer.loadWorkoutSession(aSession.getId());
        System.out.println("CARICATO " + loaded.getFilepath() + " " + loaded.getId());
        UserWorkoutSession  uwSession    =   new UserWorkoutSession(loaded.getFilepath(),1,sqLiteSerializer,sqLiteSerializer,new Date(),loaded.getId(),"pippo",false);
        System.out.println("ID SESSIONE " + uwSession.getId());
        sqLiteSerializer.addSessiontoWorkout(1, uwSession.getId(), 4);
        ArrayList<WorkoutSession> wo1sessions   =   sqLiteSerializer.loadAllWorkoutSessionsForWorkout(1);
        for(int i=0;i<wo1sessions.size();i++){
            System.out.println("SESSION LOADED: " + wo1sessions.get(i).toString());
        }
        User user1  =   sqLiteSerializer.loadUser(1);
        System.out.println(user1.toString());
        ArrayList<UserWorkout> allWO    =   sqLiteSerializer.loadWorkoutsForUser(1);
        for(int i=0;i<allWO.size();i++){
            ArrayList<UserWorkoutSession> mySessions    =   sqLiteSerializer.loadAllSessionsForUserWorkout(1, allWO.get(i).getIntWOID());
            for(int j=0;j<mySessions.size();j++){
                System.out.println("SESSIONE: " + mySessions.get(j).toString());
                ArrayList<UserExercise>    exercises = sqLiteSerializer.getExercisesOfAUserSession(mySessions.get(j).getId(),1,Singletons.formatFromString("15/01/2015"));
                System.out.println("Size array " + exercises.size());
                for(int k=0;k<exercises.size();k++){
                    System.out.println(exercises.get(k).toString());
                    exercises.get(k).setComment("cambiato commento");
                    exercises.get(k).setDone(true);
                    System.out.println(exercises.get(k).toString());
                   // sqLiteSerializer.deleteUserExercise(exercises.get(k).getMyUserID(),exercises.get(k).getId(),exercises.get(k).getExerciseDate());
                }
                sqLiteSerializer.updateSession(mySessions.get(j).getId(), mySessions.get(j).getDateSessionDate(), "stocazzissimo", mySessions.get(j).getUserId());
                //sqLiteSerializer.deleteSession(mySessions.get(j).getId(),mySessions.get(j).getUserId(),mySessions.get(j).getDateSessionDate());
            }
            //sqLiteSerializer.removeWorkoutForUser(1,allWO.get(i).getIntWOID());
        }
        sqLiteSerializer.updateUser(1, "pippo", "calzetta", 1, new Date());
        User test   =   sqLiteSerializer.loadUser(1);
        System.out.println(test.toString());
        //sqLiteSerializer.deleteUser(1);
        sqLiteSerializer.close();*/
        return null;
    }

    @Override
    public void onInit(int status) {
        textToSpeech.setLanguage(Locale.ITALIAN);
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP){
            //textToSpeech.speak("Benvenuto in work app, pronto a muovere le chiappe? Per non fare schifo devi spingere come un maledetto!", TextToSpeech.QUEUE_ADD, null);
        }
        else{
            //textToSpeech.speak("Benvenuto in work app, pronto a muovere le chiappe? Per non fare schifo devi spingere come un maledetto!", TextToSpeech.QUEUE_ADD, null,"pippo");
            /*textToSpeech.setSpeechRate((float) 0.5);
            textToSpeech.speak("E se mi gira ti posso far andare molto pianoooooooooo", TextToSpeech.QUEUE_ADD, null, "pippo");
            textToSpeech.setSpeechRate((float) 3);
            textToSpeech.speak("Oppure molto forte!", TextToSpeech.QUEUE_ADD, null, "pippo");
            textToSpeech.setSpeechRate(1);
            textToSpeech.speak("Un solo grido! Un solo allarme! Milano in fiamme! Milano in fiamme!", TextToSpeech.QUEUE_ADD, null, "pippo");
            */}
      }
}
