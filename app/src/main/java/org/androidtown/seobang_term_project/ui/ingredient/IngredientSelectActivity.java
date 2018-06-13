package org.androidtown.seobang_term_project.ui.ingredient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import org.androidtown.seobang_term_project.ui.Accuracy.AccuracyActivity;
import org.androidtown.seobang_term_project.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @When:
 * This activity is executed on the screen for selecting ingredients.
 *
 * @Functions
 * If user press the select finish button without selecting a ingredient, an toast alarm will appear
 * It tells user whether or not the ingredient the user searched for is in the database.
 * It shows all the ingredients in the database.
 * Sends the selected ingredients to the RecipeFromIngredientActivity.
 *
 * @Technique
 * Send data to another activity using intent and bundle.
 * Use sqlite's query to retrieve it from the database.
 *
 */

public class IngredientSelectActivity extends BaseActivity implements IngredientViewHolder.Delegate {

    public final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public final String DB_Name = "ingredient_info_modify_weight_2.db";

    public SQLiteDatabase db;

    public Cursor cursor;

    protected @BindView(R.id.edt)
    EditText edt;
    protected @BindView(R.id.btnSelect)
    ImageButton btnSelect;
    protected @BindView(R.id.setAccuracy)
    ImageButton btnAccuracy;
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
            Toast.makeText(getApplicationContext(), "아무것도 고르지 않았습니다.", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), RecipeFromIngredientActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("result", result.substring(0, result.length() - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private int count = 0;
    private IngredientListAdapter adapter;
    private IngredientAdapter resultAdapter;
    private Ingredient newIngredient;
    List<Ingredient> ingredients_meat;
    List<Ingredient> ingredients_fish;
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

        for (int i = 0; i < ingredients_meat.size(); i++) {
            compareString = ingredients_meat.get(i).getIngredientType();
            if (edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 고기류 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for (int i = 0; i < ingredients_fish.size(); i++) {
            compareString = ingredients_fish.get(i).getIngredientType();
            if (edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 해산물 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for (int i = 0; i < ingredients_mushroom.size(); i++) {
            compareString = ingredients_mushroom.get(i).getIngredientType();
            if (edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 버섯류 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for (int i = 0; i < ingredients_others.size(); i++) {
            compareString = ingredients_others.get(i).getIngredientType();
            if (edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 기타재료 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for (int i = 0; i < ingredients_seasoning.size(); i++) {
            compareString = ingredients_seasoning.get(i).getIngredientType();
            if (edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 양념 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }
        for (int i = 0; i < ingredients_veget.size(); i++) {
            compareString = ingredients_veget.get(i).getIngredientType();
            if (edt.getText().toString().compareTo(compareString) == 0) { //같다 즉 있으면
                Toast.makeText(getApplicationContext(), "해당 재료는 채소류 리스트에 이미 존재합니다.", Toast.LENGTH_LONG).show();
                cnt = 0;
                check = 0;
            }
        }


        if (cnt != 0) {
            if (result.contains(edt.getText().toString())) {
                Toast.makeText(getApplicationContext(), "이미 선택된 재료 입니다.", Toast.LENGTH_LONG).show();
            } else {
                Ingredient newIngredient = new Ingredient(edt.getText().toString(), R.drawable.spoon);
                newIngredient.setListItem(false);
                resultAdapter.addItem(newIngredient);
                result = checkIsInResultForString(edt.getText().toString(), result);
            }
        } else {
            if (check == 1) {
                Toast.makeText(getApplicationContext(), "해당 재료는 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
        edt.setText("");
    }

    private void initIngredients() {
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
        ingredients_meat.add(new Ingredient("닭가슴살", R.drawable.chicken));
        ingredients_meat.add(new Ingredient("닭고기", R.drawable.whole_chicken));
        ingredients_meat.add(new Ingredient("닭다리", R.drawable.chicken_leg));
        ingredients_meat.add(new Ingredient("돼지갈비", R.drawable.galbi));
        ingredients_meat.add(new Ingredient("돼지고기", R.drawable.meat));
        ingredients_meat.add(new Ingredient("소고기", R.drawable.beaf));


        //List<Ingredient> ingredients_fish = new ArrayList<>();
        ingredients_fish.add(new Ingredient("갈치", R.drawable.fish4));
        ingredients_fish.add(new Ingredient("고등어", R.drawable.fish2));
        ingredients_fish.add(new Ingredient("굴", R.drawable.oyster));
        ingredients_fish.add(new Ingredient("꽁치", R.drawable.fish1));
        ingredients_fish.add(new Ingredient("낙지", R.drawable.nakji));
        ingredients_fish.add(new Ingredient("도미", R.drawable.fish8));
        ingredients_fish.add(new Ingredient("멸치", R.drawable.myeolchi));
        ingredients_fish.add(new Ingredient("바지락", R.drawable.bazirak));
        ingredients_fish.add(new Ingredient("새우", R.drawable.shrimp));
        ingredients_fish.add(new Ingredient("오징어", R.drawable.squid));
        ingredients_fish.add(new Ingredient("조개", R.drawable.shellfish));
        ingredients_fish.add(new Ingredient("쭈꾸미", R.drawable.fish7));
        ingredients_fish.add(new Ingredient("참치", R.drawable.tunacan));
        ingredients_fish.add(new Ingredient("홍합", R.drawable.mussel));

        //List<Ingredient> ingredients_veget = new ArrayList<>();
        ingredients_veget.add(new Ingredient("가지", R.drawable.eggplant));
        ingredients_veget.add(new Ingredient("감자", R.drawable.potato_2));
        ingredients_veget.add(new Ingredient("고구마", R.drawable.sweet_potato));
        ingredients_veget.add(new Ingredient("고사리", R.drawable.gosari));
        ingredients_veget.add(new Ingredient("고추", R.drawable.pepper));
        ingredients_veget.add(new Ingredient("깻잎", R.drawable.ggaet));
        ingredients_veget.add(new Ingredient("당근", R.drawable.carrot_2));
        ingredients_veget.add(new Ingredient("대파", R.drawable.daepa));
        ingredients_veget.add(new Ingredient("도라지", R.drawable.doraji));
        ingredients_veget.add(new Ingredient("마늘", R.drawable.garlic));
        ingredients_veget.add(new Ingredient("무", R.drawable.radish));
        ingredients_veget.add(new Ingredient("무순", R.drawable.musoon));
        ingredients_veget.add(new Ingredient("미나리", R.drawable.minari));
        ingredients_veget.add(new Ingredient("방울 토마토", R.drawable.bangultomato));
        ingredients_veget.add(new Ingredient("배추", R.drawable.baechu));
        ingredients_veget.add(new Ingredient("부추", R.drawable.buchu));
        ingredients_veget.add(new Ingredient("상추", R.drawable.lettuce));
        ingredients_veget.add(new Ingredient("샐러리", R.drawable.celery));
        ingredients_veget.add(new Ingredient("생강", R.drawable.saenggang));
        ingredients_veget.add(new Ingredient("숙주", R.drawable.sukju));
        ingredients_veget.add(new Ingredient("시금치", R.drawable.spinach));
        ingredients_veget.add(new Ingredient("실파", R.drawable.silpa));
        ingredients_veget.add(new Ingredient("쑥", R.drawable.ssuk));
        ingredients_veget.add(new Ingredient("애호박", R.drawable.childpumpkin));
        ingredients_veget.add(new Ingredient("양배추", R.drawable.cabbage));
        ingredients_veget.add(new Ingredient("양상추", R.drawable.yangsangchu));
        ingredients_veget.add(new Ingredient("양파", R.drawable.onion));
        ingredients_veget.add(new Ingredient("오이", R.drawable.cucumber));
        ingredients_veget.add(new Ingredient("죽순", R.drawable.juksoon));
        ingredients_veget.add(new Ingredient("콩나물", R.drawable.kongnamul));
        ingredients_veget.add(new Ingredient("토마토", R.drawable.tomato));
        ingredients_veget.add(new Ingredient("파프리카", R.drawable.pap));
        ingredients_veget.add(new Ingredient("피망", R.drawable.pimento));
        ingredients_veget.add(new Ingredient("호박", R.drawable.pumpkin));

        //List<Ingredient> ingredients_mushroom = new ArrayList<>();
        ingredients_mushroom.add(new Ingredient("느타리 버섯", R.drawable.neutari));
        ingredients_mushroom.add(new Ingredient("목이 버섯", R.drawable.moki));
        ingredients_mushroom.add(new Ingredient("새송이 버섯", R.drawable.saesongi));
        ingredients_mushroom.add(new Ingredient("양송이 버섯", R.drawable.yangsongi));
        ingredients_mushroom.add(new Ingredient("팽이 버섯", R.drawable.pengi));
        ingredients_mushroom.add(new Ingredient("표고 버섯", R.drawable.pyogo));

        //List<Ingredient> ingredients_seasoning = new ArrayList<>();
        ingredients_seasoning.add(new Ingredient("간장", R.drawable.ganjang));
        ingredients_seasoning.add(new Ingredient("고추장", R.drawable.gochujang));
        ingredients_seasoning.add(new Ingredient("고춧가루", R.drawable.gochugaru));
        ingredients_seasoning.add(new Ingredient("국간장", R.drawable.gukganjang));
        ingredients_seasoning.add(new Ingredient("굴소스", R.drawable.gulsauce));
        ingredients_seasoning.add(new Ingredient("깨소금", R.drawable.ggaesalt));
        ingredients_seasoning.add(new Ingredient("된장", R.drawable.doenjang));
        ingredients_seasoning.add(new Ingredient("마요네즈", R.drawable.mayomayo));
        ingredients_seasoning.add(new Ingredient("머스타드", R.drawable.must));
        ingredients_seasoning.add(new Ingredient("물엿", R.drawable.mulyeot));
        ingredients_seasoning.add(new Ingredient("새우젓", R.drawable.saeujeot));
        ingredients_seasoning.add(new Ingredient("생강즙", R.drawable.gingerjuice));
        ingredients_seasoning.add(new Ingredient("설탕", R.drawable.sugar));
        ingredients_seasoning.add(new Ingredient("소금", R.drawable.salt));
        ingredients_seasoning.add(new Ingredient("식초", R.drawable.vinegar));
        ingredients_seasoning.add(new Ingredient("진간장", R.drawable.jinganjang));
        ingredients_seasoning.add(new Ingredient("청주", R.drawable.cheongju));
        ingredients_seasoning.add(new Ingredient("토마토 케찹", R.drawable.ketchup));
        ingredients_seasoning.add(new Ingredient("후추", R.drawable.huchu));


        //List<Ingredient> ingredients_others =
        ingredients_others.add(new Ingredient("가래떡", R.drawable.garaeddeock));
        ingredients_others.add(new Ingredient("게맛살", R.drawable.gaematsal));
        ingredients_others.add(new Ingredient("계란", R.drawable.egg));
        ingredients_others.add(new Ingredient("김", R.drawable.kim));
        ingredients_others.add(new Ingredient("꿀", R.drawable.honey));
        ingredients_others.add(new Ingredient("녹말", R.drawable.nokmal));
        ingredients_others.add(new Ingredient("다시마", R.drawable.dasima));
        ingredients_others.add(new Ingredient("당면", R.drawable.dangmyoen));
        ingredients_others.add(new Ingredient("대추", R.drawable.daechu));
        ingredients_others.add(new Ingredient("두부", R.drawable.tofu));
        ingredients_others.add(new Ingredient("미역", R.drawable.miyeok));
        ingredients_others.add(new Ingredient("밀가루", R.drawable.wheat_flour));
        ingredients_others.add(new Ingredient("밤", R.drawable.chestnut));
        ingredients_others.add(new Ingredient("밥", R.drawable.bob));
        ingredients_others.add(new Ingredient("배", R.drawable.bae));
        ingredients_others.add(new Ingredient("배추김치", R.drawable.kimchi));
        ingredients_others.add(new Ingredient("버터", R.drawable.butter));
        ingredients_others.add(new Ingredient("베이컨", R.drawable.bacon));
        ingredients_others.add(new Ingredient("소면", R.drawable.somyeon));
        ingredients_others.add(new Ingredient("스파게티 면", R.drawable.spaghetti));
        ingredients_others.add(new Ingredient("식용유", R.drawable.sikyongoil));
        ingredients_others.add(new Ingredient("쌀", R.drawable.rice));
        ingredients_others.add(new Ingredient("올리브유", R.drawable.oliveoil));
        ingredients_others.add(new Ingredient("우유", R.drawable.milk));
        ingredients_others.add(new Ingredient("잣", R.drawable.jat));
        ingredients_others.add(new Ingredient("참기름", R.drawable.chamgireum));
        ingredients_others.add(new Ingredient("찹쌀", R.drawable.chapssal));
        ingredients_others.add(new Ingredient("찹쌀가루", R.drawable.chapssalgaru));
        ingredients_others.add(new Ingredient("통깨", R.drawable.tongggae));
        ingredients_others.add(new Ingredient("파슬리", R.drawable.paseul));
        ingredients_others.add(new Ingredient("팥", R.drawable.red_been));
        ingredients_others.add(new Ingredient("햄", R.drawable.ham));


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

    @OnClick(R.id.setAccuracy)
    public void setAccuracy(View view) {

        Intent intent = new Intent(getApplicationContext(), AccuracyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onItemClick(Ingredient ingredient, boolean isOnClicked) {
        if (ingredient.getIsListItem()) { // 리스트 아이템을 클릭한경우
            result = checkIsInResult(ingredient, result);
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
