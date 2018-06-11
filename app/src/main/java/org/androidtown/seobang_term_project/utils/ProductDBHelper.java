package org.androidtown.seobang_term_project.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

/**
 * @When & @functions & @technique:
 * This database helper class is for optimizing the database functions
 * by implement or extends this class, code lines can be reduced which will be helpful for the structure
 */


public class ProductDBHelper extends SQLiteOpenHelper {

    public ProductDBHelper(Context context, String DB_Name) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}