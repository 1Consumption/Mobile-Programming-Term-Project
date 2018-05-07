package org.androidtown.seobang_term_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuSelectActivity extends AppCompatActivity {

    //recycler view 에서 section 을 나눠서

    private List<Ingredient> ingredientList = new ArrayList<>();
    private RecyclerView recyclerView;
    private IngredientAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new IngredientAdapter(ingredientList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareIngredientData();
    }

    private void prepareIngredientData(){
        //ingredient 추가 (디비추가?)
        Ingredient ingredient = new Ingredient();
    }
}
