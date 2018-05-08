package org.androidtown.seobang_term_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MenuSelectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //recycler view 에서 section 을 나눠서

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        initLayout();
        initData();

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

