package org.androidtown.seobang_term_project.items;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */


/**
 * When:
 * This class is for covering the items of the recipe
 * Functions & Technique:
 * It has methods with name and Url for image which will be utilized from IngredientSelectActivity for checking states
 * This class is good for software architecture
 */

public class Recipe {

    private String name;
    private String url;

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

    public void setUrl(String url) {
        this.url = url;
    }
}
