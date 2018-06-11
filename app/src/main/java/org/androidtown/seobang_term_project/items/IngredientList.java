package org.androidtown.seobang_term_project.items;

import java.util.List;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

/**
 * @When:
 * This data model class is for covering the lists of categorizing
 *
 * @Functions & Technique:
 * It has methods which will be utilized from {@link org.androidtown.seobang_term_project.recycler.viewholders.IngredientListViewHolder} for checking states
 * This class is good for software architecture
 */

public class IngredientList {

    private String name;
    private List<Ingredient> ingredients;

    public IngredientList(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }


    /*********************
     * getters & setters *
     ********************/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
