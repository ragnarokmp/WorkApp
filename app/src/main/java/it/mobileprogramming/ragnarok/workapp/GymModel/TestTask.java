package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.content.Context;
import android.os.AsyncTask;
/**
 * Created by paride on 20/06/15.
 * testing task for all classes in data model
 */
public class TestTask extends AsyncTask {
    Context myContext;
    public TestTask(Context aContext){
        this.myContext  =   aContext;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        System.out.println("Hello, i'm a testing Asynctask!");
        SQLiteSerializer    sqLiteSerializer    =   new SQLiteSerializer(this.myContext,"workapp.db");
        sqLiteSerializer.open();
        System.out.println("LOADING USERS");
        sqLiteSerializer.getAllUsers();
        System.out.println("NEW USER");
        sqLiteSerializer.createNewUser("Francesco", "Totti", "AS Roma", 0, Singletons.formatFromString("27/09/1976"));
        sqLiteSerializer.getAllUsers();
        for(int i=2;i<129;i++){
            sqLiteSerializer.deleteWorkout(i);
        }
        sqLiteSerializer.loadAllWorkouts(false);
        sqLiteSerializer.close();
        return null;
    }
}
