package org.androidtown.seobang_term_project.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.androidtown.seobang_term_project.ui.history.HistoryActivity;
import org.androidtown.seobang_term_project.ui.ingredient.IngredientSelectActivity;
import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.ui.info.InfoActivity;
import org.androidtown.seobang_term_project.ui.recipe.RecipeSelectActivity;
import org.androidtown.seobang_term_project.ui.tutorial.TutorialActivity;
import org.androidtown.seobang_term_project.utils.PowerMenuUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class MainActivity extends AppCompatActivity {

    private PowerMenu powerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        powerMenu = PowerMenuUtils.getHamburgerPowerMenu(this, this, powerMenuItemOnMenuItemClickListener);
    }

    @OnClick(R.id.recipe_select_button) ///카드뷰로 이름 바꿔주기
    public void recipeButton(View view) {
        Intent intent = new Intent(getApplicationContext(), RecipeSelectActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.start_select_button)
    public void startButton(View view) {
        Intent intent = new Intent(getApplicationContext(), IngredientSelectActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.history_button)
    public void historyButton(View view){
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.info_button)
    public void infoButton(View view) {
        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.main_more})
    public void settingButton(View view) {
        powerMenu.showAsAnchorRightTop(view);
    }

    private OnMenuItemClickListener<PowerMenuItem> powerMenuItemOnMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            switch (position) {
                case 0:
                    Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(getApplication(), "메뉴2", Toast.LENGTH_SHORT).show();
                    //여기도 음식 정확도 설정 해주는 부분 만들어 줘야 함 라이오 설정 뭐 이런 팝업으로
                    break;
                default:
                    break;
            }
            powerMenu.dismiss();
        }
    };

    @Override
    public void onBackPressed() {
        if(powerMenu.isShowing())
            powerMenu.dismiss();
        else
            super.onBackPressed();
    }
}
