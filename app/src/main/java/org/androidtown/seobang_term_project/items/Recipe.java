package org.androidtown.seobang_term_project.items;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class Recipe {

    private String name;
    private String url;
    private String representUrl;

    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_basic_information.db";
    public SQLiteDatabase db;
    public Cursor cursor;


    public Recipe(String name, String url) {
        this.name = name;
        this.url=  url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getRepresentUrl(){
        return representUrl;
    }

    public void setRepresentUrl(String representUrl) {
        this.representUrl = representUrl;
    }


    public void setUrl(String url) {
        this.url = url;
    }
}
