package org.androidtown.seobang_term_project.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.ui.Accuracy.AccuracyActivity;
import org.androidtown.seobang_term_project.ui.history.HistoryActivity;
import org.androidtown.seobang_term_project.ui.info.InfoActivity;
import org.androidtown.seobang_term_project.ui.ingredient.IngredientSelectActivity;
import org.androidtown.seobang_term_project.ui.recipe.CameraActivity;
import org.androidtown.seobang_term_project.ui.recipe.RecipeSelectActivity;
import org.androidtown.seobang_term_project.ui.tutorial.TutorialActivity;
import org.androidtown.seobang_term_project.utils.PowerMenuUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

/**
 * @When:
 * This activity runs when you start the application for the first time.
 *
 * @Function:
 * Let users choose what they want to do.
 * there are 4 options.
 * food selection, ingredient selection, history view, developer info
 *
 * and 3 sub options
 * Set Accuracy, Capture Camera, View Tutorial
 *
 *It sets the permissions of this application.
 * Set permissions for save and retrieve and camera
 *
 * @Technique:
 * we made sub options through power menu.
 * Go to the class file through onclicklistener.
 * Set the access authority of the mobile phone.
 */

public class MainActivity extends AppCompatActivity {

    private PowerMenu powerMenu;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
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
                    Intent intent1 = new Intent(getApplicationContext(), AccuracyActivity.class);
                    startActivity(intent1);
                    break;
                case 2:
                    Intent intent2 = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(intent2);
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

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // 하나라도 거부한다면.
                        new AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();

                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
    }
}
