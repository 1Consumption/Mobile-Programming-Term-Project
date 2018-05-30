package org.androidtown.seobang_term_project.ui.intro;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.ui.main.MainActivity;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class IntroActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Drawable alpha = ((ImageView)findViewById(R.id.logo)).getDrawable();
        alpha.setAlpha(80);

        handler = new Handler();
        handler.postDelayed(pass, 2000);
    }

    private Runnable pass = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(pass);
    }
}
