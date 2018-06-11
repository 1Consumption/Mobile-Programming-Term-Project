package org.androidtown.seobang_term_project.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * @When:
 * This class is for opening SQLite
 *
 * @functions:
 * By helping opening SQLite, reduce redundant code and make good software architecture
 *
 * @technique:
 * By utilizing the activity life cycle, helps opening the database
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table frequency (" +
                "id text, " +
                "frequency integer,"+
                "date text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists frequency";
        db.execSQL(sql);
        onCreate(db);
    }
}
