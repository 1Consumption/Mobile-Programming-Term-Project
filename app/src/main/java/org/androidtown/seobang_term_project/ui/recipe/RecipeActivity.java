package org.androidtown.seobang_term_project.ui.recipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.utils.DBUtils;
import org.androidtown.seobang_term_project.utils.QuickSortString;
import org.androidtown.seobang_term_project.utils.ZoomOutPageTransformer;

import butterknife.BindView;

/**
 * @When:
 * When the user views the recipe to cook, it is executed.
 *
 * @Function:
 * In the database, find the recipe of the dish of your choice.
 * It shows each course of cooking.
 * If the user exits while the timer is running, a warning message is displayed.
 *
 * @Technique:
 * In the database, we find recipes that match the cooked code, store the order of the recipes together, sort them, and show them in order.
 * If timeron is true, an alert window appears.
 * Send the recipe information as a pagefragment.
 */

public class RecipeActivity extends BaseActivity {
    protected @BindView(R.id.pager)
    ViewPager viewpager;
    private PagerAdapter mPagerAdapter;

    private String[] RecipeProcess;
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_process_2.db";
    public static final String TABLE_NAME = "recipe_process";
    public static final String DB_Name_2 = "recipe_basic_information_2.db";
    public static final String TABLE_NAME_2 = "recipe_basic_information";

    private SQLiteDatabase db;
    private Cursor cursor;

    public static Boolean timerOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe);

        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name_2);

        String result = getSelectedRecipeId();

        String name = getSelectedRecipeName();
        db = DatabaseFactory.create(this, DB_Name);
        cursor = db.rawQuery("SELECT process,explanation,url FROM " + TABLE_NAME + " WHERE recipe_code=\"" + result + "\"", null); //쿼리문

        String strRecipeProcess = "";

        while (cursor.moveToNext()) {
            strRecipeProcess += name + "+";
            strRecipeProcess += cursor.getString(0) + "&";
            strRecipeProcess += cursor.getString(1) + "|";
            strRecipeProcess += cursor.getString(2) + "#";
        }

        strRecipeProcess = strRecipeProcess.substring(0, strRecipeProcess.length() - 1);

        RecipeProcess = strRecipeProcess.split("#");

        QuickSortString.sort(RecipeProcess, 0, RecipeProcess.length - 1);
        RecipeProcess[RecipeProcess.length - 1] += "last";

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mPagerAdapter);
        viewpager.setOffscreenPageLimit(RecipeProcess.length);
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

    public void setTimerOn(Boolean b) {
        timerOn = b;
    }

    public Boolean getTimerOn() {
        return timerOn;
    }

    @Override
    public void onBackPressed() {
        if (timerOn == true) {
            AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
            alert_ex.setMessage("정말로 종료하시겠습니까?\n실행 중인 타이머는 종료됩니다.");

            alert_ex.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert_ex.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RecipeActivity.super.onBackPressed();
                }
            });
            alert_ex.setTitle("경고!");
            AlertDialog alert = alert_ex.create();
            alert.show();
        } else {
            super.onBackPressed();
        }
    }
}
