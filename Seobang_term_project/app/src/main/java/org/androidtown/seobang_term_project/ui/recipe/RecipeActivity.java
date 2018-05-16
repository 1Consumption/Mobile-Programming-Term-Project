
package org.androidtown.seobang_term_project;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.support.v4.app.NotificationCompat.*;

public class RecipeActivity extends AppCompatActivity implements PageFragment.OnTimePickerSetListener {
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    int mresult;

    String[] RecipeProcess;
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_process.db";
    public static final String TABLE_NAME = "recipe_process";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;

    public void onTimePickerSet(int result) {
        mresult = result;
        if (mresult == 0) {
            Builder mBuilder = new Builder(RecipeActivity.this);
            mBuilder.setContentText("서방");
            mBuilder.setSmallIcon(R.drawable.spoon);
            mBuilder.setContentTitle("타이머 종료!");
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            mBuilder.setPriority(PRIORITY_DEFAULT);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setDB(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String result = bundle.getString("selectedRecipe");

        mHelper = new ProductDBHelper(getApplicationContext());


        db = mHelper.getWritableDatabase();

        cursor = db.rawQuery("SELECT process,explanation,url FROM " + TABLE_NAME + " WHERE recipe_code=\"" + result + "\"", null); //쿼리문

//        Cursor URLCursor=db.rawQuery("SELECT url FROM " + TABLE_NAME + " WHERE recipe_code=\"" + result + "\"", null);

        String strRecipeProcess = "";

        while (cursor.moveToNext()) {
            strRecipeProcess += cursor.getString(0);
            strRecipeProcess += "&" + cursor.getString(1) + "|";
            strRecipeProcess += cursor.getString(2) + "#";
        }

        strRecipeProcess = strRecipeProcess.substring(0, strRecipeProcess.length() - 1);

        RecipeProcess = strRecipeProcess.split("#");
        Log.e("LENGTH", String.valueOf(RecipeProcess.length));

        QuickSort sort = new QuickSort();

        sort.sort(RecipeProcess, 0, RecipeProcess.length - 1);

        for (int i = 0; i < RecipeProcess.length; i++)
            Log.e("Recipe", RecipeProcess[i]);

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

}
