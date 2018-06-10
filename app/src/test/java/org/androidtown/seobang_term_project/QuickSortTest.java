package org.androidtown.seobang_term_project;


import org.androidtown.seobang_term_project.utils.QuickSortArrayList;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;

@RunWith(JUnit4.class)
public class QuickSortTest {
    private ArrayList<String> dummy;

    @Before
    public void initDummies() {
        this.dummy = new ArrayList<>();
        this.dummy.add("98w1");
        this.dummy.add("24w1");
        this.dummy.add("125w1");
        this.dummy.add("49w1");
        this.dummy.add("93w1");
        this.dummy.add("95w1");
        this.dummy.add("28w1");
        this.dummy.add("19w1");
        this.dummy.add("164w1");
        this.dummy.add("147w1");
    }

    @Test
    public void sortTest() {
        QuickSortArrayList.sort(dummy, 0, dummy.size()-1);
        MatcherAssert.assertThat(dummy.get(0), is("19w1")); // check min
        MatcherAssert.assertThat(dummy.get(dummy.size()-1), is("164w1")); // check max
    }
}
