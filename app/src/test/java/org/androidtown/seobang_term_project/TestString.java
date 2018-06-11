package org.androidtown.seobang_term_project;

import android.support.test.rule.ActivityTestRule;

import org.androidtown.seobang_term_project.ui.main.MainActivity;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestString {

    private MainActivity activity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp(){
        this.activity = mActivityRule.getActivity();
    }

    @Test
    public void testString(){
        String str = mActivityRule.getActivity().getString(R.string.menu1); //요리 선택 string
        MatcherAssert.assertThat("요리 선택", equals(str)); //okay

        //Other test cases
        //String str2 = mActivityRule.getActivity().getString(R.string.menu2); //재료 선택 string
        //MatcherAssert.assertThat("요리 선택", equals(str2)); //wrong!

        //String str3 = mActivityRule.getActivity().getString(R.string.menu3); //재료 선택 string
        //MatcherAssert.assertThat("요리 선택", equals(str3)); //wrong!

        //String str4 = mActivityRule.getActivity().getString(R.string.menu4); //재료 선택 string
        //MatcherAssert.assertThat("개발자 정보", equals(str4)); //okay!
    }


}
