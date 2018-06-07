package org.androidtown.seobang_term_project.ui.info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.androidtown.seobang_term_project.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.title)
                .setDescription("김하연 손민욱 신한섭")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("elmehdi.sakout@gmail.com")
                .addEmail("hanseop95@gmail.com")
                .addEmail("elmehdi.sakout@gmail.com")
                .addPlayStore("org.androidtown.seobang_term_project")
                .addGitHub("HanseopShin/Mobile-Programming-Term-Project")
                .create();
        setContentView(aboutPage);
    }
}
