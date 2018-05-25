package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.items.Recipe;
import org.androidtown.seobang_term_project.recycler.adapters.RecipeAdapter;
import org.androidtown.seobang_term_project.recycler.viewholders.RecipeViewHolder;
import org.androidtown.seobang_term_project.ui.recipe.RecipePreviewActivity;
import org.androidtown.seobang_term_project.utils.DBUtils;
import org.androidtown.seobang_term_project.utils.QuickSort;
import org.w3c.dom.Text;


public class RecipeFromIngredientActivity extends AppCompatActivity implements RecipeViewHolder.Delegate {
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "test_ingredient_3.db";
    public static final String TABLE_NAME = "recipe_ingredient_info";
    public static final String DB_Name_2 = "recipe_basic_information.db";
    public static final String TABLE_NAME_2 = "recipe_basic_information";

    public SQLiteDatabase db;
    public SQLiteDatabase db_info;
    public Cursor cursor;


    String[] recipeList = new String[6000];
    String[] tempList = new String[6000];

    int recipeLength = 0;
    int cnt = 1;
    int tempLength = 0;

    private RecipeAdapter mAdapter;

    private int index = 2;
    private boolean isLoading = false;

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
            cursor = db.rawQuery("SELECT recipe_code,weight FROM recipe_ingredient_info WHERE ingredient_type_name=\"주재료\" and ingredient_name=\"" + ingredient[i] + "\"", null); //쿼리문

            while (cursor.moveToNext()) {
                tempList[tempLength] = cursor.getString(0);
//                tempList[tempLength] += "a"+cursor.getString(1);
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
                    recipeList[recipeLength] = tempList[i] + "b" + String.valueOf(cnt);
                    cnt = 1;
                    recipeLength++;
                }
            }
        }

        for (int i = 0; i < recipeLength; i++) {
            for (int j = 0; j < recipeLength - 1 - i; j++) {
                if (Integer.parseInt(recipeList[j].substring(recipeList[j].indexOf("b") + 1)) < Integer.parseInt(recipeList[j + 1].substring(recipeList[j + 1].indexOf("b") + 1))) {
                    String a = recipeList[j];
                    recipeList[j] = recipeList[j + 1];
                    recipeList[j + 1] = a;
                }
            }
        }

        for (int i = 0; i < recipeLength; i++) {
            String frequency = recipeList[i].substring(recipeList[i].indexOf("b") + 1);
            recipeList[i] = recipeList[i].substring(0, recipeList[i].indexOf("b"));
            Log.e("RECIPELIST",recipeList[i]);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView_2);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mAdapter = new RecipeAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalItemCount, lastVisibleItem;
            int visibleThreshold = 4;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if ((index - 1) * 10 <= (lastVisibleItem + visibleThreshold)) {
                    loadMore();
                    isLoading = true;
                }
            }
        });

        for (int i = 0; i < 10; i++) {
            Log.e("iValue", recipeList[i]);
            mAdapter.addItem(new Recipe(getFoodName(recipeList[i]), getFoodPreviewImage(recipeList[i])));
        }
    }

    private void loadMore() {
        if ((index * 10) <= recipeLength) {
            for (int i = (index - 1) * 10; i < index * 10; i++) {
                mAdapter.addItem(new Recipe(getFoodName(recipeList[i]), getFoodPreviewImage(recipeList[i])));
            }
            index++;
        }
    }

    private String getFoodCode(String name) {
        cursor = db_info.rawQuery("SELECT recipe_code FROM " + TABLE_NAME_2 + " WHERE recipe_name=\"" + name + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        return cursor.getString(0);
    }

    private String getFoodName(String code) {
        cursor = db_info.rawQuery("SELECT recipe_name FROM " + TABLE_NAME_2 + " WHERE recipe_code=\"" + code + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        return cursor.getString(0);
    }

    private String getFoodPreviewImage(String code) {
        cursor = db_info.rawQuery("SELECT URL FROM " + TABLE_NAME_2 + " WHERE recipe_code=\"" + code + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        String url = cursor.getString(0);

        return url;
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(getApplicationContext(), RecipePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("selectedRecipe", getFoodCode(recipe.getName()));
        bundle.putString("RecipeName", recipe.getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        db_info.close();
    }
}
