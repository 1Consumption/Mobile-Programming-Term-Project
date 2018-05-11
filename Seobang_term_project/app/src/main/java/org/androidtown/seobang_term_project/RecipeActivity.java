package org.androidtown.seobang_term_project;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class RecipeActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    String[] RecipeProcess;
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_process.db";
    public static final String TABLE_NAME = "recipe_process";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setDB(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String result = bundle.getString("selectedRecipe");

        int min = 1;

        mHelper = new ProductDBHelper(getApplicationContext());
        db = mHelper.getWritableDatabase();

        Cursor countCursor = db.rawQuery("SELECT count(*) FROM " + TABLE_NAME + " WHERE recipe_code=\"" + result + "\"", null);
        countCursor.getCount();
        countCursor.moveToNext();
        int cnt = countCursor.getInt(0);
        countCursor.close();

        cursor = db.rawQuery("SELECT process,explanation FROM " + TABLE_NAME + " WHERE recipe_code=\"" + result + "\"", null); //쿼리문
        startManagingCursor(cursor);

        String strRecipeProcess = "";

        while (cursor.moveToNext()) {
            strRecipeProcess += cursor.getString(0);
            strRecipeProcess += "&" + cursor.getString(1) + "#";
        }
        strRecipeProcess = strRecipeProcess.substring(0, strRecipeProcess.length() - 1);

        RecipeProcess = strRecipeProcess.split("#");
        Log.e("LENGTH", String.valueOf(RecipeProcess.length));

        QuickSort sort = new QuickSort();

        sort.sort(RecipeProcess, 0, RecipeProcess.length - 1);

        for (int i = 0; i < RecipeProcess.length; i++)
            Log.e("Recipe", RecipeProcess[i]);

        cursor.close();
        db.close();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
    }

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
            Log.e("getCountTest", String.valueOf(RecipeProcess.length));
            return RecipeProcess.length;
        }

    }


    public static void setDB(Context ctx) {
        File folder = new File(ROOT_DIR);
        if (folder.exists()) {
        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets();
        File outfile = new File(ROOT_DIR + DB_Name);
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open(DB_Name, AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } else {
            }
        } catch (IOException e) {
        }
    }

    class ProductDBHelper extends SQLiteOpenHelper {  //새로 생성한 adapter 속성은 SQLiteOpenHelper이다.
        public ProductDBHelper(Context context) {
            super(context, DB_Name, null, 1);    // db명과 버전만 정의 한다.
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }

    static int partition(int arr[], int left, int right) {

        int pivot = arr[(left + right) / 2];

        while (left < right) {
            while ((arr[left] < pivot) && (left < right))
                left++;
            while ((arr[right] > pivot) && (left < right))
                right--;

            if (left < right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }

        return left;
    }

    public static void quickSort(int arr[], int left, int right) {

        if (left < right) {
            int pivotNewIndex = partition(arr, left, right);

            quickSort(arr, left, pivotNewIndex - 1);
            quickSort(arr, pivotNewIndex + 1, right);
        }

    }
}