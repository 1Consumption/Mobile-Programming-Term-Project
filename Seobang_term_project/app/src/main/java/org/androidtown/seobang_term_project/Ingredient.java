package org.androidtown.seobang_term_project;

import android.app.Activity;

public class Ingredient extends Activity {
    private String meat, fish, vege;

    public Ingredient(){

    }

    public Ingredient(String meat, String fish, String vege){
        this.meat = meat;
        this.fish = fish;
        this.vege = vege;
    }

    public String getMeat(){
        return meat;
    }

    public void setMeat(String selectedMeat){
        this.meat = selectedMeat;
    }

    public String getFish(){
        return fish;
    }

    public void setFish(String selectedFish){
        this.fish = selectedFish;
    }

    public String getVege(){
        return vege;
    }

    public void setVege(String selectedVege){
        this.vege = selectedVege;
    }
}
