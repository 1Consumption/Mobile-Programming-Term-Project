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
import android.util.Log;
import android.view.View;
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


public class RecipeFromIngredientActivity extends BaseActivity implements RecipeViewHolder.Delegate {
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "test_ingredient_4.db";
    public static final String TABLE_NAME = "recipe_ingredient_info";
    public static final String DB_Name_2 = "recipe_basic_information.db";
    public static final String TABLE_NAME_2 = "recipe_basic_information";

    public SQLiteDatabase db;
    public SQLiteDatabase db_info;
    public Cursor cursor;

    ArrayList<String> recipeList = new ArrayList<>();
    ArrayList<String> tempList = new ArrayList<>();
    ArrayList<String> mapping = new ArrayList<>();

    int recipeLength = 0;
    int cnt = 0;

    double totalWeight = 0;

    private RecipeAdapter mAdapter;

    private int index = 2;
    private boolean isLoading = false;

    private int accuracy = 0;

    RecyclerView rec;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe_from_ingredient);
        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name_2);

        Log.e("RecipeFromIngredient", "onCreate");

        db = DatabaseFactory.create(this, DB_Name);
        db_info = DatabaseFactory.create(this, DB_Name_2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String received = bundle.getString("result");
        String[] ingredient = received.split(",");

        rec = findViewById(R.id.recyclerView_2);
        text = findViewById(R.id.noList_recipe);

        calTotal();

        SharedPreferences preferences = getSharedPreferences("Accuracy", MODE_PRIVATE);
        accuracy = preferences.getInt("Accuracy", 50);

        for (int i = 0; i < ingredient.length; i++)
            Log.e("RecipeFromIngredient", "\"" + ingredient[i] + "\"");


        for (int i = 0; i < ingredient.length; i++) {
            cursor = db.rawQuery("SELECT recipe_code,weight FROM recipe_ingredient_info WHERE ingredient_name=\"" + ingredient[i] + "\"", null); //쿼리문

            while (cursor.moveToNext()) {
                String temp = cursor.getString(0);
                temp += "w" + cursor.getString(1);
                tempList.add(temp);
                Log.e("zzz", "zzzz");
            }
        }
        QuickSortArrayList.sort(tempList, 0, tempList.size() - 1);

        for (int i = 0; i < tempList.size(); i++)
            Log.e("test", tempList.get(i));


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
                                }
                            }

                        } else {
                            for (int j = 0; j < mapping.size(); j++) {
                                String mappedRecipeCode = mapping.get(j).substring(0, mapping.get(j).indexOf(","));
                                Double mappedTotal = Double.parseDouble(mapping.get(j).substring(mapping.get(j).indexOf(",") + 1));

                                if (curRecipeCode.equals(mappedRecipeCode)) {
                                    recipeList.add(tempList.get(i).substring(0, tempList.get(i).indexOf("w")) + "w" + String.valueOf(totalWeight) + "t" + String.valueOf(mappedTotal));
                                }
                            }
                        }
                        cnt = 0;
                        totalWeight = 0;
                    }
                }
            }
        }

        for (int i = 0; i < recipeList.size(); i++)
            Log.e("도출스", recipeList.get(i));

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

        if (recipeLength < 10) {
            for (int i = 0; i < recipeList.size(); i++) {
                //Log.e("iValue", recipeList.get(i));
                Double percent = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf(",") + 1));
                String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf(","));
                if (percent > accuracy)
                    mAdapter.addItem(new Recipe(getFoodName(recipeCode), getFoodPreviewImage(recipeCode)));
                else
                    continue;
            }
        } else {
            for (int i = 0; i < 10; i++) {
//                Log.e("iValue", recipeList.get(i));
                Double percent = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf(",") + 1));
                String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf(","));
                if (percent > accuracy)
                    mAdapter.addItem(new Recipe(getFoodName(recipeCode), getFoodPreviewImage(recipeCode)));
                else
                    continue;
            }
        }

        if (percentCount != 0) {
            text.setVisibility(View.INVISIBLE);
            rec.setVisibility(View.VISIBLE);
        }
    }

    private void loadMore() {
        if ((index * 10) <= recipeList.size()) {
            for (int i = (index - 1) * 10; i < index * 10; i++) {
                Double percent = Double.parseDouble(recipeList.get(i).substring(recipeList.get(i).indexOf(",") + 1));
                String recipeCode = recipeList.get(i).substring(0, recipeList.get(i).indexOf(","));
                if (percent > accuracy)
                    mAdapter.addItem(new Recipe(getFoodName(recipeCode), getFoodPreviewImage(recipeCode)));
                else
                    continue;
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
        Cursor cursor = db_info.rawQuery("SELECT recipe_name FROM " + TABLE_NAME_2 + " WHERE recipe_code=\"" + code + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        String name = cursor.getString(0);

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
        Log.e("디스트로이", "ㅁㄴㅇㅁㄴㅇㅁㄴㅇ");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name_2);
        db = DatabaseFactory.create(this, DB_Name);
        db_info = DatabaseFactory.create(this, DB_Name_2);
        Log.e("RecipeFromIngredient", "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("RecipeFromIngredient", "onResume");
    }

    public void calTotal() {
        cursor = db.rawQuery("SELECT recipe_code,count(*) FROM " + TABLE_NAME + " group by recipe_code order by count(*) desc", null);
        while (cursor.moveToNext()) {
            String temp = cursor.getString(0);
            temp += "," + cursor.getString(1);
            mapping.add(temp);
        }
        Log.e("totalCount", String.valueOf(mapping.size()));
    }
}
