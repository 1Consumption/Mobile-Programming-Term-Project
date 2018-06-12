package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.items.Recipe;
import org.androidtown.seobang_term_project.recycler.adapters.RecipeAdapter;
import org.androidtown.seobang_term_project.recycler.viewholders.RecipeViewHolder;
import org.androidtown.seobang_term_project.ui.recipe.RecipePreviewActivity;
import org.androidtown.seobang_term_project.utils.DBUtils;
import org.androidtown.seobang_term_project.utils.QuickSortArrayList;

import java.util.ArrayList;

/**
 * @When:
 * When receive the ingredients from ingredientselectactivity, this activity executed.
 *
 * @Functions
 * Calculate the weight of the ingredients received to create a matching cookbook.
 *
 * @Technique
 *  Use sqlite's query to retrieve it from the database.
 *  Different weights of different properties of ingredients, resulting in various results
 */


public class RecipeFromIngredientActivity extends BaseActivity implements RecipeViewHolder.Delegate {
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "ingredient_info_modify_weight_2.db";
    public static final String TABLE_NAME = "recipe_ingredient_info";
    public static final String DB_Name_2 = "recipe_basic_information_2.db";
    public static final String TABLE_NAME_2 = "recipe_basic_information";

    public Boolean loadingFlag = false;

    public SQLiteDatabase db;
    public SQLiteDatabase db_info;
    public Cursor cursor;

    ArrayList<String> recipeList = new ArrayList<>();
    ArrayList<String> tempList = new ArrayList<>();
    ArrayList<String> mapping = new ArrayList<>();
    ArrayList<String> waterWeight = new ArrayList<>();
    ArrayList<String> yuksuWeight = new ArrayList<>();
    ArrayList<Cursor> cursorList = new ArrayList<>();

    int cnt = 0;

    double totalWeight = 0;

    private RecipeAdapter mAdapter;

    private int index = 2;
    private boolean isLoading = false;

    private int accuracy = 0;

