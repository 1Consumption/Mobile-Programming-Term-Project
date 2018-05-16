package org.androidtown.seobang_term_project.items;

import java.util.List;

public class IngredientList {

    private String name;
    private List<Ingredient> ingredients;

    public IngredientList(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

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
