package it.mobileprogramming.ragnarok.workapp;

import android.app.Application;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;

public class App extends Application {

    private SQLiteSerializer sqLiteSerializer;

    public SQLiteSerializer getDBSerializer() {
        if (sqLiteSerializer == null) {
            sqLiteSerializer = new SQLiteSerializer(this, "workapp.db");
        }
        return sqLiteSerializer;
    }
}
