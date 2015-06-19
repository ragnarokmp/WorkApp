package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by paride on 19/06/15.
 */
public class GymSQLiteHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "workapp.db";
    private static final int DATABASE_VERSION = 1;

    /**#
     * automatic generation/update of SQLite DB, for further info please refer to https://github.com/jgilfelt/android-sqlite-asset-helper
     * @param context
     */
    public GymSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}

