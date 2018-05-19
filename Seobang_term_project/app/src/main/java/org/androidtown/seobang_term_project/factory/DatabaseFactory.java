package org.androidtown.seobang_term_project.factory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.androidtown.seobang_term_project.utils.ProductDBHelper;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class DatabaseFactory {

    private static ProductDBHelper helper;

    public static SQLiteDatabase create(Context context, String DB_Name) {
        helper = new ProductDBHelper(context, DB_Name);
        return helper.getWritableDatabase();
    }
}
