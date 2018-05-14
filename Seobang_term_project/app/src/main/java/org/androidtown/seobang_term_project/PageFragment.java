package org.androidtown.seobang_term_project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class PageFragment extends Fragment {

    private String mPageString;
    TextView hour;
    TextView minute;
    TextView second;
    int time = 3666;

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

        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        hour = rootView.findViewById(R.id.hourText);
        minute = rootView.findViewById(R.id.minuteText);
        second = rootView.findViewById(R.id.secondText);


        final Button timerStartButton = rootView.findViewById(R.id.timerStartButton);
        timerStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new CountDownTask().execute();
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                timerStartButton.startAnimation(anim);
                timerStartButton.setVisibility(View.INVISIBLE);
            }
        });


        if (!mPageString.substring(mPageString.indexOf("|") + 1).isEmpty())
            webView.loadUrl(mPageString.substring(mPageString.indexOf("|") + 1));
        else {
            webView.loadUrl("https://github.com/HanseopShin/Mobile-Programming-Term-Project/blob/master/no_Info.png?raw=true");
        }

        webView.setWebViewClient(new WebViewClient());

        return rootView;
    }

    private class CountDownTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            for (int i = time; i >= 0; i--) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i);

                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int temp = values[0].intValue();
            int intHour = temp / 3600;
            int intMinute = (temp - (intHour * 3600)) / 60;
            int intSecond = (temp - (intHour * 3600 + intMinute * 60));

            hour.setText(String.format("%02d", intHour));
            minute.setText(String.format("%02d", intMinute));
            second.setText(String.format("%02d", intSecond));
        }
    }
}
