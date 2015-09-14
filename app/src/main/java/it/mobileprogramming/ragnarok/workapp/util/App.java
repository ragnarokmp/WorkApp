package it.mobileprogramming.ragnarok.workapp.util;

import android.app.Application;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;

/**
 * Own implementation of Application with collects singletons.
 */
public class App extends Application {
    /**
     * SQLiteSerializer instance.
     */
    private SQLiteSerializer sqLiteSerializer;
    private User currentUser;                       //current user logged in the application
    private UserWorkout currentWO   =   null;       //current userWorkout active

    public UserWorkout getCurrentWO() {
        return currentWO;
    }

    public void setCurrentWO(UserWorkout currentWO) {
        this.currentWO = currentWO;
    }

    public WorkoutSession getCurrentWorkoutSession() {
        return currentWorkoutSession;
    }

    public void setCurrentWorkoutSession(WorkoutSession currentWorkoutSession) {
        this.currentWorkoutSession = currentWorkoutSession;
    }

    private WorkoutSession currentWorkoutSession;

    /*
     * Singleton for SQLiteSerializer.
     * @return the SQLiteSerializer instance.
     */
    public SQLiteSerializer getDBSerializer() {

        if (sqLiteSerializer == null) {
            sqLiteSerializer = new SQLiteSerializer(this, "workapp.db");
            sqLiteSerializer.open();
        }
        return sqLiteSerializer;
    }

    /**
     * Gets the current user of the application.
     * @return current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user of this application
     * @param currentUser current user of the app
     */
    public void setCurrentUser(User currentUser) {
        //TODO check with authentication!!! If leaved in this way is a security issue!
        this.currentUser = currentUser;
    }
}
