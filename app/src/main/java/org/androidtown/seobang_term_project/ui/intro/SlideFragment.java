package org.androidtown.seobang_term_project.ui.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * @When
 * This is the screen shown when the tutorial is run.
 *
 * * @Function & @Technique:
 * Passing a value along with the xml file in tutorialactivity generates a fragment page.
 * Show xml files through inflater.
 *

 * */

public class SlideFragment extends Fragment {

    private int layout;
    private static final String LAYOUT_ID = "layout_id";

    public static SlideFragment newInstance(int layoutResId) {
        SlideFragment sampleSlide = new SlideFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutResId);
        sampleSlide.setArguments(args);
        return sampleSlide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(LAYOUT_ID)) {
            layout = getArguments().getInt(LAYOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }
}
