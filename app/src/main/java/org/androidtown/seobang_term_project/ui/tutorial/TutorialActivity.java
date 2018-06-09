package org.androidtown.seobang_term_project.ui.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.ui.intro.SlideFragment;
import org.androidtown.seobang_term_project.ui.main.MainActivity;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class TutorialActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(SlideFragment.newInstance(R.layout.layout_intro0));
        addSlide(SlideFragment.newInstance(R.layout.layout_intro1));
        addSlide(SlideFragment.newInstance(R.layout.layout_intro2));
        addSlide(SlideFragment.newInstance(R.layout.layout_intro3));
        addSlide(SlideFragment.newInstance(R.layout.layout_intro4));
        addSlide(SlideFragment.newInstance(R.layout.layout_intro5));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
