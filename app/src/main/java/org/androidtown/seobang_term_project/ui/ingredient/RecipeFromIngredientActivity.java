package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.utils.DBUtils;
import org.androidtown.seobang_term_project.utils.QuickSort;
import org.w3c.dom.Text;


public class RecipeFromIngredientActivity extends AppCompatActivity {
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "test_ingredient.db";
    public static final String TABLE_NAME = "recipe_ingredient_info";
    public static final String DB_Name_2 = "recipe_basic_information.db";
    public static final String TABLE_NAME_2 = "recipe_basic_information";

    public SQLiteDatabase db;
    public SQLiteDatabase db_info;
    public Cursor cursor;


    String[] recipeList = new String[2000];
    String[] tempList = new String[2000];

    int recipeLength = 0;
    int cnt = 1;
    int tempLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_from_ingredient);
        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name_2);

        db = DatabaseFactory.create(this, DB_Name);
        db_info = DatabaseFactory.create(this, DB_Name_2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        TextView text = findViewById(R.id.ingredientSelected);

        String received = bundle.getString("result");
        String[] ingredient = received.split(",");

        String temp = "";

        for (int i = 0; i < ingredient.length; i++)
            Log.e("RecipeFromIngredient", "\"" + ingredient[i] + "\"");

        int tempNum = 0;

        for (int i = 0; i < ingredient.length; i++) {
            cursor = db.rawQuery("SELECT count(recipe_code) FROM recipe_ingredient_info WHERE ingredient_type_name=\"주재료\" and ingredient_name=\"" + ingredient[tempNum] + "\"", null);
            tempNum++;
            cursor.moveToNext();
            temp += cursor.getString(0) + "\n\n";
            cursor = db.rawQuery("SELECT recipe_code FROM recipe_ingredient_info WHERE ingredient_type_name=\"주재료\" and ingredient_name=\"" + ingredient[i] + "\"", null); //쿼리문

            while (cursor.moveToNext()) {
                tempList[tempLength] = cursor.getString(0);
                tempLength++;
            }
        }
        QuickSort.sort(tempList, 0, tempLength - 1);

        for (int i = 0; i < tempLength; i++) {
            if (i + 1 == tempLength)
                break;
            else {
                if (tempList[i].equals(tempList[i + 1]))
                    cnt++;
                else {
                    recipeList[recipeLength] = tempList[i] + "." + String.valueOf(cnt);
                    cnt = 1;
                    recipeLength++;
                }
            }
        }

        for (int i = 0; i < recipeLength; i++) {
            for (int j = 0; j < recipeLength - 1 - i; j++) {
                if (Integer.parseInt(recipeList[j].substring(recipeList[j].indexOf(".") + 1)) < Integer.parseInt(recipeList[j + 1].substring(recipeList[j + 1].indexOf(".") + 1))) {
                    String a = recipeList[j];
                    recipeList[j] = recipeList[j + 1];
                    recipeList[j + 1] = a;
                }
            }
        }

        for (int i = 0; i < recipeLength; i++) {
            String frequency = recipeList[i].substring(recipeList[i].indexOf(".") + 1);
            recipeList[i] = recipeList[i].substring(0, recipeList[i].indexOf("."));
            text.append(recipeList[i] + "      " + frequency + "번\n");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        db_info.close();
    }
}
