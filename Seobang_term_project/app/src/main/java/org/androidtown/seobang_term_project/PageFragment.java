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
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageFragment extends Fragment {

    private String mPageString;
    TextView hour;
    TextView minute;
    TextView second;
    LinearLayout timerLayout;
    int time = 0;

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
        String processString = mPageString.substring(mPageString.indexOf("&") + 1, mPageString.indexOf("|"));
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_page, container, false);
        ((TextView) rootView.findViewById(R.id.recipeString)).setText(processString);
        WebView webView = rootView.findViewById(R.id.processWebView);
        webView.getSettings().setJavaScriptEnabled(true);

        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        timerLayout = rootView.findViewById(R.id.timerLayout);
        hour = rootView.findViewById(R.id.hourText);
        minute = rootView.findViewById(R.id.minuteText);
        second = rootView.findViewById(R.id.secondText);

        if (processString.indexOf("시간") != -1 || processString.indexOf("분") != -1) {
            double extractedTime = 0;
            int timeIndex = 0;
            String timeInProcess = "";
            final Button timerStartButton = rootView.findViewById(R.id.timerStartButton);

            if (processString.indexOf("시간") != -1) {
                timerLayout.setVisibility(View.VISIBLE);
                timerStartButton.setVisibility(View.VISIBLE);
                int index = processString.indexOf("시간");

                timeIndex = extractTime(processString, index);
                timeInProcess = processString.substring(timeIndex, processString.indexOf("시간"));

                if (timeInProcess.indexOf("~") != -1 || timeInProcess.indexOf("-") != -1) {
                    String[] temp = null;
                    if (timeInProcess.indexOf("~") != -1)
                        temp = timeInProcess.split("~");

                    if (timeInProcess.indexOf("-") != -1)
                        temp = timeInProcess.split("-");

                    timeInProcess = String.valueOf((Double.parseDouble(temp[0]) + Double.parseDouble(temp[1])) / 2);
                }
                extractedTime = Double.parseDouble(timeInProcess) * 3600;
            }

            if (processString.indexOf("분") != -1) {
                int index = processString.indexOf("분");
                timeIndex = extractTime(processString, index);
                char errorCheck = processString.charAt(processString.indexOf("분") - 1);

                if (Character.isDigit(errorCheck) == true) {
                    timerLayout.setVisibility(View.VISIBLE);
                    timerStartButton.setVisibility(View.VISIBLE);
                    timeInProcess = processString.substring(timeIndex, processString.indexOf("분"));

                    if (timeInProcess.indexOf("~") != -1 || timeInProcess.indexOf("-") != -1) {
                        String[] temp = null;
                        if (timeInProcess.indexOf("~") != -1)
                            temp = timeInProcess.split("~");

                        if (timeInProcess.indexOf("-") != -1)
                            temp = timeInProcess.split("-");
                        timeInProcess = String.valueOf((Double.parseDouble(temp[0]) + Double.parseDouble(temp[1])) / 2);
                    }
                    extractedTime = Double.parseDouble(timeInProcess) * 60;
                }
            }

            time = (int) extractedTime;

            timerStartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new CountDownTask().execute();
                    Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                    timerStartButton.startAnimation(anim);
                    timerStartButton.setVisibility(View.INVISIBLE);
                }
            });
        }


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

    private int extractTime(String process, int curIndex) {
        int timeIndex = 0;

        char temp = process.charAt(--curIndex);

        if (Character.isDigit(temp) == true) {
            timeIndex = extractTime(process, curIndex);
        } else if (temp == '~' || temp == '-') {
            timeIndex = extractTime(process, curIndex);
        } else {
            return curIndex + 1;
        }

        return timeIndex;
    }
}
