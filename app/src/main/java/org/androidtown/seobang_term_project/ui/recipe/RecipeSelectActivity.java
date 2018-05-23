package org.androidtown.seobang_term_project.ui.recipe;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.items.Recipe;
import org.androidtown.seobang_term_project.recycler.adapters.RecipeAdapter;
import org.androidtown.seobang_term_project.recycler.viewholders.RecipeViewHolder;
import org.androidtown.seobang_term_project.utils.DBUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RecipeSelectActivity extends BaseActivity implements RecipeViewHolder.Delegate {

    private static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    private static final String DB_Name = "recipe_basic_information.db";
    private static final String TABLE_NAME = "recipe_basic_information";
    private static final String DB_Name2 = "recipe_process.db";
    private static final String TABLE_NAME2 = "recipe_process";

    private SQLiteDatabase db_info;
    private SQLiteDatabase db_process;

    private Cursor cursor;
    private RecipeAdapter mAdapter;

    private int index = 2;
    private boolean isLoading = false;

    static final String[] menus = new String[] {"콩비지동그랑땡", "누드김밥", "쪽파 새우강회", "카레토마토달걀볶음밥", "빈대떡", "송편", "배추속댓국", "낙지불고기", "양파전", "봄동겉절이","식빵고구마파이", "배추만두", "당근잎 감자전", "단호박 고등어조림", "나물 월남쌈", "애호박 구이", "오이보트카나페", "가지두부스테이크", "깻잎장아찌", "고추장아찌", "민트라임모히또", "어린잎샌드위치", "호박무침", "가지무침과 호박잎쌈밥", "상추 치커리 오징어 초침무", "가지그라탕", "매운가지볶음", "고구마잎무침", "닭가슴살 호박말이", "토마토미니새송이볶음", "오이동치미", "돛나물 부추 오이무침", "가지말이구이", "바질페스토 스파게티", "시금치들깨수제비", "단호박옥수수치즈구이", "감동젓무김치", "보쌈김치", "시래기돼지갈비찜", "흑임자삼계죽", "한우토마토스튜", "시금치표고된장무른밥", "시금치배미음죽", "쌀샐러드", "까르보나라 (한국식)", "송어샐러드", "잉어찜", "메기매운탕", "비빔냉면", "미소된장국", "꽁치김치찌개", "떡만두국", "채소피클", "발효초요구르트", "쫄면", "김치우동", "해물순두부찌개", "해물매운탕", "새송이쌈장구이", "해물볶음밥", "오코노미야키", "두부조림", "돼지고기김치찌개", "해물찌개", "고추잡채와꽃빵", "두부파프리카전", "참치죽", "고추장닭강정", "된장채소수제비", "치킨샐러드", "무쌈말이", "바베큐립", "부추올리브오일무침", "버섯잡채", "가지김치", "상추미소국", "크림소스파스타", "녹두고물호박떡케이크", "토란닭고기찜", "삼색나물월남쌈", "소꼬리찜", "왕새우구이와구운채소", "전어회무침", "대대로닭칼국수", "서리태콩국수", "모듬콩수프", "우무콩냉국", "녹차콩국수", "시금치나물", "죽순회와미나리강회", "전복죽", "새우죽", "낙지연포탕", "시금치계란말이", "돼지갈비레몬찜", "파프리카볶음밥", "붉은갓동치미", "깻잎말이김치", "된장찌개", "근대된장국", "고구마강정", "고추장", "된장", "간장", "얼갈이열무물김치", "깻잎조림", "쌀국수", "마늘볶음국수", "수삼치즈샐러드", "채소치즈죽", "치즈멸치볼", "새우겨자채", "갈치조림"
            , "갈치구이", "고등어조림", "고등어양념구이", "돌솥비빔밥", "낙지불고기", "무말랭이무침", "다시마채소말이", "돼지고기우엉말이조림", "북어해장국밥", "통마늘장아찌", "생선전", "생굴채소무침", "고등어튀김케첩조림", "쇠고기덮밥", "브로콜리베이컨말이", "맑은대구탕", "미역수제비", "양파쌈파이", "라조기", "단호박구이", "돼지불고기", "녹두죽", "간장게장", "해물샤브샤브", "불고기꽃만두", "쟁반라면", "부추잡채", "오징어볶음과소면", "참치김치찌개", "연어오픈샌드위치", "떡꼬치", "오징어섞어찌개", "어묵볶음", "닭가슴살해파리샐러드", "골뱅이볶음", "가지튀김", "김치홍합국", "라조육", "양상추참치샐러드", "조기찜", "동래파전", "버섯매운탕", "우묵냉채", "순두부", "마파두부덮밥", "채소영양밥", "오징어삼겹살볶음", "볶음우동", "돈까스덮밥", "토마토해산물스파게티", "장어계란말이", "장어덮밥", "모듬전", "해초무침", "꽃게해물탕", "쇠고기버섯덮밥", "소바정식", "샤브샤브", "채소스프", "해산물두부수프", "깐풍새우", "연어알초밥", "돈부리", "콩나물미나리무침", "만두샐러드", "화전", "무초김치", "오곡주먹밥", "캘리포니아롤", "삼색태극말이초밥", "그린파스타", "제육겨자쌈", "시금치된장죽", "고구마그라탱", "두부두루치기", "고등어된장조림", "무나물", "월남쌈", "해물스파게티", "단호박튀김", "파래무침", "안심스테이크", "해물국시", "무생채", "오징어젓갈", "꽁치구이", "호박양파국", "상추채소무침", "인절미", "콩나물비빔밥", "계란찜", "치킨수프", "마늘빵", "감자수프", "기스면", "충무김밥", "고추부각", "불고기찹쌀구이", "대구탕", "모듬채소볶음", "오징어덮밥", "홍합미역국", "양송이버섯죽", "애호박무침", "오징어도라지생채", "바지락칼국수", "오징어채볶음", "김치볶음밥", "닭개장", "버섯파스타", "볶음쌀국수", "물만두", "새송이산적", "치킨버섯찜", "냉고기쌈", "부추부침개", "버섯청국장찌개", "가지쇠고기볶음", "바지락볶음", "류산슬", "고등어김치조림", "라볶이", "아욱국", "비빔쌀국수", "된장깻잎장아찌", "쑥브리오슈", "버섯잡채밥", "다시마볶음", "열무비빔밥", "베이컨꼬치구이", "닭갈비", "순대볶음", "치킨데리야끼", "코다리조림", "감자그라탕", "잔치국수", "김치쌈밥", "한국식타코샐러드", "간장떡볶이", "치즈돈까스", "김치꽁치조림", "옥수수볶음밥", "해물떡꼬치", "브로콜리크림수프"
            , "연어양상추쌈", "두부알찜", "상추겉절이비빔밥", "오징어풋마늘산적", "돼지고기표고볶음", "꽃만두국(완당국)", "아스파라거스새우볶음", "치킨롤", "해파리해물냉채", "호박두부찌개", "김치두부쌈", "닭고기콩나물덮밥", "오징어통구이", "참나물고추장무침", "숙주미나리무침", "두릅된장무침", "달래장김치", "달래굴파전", "고들빼기김치", "삼색수제비", "유부미역된장국", "참치주먹밥", "약과", "두부채소냉채", "떡잡채", "사색나물", "영양채소밥", "닭다리굴소스볶음", "어묵꼬치", "콩나물버섯덮밥", "오징어탕수", "채소비빔소면", "김치피자", "별미밥", "잔멸치된장볶음", "불고기낙지전골", "단팥죽", "찬밥전", "사골우거지탕", "보쌈김치", "배추밤김치", "제육불고기", "떡볶이", "돼지갈비찜", "홍합탕", "김치주먹밥", "버섯두부찌개", "두부굴찌개", "생태매운탕", "고춧기름", "닭고기카레튀김", "가지된장찜", "홍차계란장조림", "두부카나페", "두부스테이크", "해물밥전", "버섯만두전골", "생선초밥", "코다리찜", "제육배추찜", "짬뽕해물탕밥", "오징어숙회", "오곡밥쌈밥정식", "김치수제비", "감자국수", "사골탕", "잼(사과, 딸기, 포도)", "두부오믈렛", "오징어산적", "갈비떡볶이", "삼겹살치즈구이", "녹차수제비", "닭꼬치구이", "우유두부", "삼겹살깻잎전", "초교탕", "양상추튀김", "해물전골", "고추장스파게티", "육개장", "명란젓찌개", "쇠고기무국", "김치어묵", "고추장아찌", "잣죽", "새우만두", "왜된장국", "조개맑은국", "어묵국", "계란말이주먹밥", "찰밥주먹밥", "김치채소쌈", "시금치된장국", "영양돌솥밥", "오이피클", "오징어찌개", "양배춧국", "찬밥맛탕", "김치동그랑땡", "봄동바지락볶음", "닭고기명란튀김", "쑥버섯볶음", "달래된장찌개", "모듬쌈밥", "콩나물무밥", "찰밥", "맑은떡국", "해물잡채", "쇠고기완자찜", "두부탕", "팥시루떡", "깐풍기", "수정과", "절편", "떡국", "어묵닭고기조림", "고구마줄기볶음", "팥타르트", "동지팥죽", "찬밥지짐이"
            , "가래떡꼬치", "꽃상추쌈", "오징어순대", "닭날개튀김", "김치채소볶음", "쌈장", "잡채", "고등어무조림", "쇠고기전골", "화양적", "삼겹살채소말이", "오렌지건포도빵", "아귀찜", "곱창전골", "부대찌개", "채소수프", "시금치샐러드", "연어샐러드", "과일샐러드", "컬리플라워해물샐러드", "닭고기수삼샐러드", "버섯덮밥", "떡갈비샌드위치", "속채운감자", "유부계란찜", "주먹밥", "삼계탕", "까르보나라스파게티", "육회", "두부양념조림", "골뱅이무침", "떡갈비구이", "채소말이샤브샤브", "가지와마른새우무침", "고등어살튀김과소스", "묵과양념장", "가는파잡채", "감자고추채볶음", "꽁치간장구이", "국수계란말이", "해장라면", "중국식볶음밥", "두부미역냉채", "롤케비지(양배추말이)", "오믈렛", "두부김치", "낙지볶음", "김치적", "꽁치무조림", "모듬초밥", "돼지갈비구이", "부추전", "불고기", "콩국계란찜", "탕수육", "호박전", "죽순볶음", "마른오징어조림", "감자조림", "더덕구이", "배추겉절이", "닭강정", "계란말이", "냉이된장찌개", "오이나물", "해물파전", "연근조림", "쇠고기장조림", "콩자반", "오이지장아찌무침", "깻잎장아찌", "백김치", "파김치", "부추김치", "갈비탕", "꼬리곰탕", "도가니탕", "설렁탕", "곰탕", "콩나물국밥", "선지국", "낙지전골", "국수전골", "샤브샤브", "우거지된장찌개", "알탕", "조기매운탕", "아구탕", "감자탕", "꽃게찌개", "갓김치", "동치미", "굴깍두기", "총각김치", "나박김치", "오이소박이", "열무김치", "통배추김치", "해파리냉채", "오색주먹밥도시락", "유부초밥", "북어국", "콩비지찌개", "김치찌개", "동태찌개", "순두부찌개", "청국장찌개", "홍합꼬치구이", "제육보쌈", "갈비찜", "돈까스", "갈비구이", "생선탕수", "오징어불고기", "마늘장아찌", "양배추말이찜", "콩나물잡채", "팥칼국수", "두부다시마말이", "닭불고기", "갈치무조림", "멸치볶음", "바질토마토두부샐러드", "우엉조림", "콩나물무침", "두부드레싱과 채소샐러드", "부추표고버섯볶음", "죽순표고버섯볶음나물", "팥국수", "쇠고기양송이볶음", "쇠고기산적", "구운감자와도미구이", "재첩국", "해산물샐러드", "오이냉국", "연어까르파치오", "생태국", "미역냉국", "미역국", "무맑은국", "두부조개탕", "두부국", "다시마냉국", "만둣국", "해물국수", "채소국수", "열무김치냉면", "동치미막국수", "냉면", "감자수제비", "오므라이스", "카레라이스", "흑임자죽", "호박죽", "약식", "콩나물밥", " 잡채밥", "오곡밥", "나물비빔밥"
    };

    protected @BindView(R.id.searchRecipe) AutoCompleteTextView searchRecipe;
    protected @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe_select);

        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, menus);
        searchRecipe.setAdapter(adapter);

        this.db_info = DatabaseFactory.create(this, DB_Name);
        this.db_process = DatabaseFactory.create(this, DB_Name2);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mAdapter = new RecipeAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalItemCount, lastVisibleItem;
            int visibleThreshold = 4;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if ((index-1) * 10 <= (lastVisibleItem + visibleThreshold)) {
                    loadMore();
                    isLoading = true;
                }
            }
        });

        for(int i=0; i< 10; i++) {
            mAdapter.addItem(new Recipe(menus[i], getFoodPreviewImage(menus[i])));
        }
    }

    private void loadMore() {
        if((index * 10) <= menus.length) {
            for(int i = (index-1) * 10; i < index * 10; i++) {
                mAdapter.addItem(new Recipe(menus[i], getFoodPreviewImage(menus[i])));
            }
            index++;
        }
    }

    @OnClick(R.id.goToRecipe)
    public void onClickgoToRecipe(View view) {
        Cursor countCursor = db_info.rawQuery("SELECT count(*) FROM " + TABLE_NAME + " WHERE recipe_name=\"" + searchRecipe.getText().toString() + "\"", null);
        countCursor.getCount();
        countCursor.moveToNext();

        int cnt = countCursor.getInt(0);
        countCursor.close();

        String selectedRecipeCode = "";

        if (cnt != 0) {
            cursor = db_info.rawQuery("SELECT recipe_code FROM " + TABLE_NAME + " WHERE recipe_name=\"" + searchRecipe.getText().toString() + "\"", null); //쿼리문
            startManagingCursor(cursor);

            while (cursor.moveToNext()) {
                selectedRecipeCode = cursor.getString(0);
            }
            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("selectedRecipe", selectedRecipeCode);
            bundle.putString("RecipeName", searchRecipe.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            String msg = "Sorry...There is no such food.";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFoodId(String name) {
        cursor = db_info.rawQuery("SELECT recipe_code FROM " + TABLE_NAME + " WHERE recipe_name=\"" + name + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        return cursor.getString(0);
    }

    private String getFoodPreviewImage(String name) {
        cursor = db_info.rawQuery("SELECT recipe_code FROM " + TABLE_NAME + " WHERE recipe_name=\"" + name + "\"", null);
        startManagingCursor(cursor);
        cursor.moveToNext();
        String recipeId = cursor.getString(0);

        cursor = db_process.rawQuery("SELECT process, explanation, url FROM " + TABLE_NAME2 + " WHERE recipe_code=\"" + recipeId + "\"", null);


            strRecipeProcess += result + "+";
            strRecipeProcess += cursor.getString(0) + "&";
            strRecipeProcess += cursor.getString(1) + "|";
            strRecipeProcess += cursor.getString(2) + "#";


        cursor.moveToFirst();
        return cursor.getString(2);
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("selectedRecipe", getFoodId(recipe.getName()));
        bundle.putString("RecipeName", recipe.getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db_info.close();
    }
}
