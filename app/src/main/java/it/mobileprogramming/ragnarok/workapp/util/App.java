package it.mobileprogramming.ragnarok.workapp.util;

import android.app.Application;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;

/**
 * Own implementation of Application with collects singletons.
 */
public class App extends Application {
    /**
     * SQLiteSerializer instance.
     */
    private SQLiteSerializer sqLiteSerializer;

    /**
     * Singleton for SQLiteSerializer.
     * @return the SQLiteSerializer instance.
     */
    public SQLiteSerializer getDBSerializer() {

        if (sqLiteSerializer == null) {
            sqLiteSerializer = new SQLiteSerializer(this, "workapp.db");
        }
        return sqLiteSerializer;
    }
}
