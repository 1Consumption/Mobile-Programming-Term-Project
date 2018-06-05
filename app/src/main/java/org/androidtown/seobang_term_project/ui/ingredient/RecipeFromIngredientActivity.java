package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Intent;
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

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.items.Recipe;
import org.androidtown.seobang_term_project.recycler.adapters.RecipeAdapter;
import org.androidtown.seobang_term_project.recycler.viewholders.RecipeViewHolder;
import org.androidtown.seobang_term_project.ui.Accuracy.AccuracyActivity;
import org.androidtown.seobang_term_project.ui.recipe.RecipePreviewActivity;
import org.androidtown.seobang_term_project.utils.DBUtils;
import org.androidtown.seobang_term_project.utils.QuickSort;


public class RecipeFromIngredientActivity extends BaseActivity implements RecipeViewHolder.Delegate {
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
    String[] mapping = new String[600];

    int mapCount = 0;
    int recipeLength = 0;
    int cnt = 0;
    int tempLength = 0;

    double totalWeight = 0;

    private RecipeAdapter mAdapter;

    private int index = 2;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe_from_ingredient);
        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name_2);

        db = DatabaseFactory.create(this, DB_Name);
        db_info = DatabaseFactory.create(this, DB_Name_2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String received = bundle.getString("result");
        String[] ingredient = received.split(",");

        String temp = "";

        calTotal();
//        for (int i = 0; i < mapCount; i++) {
//            Log.e("test", mapping[i]);
//        }
        AccuracyActivity test=new AccuracyActivity();
        Log.e("accuracy", String.valueOf(test.getAccuracy()));

        for (int i = 0; i < ingredient.length; i++)
            Log.e("RecipeFromIngredient", "\"" + ingredient[i] + "\"");


        for (int i = 0; i < ingredient.length; i++) {
            cursor = db.rawQuery("SELECT recipe_code,weight FROM recipe_ingredient_info WHERE ingredient_name=\"" + ingredient[i] + "\"", null); //쿼리문

            while (cursor.moveToNext()) {
                tempList[tempLength] = cursor.getString(0);
                tempList[tempLength] += "w" + cursor.getString(1);
                tempLength++;
            }
        }
        QuickSort.sort(tempList, 0, tempLength - 1);

//        for (int i = 0; i < tempLength; i++)
//            Log.e("test", tempList[i]);


        for (int i = 0; i < tempLength; i++) {
            if (i + 1 == tempLength)
                break;
            else {
                String curRecipeCode = tempList[i].substring(0, tempList[i].indexOf("w"));
                String nextRecipeCode = tempList[i + 1].substring(0, tempList[i + 1].indexOf("w"));

                Double curWeight = Double.parseDouble(tempList[i].substring(tempList[i].indexOf("w") + 1));
                Double nextWeight = Double.parseDouble(tempList[i + 1].substring(tempList[i + 1].indexOf("w") + 1));


                if (curRecipeCode.equals(nextRecipeCode)) {
                    if (cnt == 0) {
                        totalWeight = curWeight + nextWeight;
                    } else {
                        totalWeight += nextWeight;
                    }
                    cnt++;
                } else {
                    if (cnt == 0) {
                        for (int j = 0; j < mapCount; j++) {
                            String mappedRecipeCode = mapping[j].substring(0, mapping[j].indexOf(","));
                            Double mappedTotal = Double.parseDouble(mapping[j].substring(mapping[j].indexOf(",") + 1));

                            if (curRecipeCode.equals(mappedRecipeCode)) {
                                recipeList[recipeLength] = tempList[i] + "t" + String.valueOf(mappedTotal);
                            }
                        }

                    } else {
                        for (int j = 0; j < mapCount; j++) {
                            String mappedRecipeCode = mapping[j].substring(0, mapping[j].indexOf(","));
                            Double mappedTotal = Double.parseDouble(mapping[j].substring(mapping[j].indexOf(",") + 1));

                            if (curRecipeCode.equals(mappedRecipeCode)) {
                                recipeList[recipeLength] = tempList[i].substring(0, tempList[i].indexOf("w")) + "w" + String.valueOf(totalWeight) + "t" + String.valueOf(mappedTotal);
                            }
                        }
                    }
                    cnt = 0;
                    recipeLength++;
                    totalWeight = 0;
                }
            }
        }

//        for (int i = 0; i < recipeLength; i++) {
//            Log.e("test", recipeList[i] + "\n");
////            Log.e("asdasd",String.valueOf(recipeLength));
//        }


        for (int i = 0; i < recipeLength; i++) {
            double weight = Double.parseDouble(recipeList[i].substring(recipeList[i].indexOf("w") + 1, recipeList[i].indexOf("t")));
            double total = Double.parseDouble(recipeList[i].substring(recipeList[i].indexOf("t") + 1));
            String recipeCode = recipeList[i].substring(0, recipeList[i].indexOf("w"));
            recipeList[i] = recipeCode + "," + String.valueOf((weight / total) * 100.0);
        }


        for (int i = 0; i < recipeLength; i++) {
            for (int j = 0; j < recipeLength - 1 - i; j++) {
                if (Double.parseDouble(recipeList[j].substring(recipeList[j].indexOf(",") + 1)) < Double.parseDouble(recipeList[j + 1].substring(recipeList[j + 1].indexOf(",") + 1))) {
                    String a = recipeList[j];
                    recipeList[j] = recipeList[j + 1];
                    recipeList[j + 1] = a;
                }
            }
        }

        for (int i = 0; i < recipeLength; i++) {
            Log.e("test", recipeList[i]);
        }

        for (int i = 0; i < recipeLength; i++) {
            String frequency = recipeList[i].substring(recipeList[i].indexOf(",") + 1);
            recipeList[i] = recipeList[i].substring(0, recipeList[i].indexOf(","));
            Log.e("RECIPELIST", recipeList[i]);
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

    public void calTotal() {
        cursor = db.rawQuery("SELECT recipe_code,count(*) FROM " + TABLE_NAME + " group by recipe_code order by count(*) desc", null);
        while (cursor.moveToNext()) {
            mapping[mapCount] = cursor.getString(0);
            mapping[mapCount] += "," + cursor.getString(1);
            mapCount++;
        }
        //Log.e("totalCount",String.valueOf(mapCount));
    }
}
