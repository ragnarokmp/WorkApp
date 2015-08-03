package it.mobileprogramming.ragnarok.workapp.util;

import android.app.Application;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;

/**
 * Own implementation of Application with collects singletons.
 */
public class App extends Application {
    /**
     * SQLiteSerializer instance.
     */
    private SQLiteSerializer sqLiteSerializer;
    private User currentUser;                   //current user logged in the application

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

    /**
     * gets the current user of the application
     * @return
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**#
     * sets the current user of this application
     * @param currentUser current user of the app
     */
    public void setCurrentUser(User currentUser) {
        //TODO check with authentication!!! If leaved in this way is a security issue!
        this.currentUser = currentUser;
    }
}
