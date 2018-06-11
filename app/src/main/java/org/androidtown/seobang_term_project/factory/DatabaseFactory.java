package org.androidtown.seobang_term_project.factory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.androidtown.seobang_term_project.utils.ProductDBHelper;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

/**
 * When:
 * This Activity is implemented from few activities which utilizes database
 * Functions & Technique:
 * By implementing this Activity, many code lines can be reduced
 * to optimize code structure for good software architecture
 */

public class DatabaseFactory {

    private static ProductDBHelper helper;

    public static SQLiteDatabase create(Context context, String DB_Name) {
        helper = new ProductDBHelper(context, DB_Name);
        return helper.getWritableDatabase();
    }
}
