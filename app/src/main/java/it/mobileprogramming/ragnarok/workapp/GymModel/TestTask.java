package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Collection;

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
        sqLiteSerializer.getAllUsers();
        sqLiteSerializer.createNewUser("Francesco","Totti","AS Roma",0,Singletons.formatFromString("27/09/1976"));
        sqLiteSerializer.getAllUsers();
        return null;
    }
}
