package org.androidtown.seobang_term_project.items;

public class Ingredient {

    private String ingredientType;
    private int image;

    public Ingredient(String ingredientType, int image) {
        this.ingredientType = ingredientType;
        this.image = image;
    }

    public String getIngredientType() {
        return ingredientType;
    }

    public void setIngredientType(String ingredientType) {
        this.ingredientType = ingredientType;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
