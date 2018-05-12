package org.androidtown.seobang_term_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        ((TextView) rootView.findViewById(R.id.recipeString)).setText(mPageString.substring(mPageString.indexOf("&") + 1, mPageString.indexOf("|")));
        WebView webView = rootView.findViewById(R.id.processWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        if (!mPageString.substring(mPageString.indexOf("|") + 1).isEmpty())
            webView.loadUrl(mPageString.substring(mPageString.indexOf("|") + 1));
        else {

        }
        webView.setWebViewClient(new WebViewClient());
        return rootView;
    }
}
