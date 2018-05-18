package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.androidtown.seobang_term_project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecipeFromIngredientActivity extends AppCompatActivity {
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "test_ingredient.db";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_from_ingredient);

        setDB(this);
        mHelper = new ProductDBHelper(getApplicationContext());
        db = mHelper.getWritableDatabase();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        TextView textView = findViewById(R.id.ingredientSelected);

        String received = bundle.getString("result");
        String[] ingredient = received.split(",");

        String temp = "";

        for (int i = 0; i < ingredient.length; i++)
            Log.e("RecipeFromIngredient", "\"" + ingredient[i] + "\"");

        int tempNum = 0;


        for (int i = 0; i < ingredient.length; i++) {
            cursor = db.rawQuery("SELECT count(recipe_code) FROM recipe_ingredient_info WHERE ingredient_type_name=\"주재료\" and ingredient_name=\"" + ingredient[tempNum] + "\"", null);
            tempNum++;
            cursor.moveToNext();
            temp += cursor.getString(0) + "\n\n";
            cursor = db.rawQuery("SELECT recipe_code FROM recipe_ingredient_info WHERE ingredient_type_name=\"주재료\" and ingredient_name=\"" + ingredient[i] + "\"", null); //쿼리문

            while (cursor.moveToNext()) {
                temp += cursor.getString(0) + "\n";
            }

            temp += "===========================================\n";
        }

        textView.setText(temp);
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
