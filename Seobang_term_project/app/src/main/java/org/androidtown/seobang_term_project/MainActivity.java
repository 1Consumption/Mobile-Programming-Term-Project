package org.androidtown.seobang_term_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); //oncreate 안에 있는게 중요: 처음에 initialize

    }

    @OnClick(R.id.recipe_select_button)
    public void recipeButton(View view){
        Intent intent = new Intent(getApplicationContext(), RecipeSelectActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.start_select_button) //annotation
    public void startButton(View view) {
        Intent intent = new Intent(getApplicationContext(), IngredientSelectActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.info_button)
    public void infoButton(View view) {
        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.setting_button)
    public void settingButton(View view) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), view); //v는 클릭된 뷰를 의미

        getMenuInflater().inflate(R.menu.settingmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.m1:
                        Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.m2:
                        Toast.makeText(getApplication(), "메뉴2", Toast.LENGTH_SHORT).show();
                        //여기도 음식 정확도 설정 해주는 부분 만들어 줘야 함 라이오 설정 뭐 이런 팝업으로
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        popup.show();
    }
}
