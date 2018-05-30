package org.androidtown.seobang_term_project.ui.recipe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.ui.history.HistoryActivity;
import org.androidtown.seobang_term_project.utils.DBUtils;
import org.androidtown.seobang_term_project.utils.QuickSort;
import org.androidtown.seobang_term_project.utils.ZoomOutPageTransformer;

import butterknife.BindView;

public class RecipeActivity extends BaseActivity {
    protected @BindView(R.id.pager)
    ViewPager viewpager;
    private PagerAdapter mPagerAdapter;

    private String[] RecipeProcess;
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_process.db";
    public static final String TABLE_NAME = "recipe_process";

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe);

        DBUtils.setDB(this, ROOT_DIR, DB_Name);

        String result = getSelectedRecipeId();
        db = DatabaseFactory.create(this, DB_Name);
        cursor = db.rawQuery("SELECT process,explanation,url FROM " + TABLE_NAME + " WHERE recipe_code=\"" + result + "\"", null); //쿼리문

        String strRecipeProcess = "";

        while (cursor.moveToNext()) {
            strRecipeProcess += result + "+";
            strRecipeProcess += cursor.getString(0) + "&";
            strRecipeProcess += cursor.getString(1) + "|";
            strRecipeProcess += cursor.getString(2) + "#";
        }

        strRecipeProcess = strRecipeProcess.substring(0, strRecipeProcess.length() - 1);

        RecipeProcess = strRecipeProcess.split("#");
        Log.e("LENGTH", String.valueOf(RecipeProcess.length));

        QuickSort.sort(RecipeProcess, 0, RecipeProcess.length - 1);
        RecipeProcess[RecipeProcess.length - 1] += "last";

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mPagerAdapter);
        viewpager.addOnPageChangeListener(pageChangeListener);
        viewpager.setPageTransformer(true, new ZoomOutPageTransformer());
        setToolbarName(getSelectedRecipeName() + " 1/" + RecipeProcess.length);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setToolbarName(getSelectedRecipeName() + " " + (position + 1) + "/" + RecipeProcess.length);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.create(RecipeProcess[position]);
        }

        @Override
        public int getCount() {
            return RecipeProcess.length;
        }
    }

    private String getSelectedRecipeId() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        return bundle.getString("selectedRecipe");
    }

    private String getSelectedRecipeName() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        return bundle.getString("RecipeName");
    }

    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
