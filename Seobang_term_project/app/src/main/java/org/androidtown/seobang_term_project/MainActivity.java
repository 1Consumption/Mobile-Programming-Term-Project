package org.androidtown.seobang_term_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button menuButton = findViewById(R.id.start_select_button);
        Button infoButton = findViewById(R.id.info_button);
        ImageButton settingButton = findViewById(R.id.setting_button);

        menuButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ManuSelectActivity.class);
                startActivity(intent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                PopupMenu popup = new PopupMenu(getApplicationContext(), view); //v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.settingmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item){
                        switch (item.getItemId()){
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
        });
    }
}
