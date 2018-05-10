package org.androidtown.seobang_term_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecipeActivity extends AppCompatActivity {

    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_process.db";
    public static final String TABLE_NAME = "recipe_process";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;

    TextView recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setDB(this);

        recipe = findViewById(R.id.recipe);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String result = bundle.getString("selectedRecipe");

        if (!result.equals("No Result")) {
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

            String[] RecipeProcess = strRecipeProcess.split("#");

            for (int i = 0; i < RecipeProcess.length; i++) {
                for (int j = 0; j < RecipeProcess.length; j++) {
                    if (Integer.parseInt(RecipeProcess[j].substring(0, RecipeProcess[j].indexOf("&"))) == min) {
                        Log.e("Test", RecipeProcess[j].substring(RecipeProcess[j].indexOf("&") + 1));
                        recipe.append(RecipeProcess[j].substring(RecipeProcess[j].indexOf("&") + 1) + "\n\n");
                        min++;
                        break;
                    }
                }
            }


            cursor.close();
            db.close();
        } else {
            recipe.append(result + "\n\n");
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