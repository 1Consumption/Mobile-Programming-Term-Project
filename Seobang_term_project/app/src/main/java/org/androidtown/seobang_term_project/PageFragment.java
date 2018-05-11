package org.androidtown.seobang_term_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {

    private String mPageString;

    public static PageFragment create(String pageNumber) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageString = getArguments().getString("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_page, container, false);
        mPageString = mPageString.substring(mPageString.indexOf("&") + 1);
        ((TextView) rootView.findViewById(R.id.number)).setText(mPageString);
        return rootView;
    }
}
