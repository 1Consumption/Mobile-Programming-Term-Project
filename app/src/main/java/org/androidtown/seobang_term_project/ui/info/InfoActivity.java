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
                .setDescription("'서방; 요리를 하다.'\n 직접 요리를 만들어 먹는 건강한 습관을 가지는 것은 어떨까요?\n\n 개발자: 김하연 신한섭 손민욱 \n Gachon Univ.")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("khayeon0824@gmail.com")
                .addEmail("hanseop95@gmail.com")
                .addEmail("elmehdi.sakout@gmail.com")
                .addPlayStore("org.androidtown.seobang_term_project")
                .addGitHub("HanseopShin/Mobile-Programming-Term-Project")
                .create();
        setContentView(aboutPage);
    }
}
