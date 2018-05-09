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

public class RecipeSelectActivity extends AppCompatActivity {

    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_basic_information.db";
    public static final String TABLE_NAME = "recipe_basic_information";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;
    Button showRecipe, goToRecipe;

    TextView recipeOutput;
    EditText searchRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_select);

        setDB(this);

        showRecipe = findViewById(R.id.showRecipe);
        goToRecipe = findViewById(R.id.goToRecipe);
        recipeOutput = findViewById(R.id.recipeOutput);
        searchRecipe = findViewById(R.id.searchRecipe);

        goToRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHelper = new ProductDBHelper(getApplicationContext());
                db = mHelper.getWritableDatabase();

                Cursor countCursor = db.rawQuery("SELECT count(*) FROM " + TABLE_NAME + " WHERE recipe_name=\"" + searchRecipe.getText().toString() + "\"", null);
                countCursor.getCount();
                countCursor.moveToNext();
                int cnt = countCursor.getInt(0);
                countCursor.close();

                String selectedRecipeCode = "";

                if (cnt != 0) {
                    cursor = db.rawQuery("SELECT recipe_code FROM " + TABLE_NAME + " WHERE recipe_name=\"" + searchRecipe.getText().toString() + "\"", null); //쿼리문
                    startManagingCursor(cursor);

                    while (cursor.moveToNext()) {
                        selectedRecipeCode = cursor.getString(0);
                    }
                    cursor.close();
                    db.close();
                } else {
                    selectedRecipeCode = "No Result";
                }

                Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("selectedRecipe", selectedRecipeCode);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        showRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHelper = new ProductDBHelper(getApplicationContext());
                db = mHelper.getWritableDatabase();
                Cursor countCursor = db.rawQuery("SELECT count(*) FROM " + TABLE_NAME, null);
                countCursor.getCount();
                countCursor.moveToNext();
                int cnt = countCursor.getInt(0);
                countCursor.close();

                if (cnt != 0) {
                    cursor = db.rawQuery("SELECT recipe_name FROM " + TABLE_NAME, null); //쿼리문
                    startManagingCursor(cursor);

                    String strRecipeName = "Recipe Name" + "\r\n" + "--------" + "\r\n";


                    while (cursor.moveToNext()) {
                        strRecipeName += cursor.getString(0) + "\r\n";
                    }
                    recipeOutput.setText(strRecipeName);
                } else {
                    recipeOutput.setText("No Result");
                }

                cursor.close();
                db.close();
            }
        });
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