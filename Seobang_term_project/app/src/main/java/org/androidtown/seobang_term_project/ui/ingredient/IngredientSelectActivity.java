package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.items.Ingredient;
import org.androidtown.seobang_term_project.items.IngredientList;
import org.androidtown.seobang_term_project.recycler.adapters.IngredientListAdapter;
import org.androidtown.seobang_term_project.recycler.viewholders.IngredientViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientSelectActivity extends AppCompatActivity implements IngredientViewHolder.Delegate {
    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_ingredient_info.db";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;
    Button btnSelect, go;

    TextView edtRecipeCode, edtIngredientOrder, edtIngredientName, edtIngredientAmount, edtIngredientTypeName;
    EditText edt;

    private int count = 0;
    protected @BindView(R.id.count) TextView tv_count;

    private RecyclerView recyclerView;
    private IngredientListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_select);
        ButterKnife.bind(this);

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

        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHelper = new ProductDBHelper(getApplicationContext());
                db = mHelper.getWritableDatabase();
                Cursor countCursor = db.rawQuery("SELECT count(*) FROM recipe_ingredient_info WHERE ingredient_name=\"" + edt.getText().toString() + "\"", null);
                countCursor.getCount();
                countCursor.moveToNext();
                int cnt = countCursor.getInt(0);
                countCursor.close();

                if (cnt != 0) {
                    cursor = db.rawQuery("SELECT * FROM recipe_ingredient_info WHERE ingredient_type_name = \"주재료\" and ingredient_name=\"" + edt.getText().toString() + "\"", null); //쿼리문
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
        recyclerView = findViewById(R.id.ingredient_select_recyclerView);
    }

    //initialize data
    private void initData() {
        List<Ingredient> ingredients_meat = new ArrayList<>();
        ingredients_meat.add(new Ingredient("돼지고기", R.drawable.meat));
        ingredients_meat.add(new Ingredient("소고기", R.drawable.beaf));
        ingredients_meat.add(new Ingredient("닭고기", R.drawable.chicken));
        ingredients_meat.add(new Ingredient("오리고기", R.drawable.duck));

        List<Ingredient> ingredients_fish = new ArrayList<>();
        for(int i=0 ;i<10; i++)
            ingredients_fish.add(new Ingredient("생선" + i, R.drawable.lettuce));

        List<Ingredient> ingredients_veget = new ArrayList<>();
        for(int i=0 ;i<10; i++)
            ingredients_veget.add(new Ingredient("채소" + i, R.drawable.lettuce));

        List<Ingredient> ingredients_others = new ArrayList<>();
        for(int i=0 ;i<10; i++)
            ingredients_others.add(new Ingredient("기타" + i, R.drawable.setting));

        adapter = new IngredientListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.addIngredientListItem(new IngredientList("고기류", ingredients_meat));
        adapter.addIngredientListItem(new IngredientList("생선류", ingredients_fish));
        adapter.addIngredientListItem(new IngredientList("채소류", ingredients_veget));
        adapter.addIngredientListItem(new IngredientList("기타재료", ingredients_others));
    }

    @Override
    public void onItemClick(Ingredient ingredient, boolean isOnClicked) {
        Toast.makeText(this, ingredient.getIngredientType() + "clicked: " + isOnClicked, Toast.LENGTH_SHORT).show();

        if(isOnClicked) tv_count.setText(++count + "");
        else tv_count.setText(--count + "");
    }
}