    RecyclerView rec;
    TextView text;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe_from_ingredient);

        setRefreshView();

        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name_2);


        db = DatabaseFactory.create(this, DB_Name);
        db_info = DatabaseFactory.create(this, DB_Name_2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String received = bundle.getString("result");
        String[] ingredient = received.split(",");


        rec = findViewById(R.id.recyclerView_2);
        text = findViewById(R.id.noList_recipe);
        img = findViewById(R.id.sadImg);

        calTotal();

        SharedPreferences preferences = getSharedPreferences("Accuracy", MODE_PRIVATE);
        accuracy = preferences.getInt("Accuracy", 50);


        for (int i = 0; i < ingredient.length; i++) {
            cursor = db.rawQuery("SELECT recipe_code,weight FROM recipe_ingredient_info WHERE ingredient_name=\"" + ingredient[i] + "\"", null); //쿼리문

            while (cursor.moveToNext()) {
                String temp = cursor.getString(0);
                temp += "w" + cursor.getString(1);
                tempList.add(temp);
            }
        }
        QuickSortArrayList.sort(tempList, 0, tempList.size() - 1);


        if (tempList.size() == 1) {
            for (int i = 0; i < mapping.size(); i++) {
                String mappedRecipeCode = mapping.get(i).substring(0, mapping.get(i).indexOf(","));
                Double mappedTotal = Double.parseDouble(mapping.get(i).substring(mapping.get(i).indexOf(",") + 1));

                if (tempList.get(0).substring(0, tempList.get(0).indexOf("w")).equals(mappedRecipeCode)) {
                    recipeList.add(tempList.get(0) + "t" + String.valueOf(mappedTotal));
                }
            }
        } else {
            for (int i = 0; i < tempList.size(); i++) {
                if (i + 1 == tempList.size())
                    break;
                else {
                    String curRecipeCode = tempList.get(i).substring(0, tempList.get(i).indexOf("w"));
                    String nextRecipeCode = tempList.get(i + 1).substring(0, tempList.get(i + 1).indexOf("w"));

                    Double curWeight = Double.parseDouble(tempList.get(i).substring(tempList.get(i).indexOf("w") + 1));
                    Double nextWeight = Double.parseDouble(tempList.get(i + 1).substring(tempList.get(i + 1).indexOf("w") + 1));


                    if (curRecipeCode.equals(nextRecipeCode)) {
                        if (cnt == 0) {
                            totalWeight = curWeight + nextWeight;
                        } else {
                            totalWeight += nextWeight;
                        }
                        cnt++;
                    } else {
                        if (cnt == 0) {
                            for (int j = 0; j < mapping.size(); j++) {
                                String mappedRecipeCode = mapping.get(j).substring(0, mapping.get(j).indexOf(","));
                                Double mappedTotal = Double.parseDouble(mapping.get(j).substring(mapping.get(j).indexOf(",") + 1));

                                if (curRecipeCode.equals(mappedRecipeCode)) {
                                    recipeList.add(tempList.get(i) + "t" + String.valueOf(mappedTotal));
                                    break;
                                }
                            }

                        } else {
                            for (int j = 0; j < mapping.size(); j++) {
                                String mappedRecipeCode = mapping.get(j).substring(0, mapping.get(j).indexOf(","));
                                Double mappedTotal = Double.parseDouble(mapping.get(j).substring(mapping.get(j).indexOf(",") + 1));

                                if (curRecipeCode.equals(mappedRecipeCode)) {
                                    recipeList.add(tempList.get(i).substring(0, tempList.get(i).indexOf("w")) + "w" + String.valueOf(totalWeight) + "t" + String.valueOf(mappedTotal));
                                    break;
                                }
                            }
                        }
                        cnt = 0;
                        totalWeight = 0;
                    }
                }
            }
        }

        getWaterWeight();
        getYuksuWeight();
        plusWaterWeight();
        plusYuksuWeight();

        for (int i = 0; i < recipeList.size(); i++) {
            double weight = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf("w") + 1, recipeList.get(i).indexOf("t")));
            double total = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf("t") + 1));
            String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf("w"));
            recipeList.set(i, recipeCode + "," + String.valueOf((weight / total) * 100.0));
        }


        for (int i = 0; i < recipeList.size(); i++) {
            for (int j = 0; j < recipeList.size() - 1 - i; j++) {
                if (Double.parseDouble(recipeList.get(j).substring(recipeList.get(j).indexOf(",") + 1)) < Double.parseDouble(recipeList.get(j + 1).substring(recipeList.get(j + 1).indexOf(",") + 1))) {
                    String a = recipeList.get(j);
                    recipeList.set(j, recipeList.get(j + 1));
                    recipeList.set(j + 1, a);
                }
            }
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

        int percentCount = 0;

        for (int i = 0; i < recipeList.size(); i++) {
            Double percent = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf(",") + 1));
            if (percent > accuracy) {
                percentCount++;
            }
        }

        if (recipeList.size() < 10) {
            for (int i = 0; i < recipeList.size(); i++) {

                Double percent = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf(",") + 1));
                String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf(","));
                if (percent > accuracy)
                    mAdapter.addItem(new Recipe(getFoodName(recipeCode), getFoodPreviewImage(recipeCode)));
                else
                    continue;
            }
        } else {
            for (int i = 0; i < 10; i++) {
                Double percent = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf(",") + 1));
                String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf(","));
                if (percent >= accuracy)
                    mAdapter.addItem(new Recipe(getFoodName(recipeCode), getFoodPreviewImage(recipeCode)));
                else
                    continue;
            }
        }

        loadingFlag = true;

        if (percentCount != 0) {
            rec.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.noRecipeLayout).setVisibility(View.VISIBLE);
        }
    }

    private void loadMore() {
        if ((index * 10) <= recipeList.size()) {
            if ((index + 1) * 10 > recipeList.size()) {
                for (int i = (index - 1) * 10; i < recipeList.size(); i++) {
                    Double percent = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf(",") + 1));
                    String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf(","));
                    if (percent >= accuracy)
                        mAdapter.addItem(new Recipe(getFoodName(recipeCode), getFoodPreviewImage(recipeCode)));
                    else
                        continue;
                }
            } else {
                for (int i = (index - 1) * 10; i < index * 10; i++) {
                    Double percent = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf(",") + 1));
                    String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf(","));
                    if (percent >= accuracy)
                        mAdapter.addItem(new Recipe(getFoodName(recipeCode), getFoodPreviewImage(recipeCode)));
                    else
                        continue;
                }
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
        cursorList.add(db_info.rawQuery("SELECT recipe_name FROM " + TABLE_NAME_2 + " WHERE recipe_code=\"" + code + "\"", null));
        Cursor temp = cursorList.get(0);
        temp.moveToNext();
        String name = temp.getString(0);
        temp.close();
        cursorList.remove(0);
        return name;
    }

    private String getFoodPreviewImage(String code) {
        cursor = db_info.rawQuery("SELECT URL FROM " + TABLE_NAME_2 + " WHERE recipe_code=\"" + code + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        String url = cursor.getString(0);

        return url;
    }

    @Override
    public void onItemClick(Recipe recipe, View view) {
        Intent intent = new Intent(getApplicationContext(), RecipePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("selectedRecipe", getFoodCode(recipe.getName()));
        bundle.putString("RecipeName", recipe.getName());
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view,
                    ViewCompat.getTransitionName(view));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        db_info.close();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name_2);
        db = DatabaseFactory.create(this, DB_Name);
        db_info = DatabaseFactory.create(this, DB_Name_2);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void calTotal() {
        ArrayList<Cursor> tempCursorList = new ArrayList<>();
        tempCursorList.add(db.rawQuery("SELECT recipe_code,sum(weight) FROM " + TABLE_NAME + " GROUP BY recipe_code", null));
        Cursor tempCursor = tempCursorList.get(0);

        while (tempCursor.moveToNext()) {
            mapping.add(tempCursor.getString(0) + "," + tempCursor.getString(1));
        }

        tempCursor.close();
        tempCursorList.remove(0);
    }

    public void getWaterWeight() {
        cursor = db.rawQuery("SELECT recipe_code,weight FROM " + TABLE_NAME + " WHERE ingredient_name=\"" + "물" + "\"", null);
        while (cursor.moveToNext()) {
            String temp = cursor.getString(0);
            temp += "," + cursor.getString(1);
            waterWeight.add(temp);
        }
    }

    public void getYuksuWeight() {
        cursor = db.rawQuery("SELECT recipe_code,weight FROM " + TABLE_NAME + " WHERE ingredient_name=\"" + "육수" + "\"", null);
        while (cursor.moveToNext()) {
            String temp = cursor.getString(0);
            temp += "," + cursor.getString(1);
            yuksuWeight.add(temp);
        }
    }

    public void plusWaterWeight() {
        for (int i = 0; i < recipeList.size(); i++) {
            String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf("w"));
            Double recipeWeight = Double.parseDouble(recipeList.get(i).substring((recipeList.get(i).indexOf("w") + 1), recipeList.get(i).indexOf("t")));
            String recipeTotal = recipeList.get(i).substring(recipeList.get(i).indexOf("t") + 1);
            for (int j = 0; j < waterWeight.size(); j++) {
                String[] weight = waterWeight.get(j).split(",");
                String waterRecipeCode = weight[0];
                Double waterWeight = Double.parseDouble(weight[1]);

                if (waterRecipeCode.equals(recipeCode)) {
                    recipeWeight += waterWeight;
                    String newRecipe = recipeCode + "w" + String.valueOf(recipeWeight) + "t" + recipeTotal;
                    recipeList.set(i, newRecipe);
                    break;
                }

            }
        }
    }

    public void plusYuksuWeight() {
        for (int i = 0; i < recipeList.size(); i++) {
            String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf("w"));
            Double recipeWeight = Double.parseDouble(recipeList.get(i).substring((recipeList.get(i).indexOf("w") + 1), recipeList.get(i).indexOf("t")));
            String recipeTotal = recipeList.get(i).substring(recipeList.get(i).indexOf("t") + 1);

            for (int j = 0; j < yuksuWeight.size(); j++) {
                String[] weight = yuksuWeight.get(j).split(",");
                String yuksuRecipeCode = weight[0];
                Double yuksuWeight = Double.parseDouble(weight[1]);

                if (yuksuRecipeCode.equals(recipeCode)) {
                    recipeWeight += yuksuWeight;
                    String newRecipe = recipeCode + "w" + String.valueOf(recipeWeight) + "t" + recipeTotal;
                    recipeList.set(i, newRecipe);
                    break;
                }

            }
        }
    }
}
