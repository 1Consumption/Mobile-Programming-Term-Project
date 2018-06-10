package org.androidtown.seobang_term_project.ui.recipe;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.items.Recipe;
import org.androidtown.seobang_term_project.recycler.adapters.RecipeAdapter;
import org.androidtown.seobang_term_project.recycler.viewholders.RecipeViewHolder;
import org.androidtown.seobang_term_project.utils.DBUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class RecipeSelectActivity extends BaseActivity implements RecipeViewHolder.Delegate {

    private static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    private static final String DB_Name = "recipe_basic_information_2.db";
    private static final String TABLE_NAME = "recipe_basic_information";
    private static final String DB_Name2 = "recipe_process_2.db";

    private SQLiteDatabase db_info;
    private Cursor cursor;
    private RecipeAdapter mAdapter;

    private int index = 2;

    ArrayList<String> menus=new ArrayList<>();

    protected @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe_select);
        setRefreshView();


        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name2);

        this.db_info = DatabaseFactory.create(this, DB_Name);

        cursor = db_info.rawQuery("SELECT recipe_name FROM " + TABLE_NAME +" ORDER BY recipe_name", null);
        while(cursor.moveToNext()){
            menus.add(cursor.getString(0));
        }


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
                if ((index-1) * 10 <= (lastVisibleItem + visibleThreshold)) {
                    loadMore();
                }
            }
        });



        for(int i=0; i< 10; i++) {
            mAdapter.addItem(new Recipe(menus.get(i), getFoodPreviewImage(menus.get(i))));
        }
    }

    private void loadMore() {
        if((index * 10) <= menus.size()) {
            if((index+1)*10>menus.size()){
                for (int i = (index - 1) * 10; i < menus.size(); i++) {
                    mAdapter.addItem(new Recipe(menus.get(i), getFoodPreviewImage(menus.get(i))));
                }
            }else {
                for (int i = (index - 1) * 10; i < index * 10; i++) {
                    mAdapter.addItem(new Recipe(menus.get(i), getFoodPreviewImage(menus.get(i))));
                }
            }
            index++;
        }
    }

    public void goToRecipe(String name) {
        Cursor countCursor = db_info.rawQuery("SELECT count(*) FROM " + TABLE_NAME + " WHERE recipe_name=\"" + name + "\"", null);
        countCursor.getCount();
        countCursor.moveToNext();

        int cnt = countCursor.getInt(0);
        countCursor.close();

        String selectedRecipeCode = "";

        if (cnt != 0) {
            cursor = db_info.rawQuery("SELECT recipe_code FROM " + TABLE_NAME + " WHERE recipe_name=\"" + name + "\"", null); //쿼리문
            startManagingCursor(cursor);

            while (cursor.moveToNext()) {
                selectedRecipeCode = cursor.getString(0);
            }
            Intent intent = new Intent(getApplicationContext(), RecipePreviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("selectedRecipe", selectedRecipeCode);
            bundle.putString("RecipeName", name);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            String msg = "해당 요리는 없습니다.";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFoodId(String name) {
        cursor = db_info.rawQuery("SELECT recipe_code FROM " + TABLE_NAME + " WHERE recipe_name=\"" + name + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        return cursor.getString(0);
    }

    private String getFoodPreviewImage(String name) {
        cursor = db_info.rawQuery("SELECT URL FROM " + TABLE_NAME + " WHERE recipe_name=\"" + name + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        return cursor.getString(0);
    }

    @Override
    public void onItemClick(Recipe recipe, View view) {
        Intent intent = new Intent(getApplicationContext(), RecipePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("selectedRecipe", getFoodId(recipe.getName()));
        bundle.putString("RecipeName", recipe.getName());
        intent.putExtras(bundle);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view,
                    ViewCompat.getTransitionName(view));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, menus);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToRecipe(adapter.getItem(position));
            }
        });
        searchAutoComplete.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                goToRecipe(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db_info.close();
    }
}
