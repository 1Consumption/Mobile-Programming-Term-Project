package org.androidtown.seobang_term_project.ui.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.ui.history.HistoryActivity;
import org.androidtown.seobang_term_project.ui.info.InfoActivity;
import org.androidtown.seobang_term_project.ui.ingredient.IngredientSelectActivity;
import org.androidtown.seobang_term_project.ui.recipe.RecipeSelectActivity;
import org.androidtown.seobang_term_project.ui.tutorial.TutorialActivity;
import org.androidtown.seobang_term_project.utils.PowerMenuUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Build.VERSION_CODES.M;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class MainActivity extends AppCompatActivity {

    private PowerMenu powerMenu;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermissionF();
        powerMenu = PowerMenuUtils.getHamburgerPowerMenu(this, this, powerMenuItemOnMenuItemClickListener);
    }

    @OnClick(R.id.recipe_select_button)
    public void recipeButton(View view) {
        Intent intent = new Intent(getApplicationContext(), RecipeSelectActivity.class);
        startIntent(intent);
    }

    @OnClick(R.id.start_select_button)
    public void startButton(View view) {
        Intent intent = new Intent(getApplicationContext(), IngredientSelectActivity.class);
        startIntent(intent);
    }

    @OnClick(R.id.history_button)
    public void historyButton(View view) {
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        startIntent(intent);
    }

    @OnClick(R.id.info_button)
    public void infoButton(View view) {
        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        startIntent(intent);
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
        if (powerMenu.isShowing())
            powerMenu.dismiss();
        else
            super.onBackPressed();
    }

    private void startIntent(final Intent intent) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 220);
    }

    private void checkPermissionF() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            // only for LOLLIPOP and newer versions
            System.out.println("Hello Marshmallow (마시멜로우)");
            int permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("단말기의 파일쓰기 권한이 필요합니다.\\n계속하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= M) {
                                        System.out.println("감사합니다. 권한을 허락했네요 (마시멜로우)");
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    Toast.makeText(getApplicationContext(), "권한 요청 취소", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();
                    //최초로 권한을 요청할 때.
                } else {
                    System.out.println("최초로 권한을 요청할 때. (마시멜로우)");
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    //        getThumbInfo();
                }
            } else {
                //권한이 있을 때.
                //       getThumbInfo();
            }

        } else {
            System.out.println("(마시멜로우 이하 버전입니다.)");
            //   getThumbInfo();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult WRITE_EXTERNAL_STORAGE ( 권한 성공 ) ");
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult READ_PHONE_STATE ( 권한 성공 ) ");
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult READ_EXTERNAL_STORAGE ( 권한 성공 ) ");
                    }
                }
            }
        } else {
            System.out.println("onRequestPermissionsResult ( 권한 거부) ");
            Toast.makeText(getApplicationContext(), "요청 권한 거부", Toast.LENGTH_SHORT).show();
        }
    }
}
