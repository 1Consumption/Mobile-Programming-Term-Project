package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.items.Ingredient;
import org.androidtown.seobang_term_project.items.IngredientList;
import org.androidtown.seobang_term_project.recycler.adapters.IngredientAdapter;
import org.androidtown.seobang_term_project.recycler.adapters.IngredientListAdapter;
import org.androidtown.seobang_term_project.recycler.viewholders.IngredientViewHolder;
import org.androidtown.seobang_term_project.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IngredientSelectActivity extends BaseActivity implements IngredientViewHolder.Delegate {

    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "test_ingredient.db";

    public SQLiteDatabase db;

    public Cursor cursor;

    protected @BindView(R.id.edt)
    EditText edt;
    protected @BindView(R.id.btnSelect)
    ImageButton btnSelect;
    protected @BindView(R.id.showRecipeFromIngredient)
    Button showRecipe;
    protected @BindView(R.id.resetButton)
    Button resetButton;
    protected @BindView(R.id.edtRecipeCode)
    TextView edtRecipeCode;
    protected @BindView(R.id.edtIngredientOrder)
    TextView edtIngredientOrder;
    protected @BindView(R.id.edtIngredientName)
    TextView edtIngredientName;
    protected @BindView(R.id.edtIngredientAmount)
    TextView edtIngredientAmount;
    protected @BindView(R.id.edtIngredientTypeName)
    TextView edtIngredientTypeName;
    protected @BindView(R.id.ingredient_select_recyclerView)
    RecyclerView recyclerView;
    protected @BindView(R.id.selectedIngredient)
    RecyclerView resultRecyclerView;
    protected @BindView(R.id.result_layout)
    LinearLayout result_layout;

    String result = "";

    @OnClick(R.id.showRecipeFromIngredient)
    public void onshowRecipe(View view) {
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

    private int count = 0;
    private IngredientListAdapter adapter;
    private IngredientAdapter resultAdapter;
    private Ingredient newIngredient;
    List<Ingredient> ingredients_meat;
    List<Ingredient> ingredients_fish ;
    List<Ingredient> ingredients_veget;
    List<Ingredient> ingredients_mushroom;
    List<Ingredient> ingredients_seasoning;
    List<Ingredient> ingredients_others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_ingredient_select);

        initData();
        DBUtils.setDB(this, ROOT_DIR, DB_Name);

        this.db = DatabaseFactory.create(this, DB_Name);
        SharedPreferences preferences = getSharedPreferences("Accuracy", MODE_PRIVATE);
        int accuracy = preferences.getInt("Accuracy", 50);
        Toast.makeText(getApplicationContext(), String.valueOf(accuracy), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnSelect)
    public void onbtnSelect(View view) {
        Cursor countCursor = db.rawQuery("SELECT count(*) FROM recipe_ingredient_info WHERE ingredient_name=\"" + edt.getText().toString() + "\"", null);
        countCursor.getCount();
        countCursor.moveToNext();
        int cnt = countCursor.getInt(0);
        int check = 1;
        countCursor.close();

        String compareString;

        for(int i = 0; i < ingredients_meat.size(); i++){
            compareString = ingredients_meat.get(i).getIngredientType();
            if(edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 고기류 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for(int i = 0; i < ingredients_fish.size(); i++) {
            compareString = ingredients_fish.get(i).getIngredientType();
            if(edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 해산물 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for(int i = 0; i < ingredients_mushroom.size(); i++){
            compareString = ingredients_mushroom.get(i).getIngredientType();
            if(edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 버섯류 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for(int i = 0 ; i <ingredients_others.size(); i++){
            compareString = ingredients_others.get(i).getIngredientType();
            if(edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 기타재료 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for(int i = 0; i < ingredients_seasoning.size(); i++){
            compareString = ingredients_seasoning.get(i).getIngredientType();
            if(edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 양념 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for(int i = 0 ; i <ingredients_veget.size(); i++){
            compareString = ingredients_veget.get(i).getIngredientType();
            if(edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 채소류 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }

        if (cnt != 0) {
            Ingredient newIngredient = new Ingredient(edt.getText().toString(), R.drawable.spoon);
            newIngredient.setListItem(false);
            resultAdapter.addItem(newIngredient);
            result = checkIsInResultForString(edt.getText().toString(), result);
        } else {
            if(check == 1)
                Toast.makeText(getApplicationContext(), "해당 재료는 없습니다.", Toast.LENGTH_LONG).show();
        }
        edt.setText("");
//        result_layout.setVisibility(View.VISIBLE);
    }

    private void initIngredients(){
     ingredients_meat = new ArrayList<>();
     ingredients_fish = new ArrayList<>();
     ingredients_veget = new ArrayList<>();
     ingredients_mushroom = new ArrayList<>();
     ingredients_seasoning = new ArrayList<>();
     ingredients_others = new ArrayList<>();
    }

    //initialize data
    private void initData() {
        initIngredients();
        //List<Ingredient> ingredients_meat = new ArrayList<>();
        ingredients_meat.add(new Ingredient("돼지고기", R.drawable.meat));
        ingredients_meat.add(new Ingredient("돼지갈비", R.drawable.galbi));
        ingredients_meat.add(new Ingredient("소고기", R.drawable.beaf));
        ingredients_meat.add(new Ingredient("닭고기", R.drawable.whole_chicken));
        ingredients_meat.add(new Ingredient("닭가슴살", R.drawable.chicken));
        ingredients_meat.add(new Ingredient("닭다리", R.drawable.chicken_leg));

        //List<Ingredient> ingredients_fish = new ArrayList<>();
        ingredients_fish.add(new Ingredient("오징어", R.drawable.squid));
        ingredients_fish.add(new Ingredient("새우", R.drawable.shrimp));
        ingredients_fish.add(new Ingredient("조개", R.drawable.shellfish));
        ingredients_fish.add(new Ingredient("낙지", R.drawable.nakji));
        ingredients_fish.add(new Ingredient("홍합", R.drawable.mussel));
        ingredients_fish.add(new Ingredient("바지락", R.drawable.bazirak));
        ingredients_fish.add(new Ingredient("고등어", R.drawable.fish2));
        ingredients_fish.add(new Ingredient("참치", R.drawable.fish1));
        ingredients_fish.add(new Ingredient("굴", R.drawable.oyster));
        ingredients_fish.add(new Ingredient("멸치", R.drawable.myeolchi));
        ingredients_fish.add(new Ingredient("꽁치", R.drawable.fish1));
        ingredients_fish.add(new Ingredient("갈치", R.drawable.fish4));
        ingredients_fish.add(new Ingredient("쭈꾸미", R.drawable.fish7));
        ingredients_fish.add(new Ingredient("도미", R.drawable.fish8));

        //List<Ingredient> ingredients_veget = new ArrayList<>();
        ingredients_veget.add(new Ingredient("마늘", R.drawable.garlic));
        ingredients_veget.add(new Ingredient("무", R.drawable.radish));
        ingredients_veget.add(new Ingredient("양파", R.drawable.onion));
        ingredients_veget.add(new Ingredient("고추", R.drawable.pepper));
        ingredients_veget.add(new Ingredient("콩나물", R.drawable.kongnamul));
        ingredients_veget.add(new Ingredient("피망", R.drawable.pimento));
        ingredients_veget.add(new Ingredient("생강", R.drawable.saenggang));
        ingredients_veget.add(new Ingredient("감자", R.drawable.potato));
        ingredients_veget.add(new Ingredient("쑥", R.drawable.ssuk));
        ingredients_veget.add(new Ingredient("오이", R.drawable.cucumber));
        ingredients_veget.add(new Ingredient("배추", R.drawable.baechu));
        ingredients_veget.add(new Ingredient("부추", R.drawable.buchu));
        ingredients_veget.add(new Ingredient("당근", R.drawable.carrot));
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

        //List<Ingredient> ingredients_mushroom = new ArrayList<>();
        ingredients_mushroom.add(new Ingredient("표고 버섯", R.drawable.pyogo));
        ingredients_mushroom.add(new Ingredient("느타리 버섯", R.drawable.neutari));
        ingredients_mushroom.add(new Ingredient("팽이 버섯", R.drawable.pengi));
        ingredients_mushroom.add(new Ingredient("양송이 버섯", R.drawable.yangsongi));
        ingredients_mushroom.add(new Ingredient("목이 버섯", R.drawable.moki));
        ingredients_mushroom.add(new Ingredient("새송이 버섯", R.drawable.saesongi));

        //List<Ingredient> ingredients_seasoning = new ArrayList<>();
        ingredients_seasoning.add(new Ingredient("소금", R.drawable.salt));
        ingredients_seasoning.add(new Ingredient("설탕", R.drawable.sugar));
        ingredients_seasoning.add(new Ingredient("청주", R.drawable.cheongju));
        ingredients_seasoning.add(new Ingredient("된장", R.drawable.doenjang));
        ingredients_seasoning.add(new Ingredient("고추장", R.drawable.gochujang));
        ingredients_seasoning.add(new Ingredient("식초", R.drawable.vinegar));
        ingredients_seasoning.add(new Ingredient("간장", R.drawable.ganjang));
        ingredients_seasoning.add(new Ingredient("토마토 케찹", R.drawable.ketchup));
        ingredients_seasoning.add(new Ingredient("깨소금", R.drawable.ggaesalt));
        ingredients_seasoning.add(new Ingredient("생강즙", R.drawable.gingerjuice));
        ingredients_seasoning.add(new Ingredient("고춧가루", R.drawable.gochugaru));
        ingredients_seasoning.add(new Ingredient("국간장", R.drawable.gukganjang));
        ingredients_seasoning.add(new Ingredient("후추", R.drawable.huchu));
        ingredients_seasoning.add(new Ingredient("진간장", R.drawable.jinganjang));
        ingredients_seasoning.add(new Ingredient("맛술", R.drawable.matsul));
        ingredients_seasoning.add(new Ingredient("새우젓", R.drawable.saeujeot));

        //List<Ingredient> ingredients_others = new ArrayList<>();
        ingredients_others.add(new Ingredient("버터", R.drawable.butter));
        ingredients_others.add(new Ingredient("밥", R.drawable.bob));
        ingredients_others.add(new Ingredient("다시마", R.drawable.dasima));
        ingredients_others.add(new Ingredient("두부", R.drawable.tofu));
        ingredients_others.add(new Ingredient("배추김치", R.drawable.kimchi));
        ingredients_others.add(new Ingredient("쌀", R.drawable.rice));
        ingredients_others.add(new Ingredient("맛술", R.drawable.matsul));
        ingredients_others.add(new Ingredient("계란", R.drawable.egg));
        ingredients_others.add(new Ingredient("통깨", R.drawable.tongggae));
        ingredients_others.add(new Ingredient("식용유", R.drawable.sikyongoil));
        ingredients_others.add(new Ingredient("밀가루", R.drawable.wheat_flour));
        ingredients_others.add(new Ingredient("찹쌀", R.drawable.chapssal));
        ingredients_others.add(new Ingredient("가래떡", R.drawable.garaeddeock));
        ingredients_others.add(new Ingredient("밤", R.drawable.chestnut));
        ingredients_others.add(new Ingredient("스파게티 면", R.drawable.spaghetti));
        ingredients_others.add(new Ingredient("팥", R.drawable.red_been));
        ingredients_others.add(new Ingredient("당면", R.drawable.dangmyoen));
        ingredients_others.add(new Ingredient("미역", R.drawable.miyeok));
        ingredients_others.add(new Ingredient("우유", R.drawable.milk));
        ingredients_others.add(new Ingredient("찹쌀가루", R.drawable.chapssalgaru));
        ingredients_others.add(new Ingredient("게맛살", R.drawable.gaematsal));

        adapter = new IngredientListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.addIngredientListItem(new IngredientList("고기류", ingredients_meat));
        adapter.addIngredientListItem(new IngredientList("해산물", ingredients_fish));
        adapter.addIngredientListItem(new IngredientList("채소류", ingredients_veget));
        adapter.addIngredientListItem(new IngredientList("버섯류", ingredients_mushroom));
        adapter.addIngredientListItem(new IngredientList("양념", ingredients_seasoning));
        adapter.addIngredientListItem(new IngredientList("기타재료", ingredients_others));

        resultAdapter = new IngredientAdapter(this);
        resultRecyclerView.setAdapter(resultAdapter);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @OnClick(R.id.resetButton)
    public void setResetButton(View view) {
        resultAdapter.removeAll();
        result = "";
        initData();
    }

    @Override
    public void onItemClick(Ingredient ingredient, boolean isOnClicked) {
        if (ingredient.getIsListItem()) { // 리스트 아이템을 클릭한경우
            Toast.makeText(this, ingredient.getIngredientType() + "clicked: " + isOnClicked, Toast.LENGTH_SHORT).show();
            result = checkIsInResult(ingredient, result);
            Log.e("result", result);
            if (isOnClicked) {
                newIngredient = new Ingredient(ingredient.getIngredientType(), ingredient.getImage());
                newIngredient.setListItem(false);
                resultAdapter.addItem(newIngredient);

            } else {
                resultAdapter.removeItem(ingredient);
            }
        }
    }

    public String checkIsInResult(Ingredient ingredient, String result) {
        String ingredientName = ingredient.getIngredientType();
        if (result.indexOf(ingredientName) == -1)
            result += ingredient.getIngredientType() + ",";
        else
            result = result.replace(ingredientName + ",", "");

        return result;
    }

    public String checkIsInResultForString(String input, String result) {
        if (result.indexOf(input) == -1)
            result += input + ",";
        else
            result = result.replace(input + ",", "");
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}