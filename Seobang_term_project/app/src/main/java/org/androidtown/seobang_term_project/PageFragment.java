package org.androidtown.seobang_term_project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class PageFragment extends Fragment {

    private String mPageString;
    TextView timer;

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

        timer = rootView.findViewById(R.id.timer);
        timer.setText("No info");
        Button timerButton = rootView.findViewById(R.id.timerButton);
        timerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new CountDownTask().execute();
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
//        @Override
//
//        protected void onPreExecute() {
//            timer.setText("*START*");
//        }

        @Override

        protected Void doInBackground(Void... params) {
            for (int i = 30; i >= 0; i--) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i); //Invokes onProgressUpdate();
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override

        protected void onProgressUpdate(Integer... values) {
            timer.setText(Integer.toString(values[0].intValue()));
        }

//        @Override
//
//        protected void onPostExecute(Void aVoid) {
//            timer.setText("*DONE*");
//        }
    }
}
