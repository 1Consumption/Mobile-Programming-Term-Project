package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    public static final String DB_Name = "test_ingredient.db";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;
    Button btnSelect, showRecipe;

    String result = "";

    TextView edtRecipeCode, edtIngredientOrder, edtIngredientName, edtIngredientAmount, edtIngredientTypeName;
    EditText edt;

    private int count = 0;
    protected @BindView(R.id.count)
    TextView tv_count;

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
        showRecipe = (Button) findViewById(R.id.showRecipeFromIngredient);
        edt = (EditText) findViewById(R.id.edt);

        mHelper = new ProductDBHelper(getApplicationContext());
        db = mHelper.getWritableDatabase();

        showRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (result.equals("")) {
                    Toast.makeText(getApplicationContext(), "You didn't choose anything!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), RecipeFromIngredientActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("result", result.substring(0, result.length() - 1));
                    intent.putExtras(bundle);
                    Log.e("IngredientSelect", result.substring(0, result.length() - 1));
                    startActivity(intent);
                }
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cursor countCursor = db.rawQuery("SELECT count(*) FROM recipe_ingredient_info WHERE ingredient_name=\"" + edt.getText().toString() + "\"", null);
                countCursor.getCount();
                countCursor.moveToNext();
                int cnt = countCursor.getInt(0);
                countCursor.close();

                if (cnt != 0) {
                    cursor = db.rawQuery("SELECT * FROM recipe_ingredient_info WHERE ingredient_type_name = \"주재료\" and ingredient_name=\"" + edt.getText().toString() + "\"", null); //쿼리문

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
        ingredients_meat.add(new Ingredient("돼지갈비", R.drawable.meat));
        ingredients_meat.add(new Ingredient("소고기", R.drawable.beaf));
        ingredients_meat.add(new Ingredient("닭고기", R.drawable.chicken));
        ingredients_meat.add(new Ingredient("닭가슴살", R.drawable.chicken));
        ingredients_meat.add(new Ingredient("닭고기", R.drawable.chicken));
//        ingredients_meat.add(new Ingredient("오리고기", R.drawable.duck));

        List<Ingredient> ingredients_fish = new ArrayList<>();
        ingredients_fish.add(new Ingredient("오징어", R.drawable.squid));
        ingredients_fish.add(new Ingredient("새우", R.drawable.shrimp));
        ingredients_fish.add(new Ingredient("조개", R.drawable.shellfish));
        ingredients_fish.add(new Ingredient("낙지", R.drawable.nakji));
        ingredients_fish.add(new Ingredient("홍합", R.drawable.mussel));
        ingredients_fish.add(new Ingredient("바지락", R.drawable.bazirak));
        ingredients_fish.add(new Ingredient("고등어", R.drawable.fish2));
        ingredients_fish.add(new Ingredient("참치", R.drawable.nakji));
        ingredients_fish.add(new Ingredient("굴", R.drawable.oyster));
        ingredients_fish.add(new Ingredient("멸치", R.drawable.myeolchi));
        ingredients_fish.add(new Ingredient("꽁치", R.drawable.fish1));
        ingredients_fish.add(new Ingredient("갈치", R.drawable.fish4));
        ingredients_fish.add(new Ingredient("쭈꾸미", R.drawable.fish7));
        ingredients_fish.add(new Ingredient("도미", R.drawable.fish8));

        List<Ingredient> ingredients_veget = new ArrayList<>();
        ingredients_veget.add(new Ingredient("무", R.drawable.radish));
        ingredients_veget.add(new Ingredient("양파", R.drawable.onion));
        ingredients_veget.add(new Ingredient("고추", R.drawable.pepper));
        ingredients_veget.add(new Ingredient("콩나물", R.drawable.kongnamul));
        ingredients_veget.add(new Ingredient("감자", R.drawable.potato));
        ingredients_veget.add(new Ingredient("오이", R.drawable.cucumber));
        ingredients_veget.add(new Ingredient("배추", R.drawable.baechu));
        ingredients_veget.add(new Ingredient("부추", R.drawable.buchu));
        ingredients_veget.add(new Ingredient("당근", R.drawable.carrot));
        ingredients_veget.add(new Ingredient("마늘", R.drawable.garlic));
        ingredients_veget.add(new Ingredient("미나리", R.drawable.minari));
        ingredients_veget.add(new Ingredient("가지", R.drawable.eggplant));
        ingredients_veget.add(new Ingredient("깻잎", R.drawable.ggaet));
        ingredients_veget.add(new Ingredient("시금치", R.drawable.spinach));
        ingredients_veget.add(new Ingredient("실파", R.drawable.silpa));
        ingredients_veget.add(new Ingredient("양배추", R.drawable.cabbage));
        ingredients_veget.add(new Ingredient("대파", R.drawable.daepa));
        ingredients_veget.add(new Ingredient("상추", R.drawable.lettuce));
        ingredients_veget.add(new Ingredient("고사리", R.drawable.gosari));
        ingredients_veget.add(new Ingredient("숙주", R.drawable.sukju));
        ingredients_veget.add(new Ingredient("죽순", R.drawable.juksoon));
        ingredients_veget.add(new Ingredient("도라지", R.drawable.doraji));
        ingredients_veget.add(new Ingredient("고구마", R.drawable.sweet_potato));
        ingredients_veget.add(new Ingredient("방울 토마토", R.drawable.bangultomato));
        ingredients_veget.add(new Ingredient("토마토", R.drawable.tomato));
        ingredients_veget.add(new Ingredient("무순", R.drawable.musoon));

//        for (int i = 0; i < 10; i++)
//            ingredients_veget.add(new Ingredient("채소" + i, R.drawable.lettuce));

        List<Ingredient> ingredients_mushroom = new ArrayList<>();
        ingredients_mushroom.add(new Ingredient("표고 버섯", R.drawable.pyogo));
        ingredients_mushroom.add(new Ingredient("느타리 버섯", R.drawable.neutari));
        ingredients_mushroom.add(new Ingredient("팽이 버섯", R.drawable.pengi));
        ingredients_mushroom.add(new Ingredient("양송이 버섯", R.drawable.yangsongi));
        ingredients_mushroom.add(new Ingredient("목이 버섯", R.drawable.moki));
        ingredients_mushroom.add(new Ingredient("새송이 버섯", R.drawable.saesongi));

        List<Ingredient> ingredients_seasoning = new ArrayList<>();
        ingredients_seasoning.add(new Ingredient("소금", R.drawable.salt));
        ingredients_seasoning.add(new Ingredient("된장", R.drawable.doenjang));
        ingredients_seasoning.add(new Ingredient("고추장", R.drawable.gochujang));
        ingredients_seasoning.add(new Ingredient("간장", R.drawable.ganjang));
        ingredients_seasoning.add(new Ingredient("새우젓", R.drawable.saeujeot));
        ingredients_seasoning.add(new Ingredient("토마토 케찹", R.drawable.ketchup));

        List<Ingredient> ingredients_others = new ArrayList<>();
        ingredients_others.add(new Ingredient("밥", R.drawable.bob));
        ingredients_others.add(new Ingredient("두부", R.drawable.tofu));
        ingredients_others.add(new Ingredient("김치", R.drawable.kimchi));
        ingredients_others.add(new Ingredient("쌀", R.drawable.rice));
        ingredients_others.add(new Ingredient("계란", R.drawable.egg));
        ingredients_others.add(new Ingredient("밀가루", R.drawable.wheat_flour));
        ingredients_others.add(new Ingredient("찹쌀", R.drawable.chapssal));
        ingredients_others.add(new Ingredient("가래떡", R.drawable.garaeddeock));
        ingredients_others.add(new Ingredient("밤", R.drawable.chestnut));
        ingredients_others.add(new Ingredient("스파게티 면", R.drawable.spaghetti));
        ingredients_others.add(new Ingredient("팥", R.drawable.red_been));
        ingredients_others.add(new Ingredient("설탕", R.drawable.sugar));
        ingredients_others.add(new Ingredient("당면", R.drawable.dangmyoen));
        ingredients_others.add(new Ingredient("미역", R.drawable.miyeok));
        ingredients_others.add(new Ingredient("우유", R.drawable.milk));
        ingredients_others.add(new Ingredient("찹쌀가루", R.drawable.chapssalgaru));
        ingredients_others.add(new Ingredient("게맛살", R.drawable.gaematsal));

//        for (int i = 0; i < 10; i++)
//            ingredients_others.add(new Ingredient("기타" + i, R.drawable.setting));

        adapter = new IngredientListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.addIngredientListItem(new IngredientList("고기류", ingredients_meat));
        adapter.addIngredientListItem(new IngredientList("해산물", ingredients_fish));
        adapter.addIngredientListItem(new IngredientList("채소류", ingredients_veget));
        adapter.addIngredientListItem(new IngredientList("버섯류", ingredients_mushroom));
        adapter.addIngredientListItem(new IngredientList("양념", ingredients_seasoning));
        adapter.addIngredientListItem(new IngredientList("기타재료", ingredients_others));
    }

    @Override
    public void onItemClick(Ingredient ingredient, boolean isOnClicked) {
        Toast.makeText(this, ingredient.getIngredientType() + " clicked: " + isOnClicked, Toast.LENGTH_SHORT).show();
        result = checkIsInResult(ingredient, result);

        if (isOnClicked) tv_count.setText(++count + "");
        else tv_count.setText(--count + "");
    }

    public String checkIsInResult(Ingredient ingredient, String result) {
        if (result.indexOf(ingredient.getIngredientType()) == -1)
            result += ingredient.getIngredientType() + ",";
        else {
            if (result.indexOf(ingredient.getIngredientType()) == 0) {
                if (result.length() == ingredient.getIngredientType().length() + 1)
                    result = "";
                else
                    result = result.substring(ingredient.getIngredientType().length() + 1);
            } else
                result = result.substring(0, result.indexOf(ingredient.getIngredientType()) - 1) + result.substring(result.indexOf(ingredient.getIngredientType()) + ingredient.getIngredientType().length());
        }
        return result;
    }
}