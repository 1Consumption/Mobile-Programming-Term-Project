package org.androidtown.seobang_term_project.ui.history;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    public FragmentAdapter(FragmentManager fm){
        super(fm);
    }

    @Override

    public Fragment getItem(int position) {

        switch (position) {

            case 0 :

                return new HistoryOneFragment();

            case 1 :

                return new HistoryTwoFragment();

        }

        return null;

    }



    @Override

    public int getCount() {

        return 2; // 원하는 페이지 수

    }

}

