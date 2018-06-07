package org.androidtown.seobang_term_project.ui.history;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


public class TabPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;
    Fragment cur_fragment;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                cur_fragment = new HistoryOneFragment();
                break;
            case 1:
                cur_fragment = new HistoryTwoFragment();
                break;
        }

        return cur_fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
