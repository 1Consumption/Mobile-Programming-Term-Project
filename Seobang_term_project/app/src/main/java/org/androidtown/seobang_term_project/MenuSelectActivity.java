package org.androidtown.seobang_term_project;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuSelectActivity extends AppCompatActivity {
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_ingredient_info.db";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;
    Button btnSelect, go;

    TextView edtRecipeCode, edtIngredientOrder, edtIngredientName, edtIngredientAmount, edtIngredientTypeName;
    EditText edt;

    private RecyclerView recyclerView;
    //recycler view 에서 section 을 나눠서

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        initLayout();
        initData();

        setDB(this);

        edtRecipeCode = (TextView) findViewById(R.id.edtRecipeCode);
        edtIngredientOrder = (TextView) findViewById(R.id.edtIngredientOrder);
        edtIngredientName = (TextView) findViewById(R.id.edtIngredientName);
        edtIngredientAmount = (TextView) findViewById(R.id.edtIngredientAmount);
        edtIngredientTypeName = (TextView) findViewById(R.id.edtIngredientTypeName);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        go = (Button) findViewById(R.id.go);
        edt = (EditText) findViewById(R.id.edt);

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView count = findViewById(R.id.count);
                mHelper = new ProductDBHelper(getApplicationContext());
                db = mHelper.getWritableDatabase();
                Cursor countCursor = db.rawQuery("SELECT count(*) FROM recipe_ingredient_info WHERE ingredient_name=\"" + edt.getText().toString() + "\"", null);
                countCursor.getCount();
                countCursor.moveToNext();
                int cnt = countCursor.getInt(0);
                countCursor.close();

                if (cnt != 0) {
                    cursor = db.rawQuery("SELECT * FROM recipe_ingredient_info WHERE ingredient_name = \"" + edt.getText().toString() + "\"", null); //쿼리문
                    startManagingCursor(cursor);

                    String strRecipeCode = "Recipe Code" + "\r\n" + "--------" + "\r\n";
                    String strIngredientOrder = "Ingredient Order" + "\r\n" + "--------" + "\r\n";
                    String strIngredientName = "Ingredient Name" + "\r\n" + "--------" + "\r\n";
                    String strIngredientAmount = "Ingredient Amount" + "\r\n" + "--------" + "\r\n";
                    String strIngredientTypeName = "Ingredient Type Name" + "\r\n" + "--------" + "\r\n";

                    while (cursor.moveToNext()) {
                        strRecipeCode += cursor.getString(0) + "\r\n";
                        strIngredientOrder += cursor.getString(1) + "\r\n";
                        strIngredientName += cursor.getString(2) + "\r\n";
                        strIngredientAmount += cursor.getString(3) + "\r\n";
                        strIngredientTypeName += cursor.getString(4) + "\r\n";
                    }

                    cursor.close();
                    db.close();

                    edtRecipeCode.setText(strRecipeCode);
                    edtIngredientOrder.setText(strIngredientOrder);
                    edtIngredientName.setText(strIngredientName);
                    edtIngredientAmount.setText(strIngredientAmount);
                    edtIngredientTypeName.setText(strIngredientTypeName);
                } else {
                    edtRecipeCode.setText("No Result");
                    edtIngredientOrder.setText("No Result");
                    edtIngredientName.setText("No Result");
                    edtIngredientAmount.setText("No Result");
                    edtIngredientTypeName.setText("No Result");
                }
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

    //initialize layout
    private void initLayout() {
        recyclerView = findViewById(R.id.menu_select_recyclerView);
    }

    //initialize data
    private void initData() {
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();

        Ingredient ingredient1 = new Ingredient();
        /*for(int i = 0; i < 5; i ++){
           //재료 아이콘 생성
        }*/
        //ingredient.setImage(R.drawable.steak);
        ingredient1.setIngredientType("고기류");
        ingredientList.add(ingredient1);

        //생선
        Ingredient ingredient2 = new Ingredient();
        /*for(int i = 0; i < 5; i ++){
           //재료 아이콘 생성
        }*/
        //ingredient.setImage(R.drawable.steak);
        ingredient2.setIngredientType("생선류");
        ingredientList.add(ingredient2);

        //채소
        Ingredient ingredient3 = new Ingredient();
        /*for(int i = 0; i < 5; i ++){
           //재료 아이콘 생성
        }*/
        //ingredient.setImage(R.drawable.steak);
        ingredient3.setIngredientType("채소류");
        ingredientList.add(ingredient3);


        //기타
        Ingredient ingredient4 = new Ingredient();
        /*for(int i = 0; i < 5; i ++){
           //재료 아이콘 생성
        }*/
        //ingredient.setImage(R.drawable.steak);
        ingredient4.setIngredientType("기타재료");
        ingredientList.add(ingredient4);

        recyclerView.setAdapter(new RecyclerAdapter(ingredientList, R.layout.menu_row));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}

