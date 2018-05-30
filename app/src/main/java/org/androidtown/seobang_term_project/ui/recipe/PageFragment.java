
package org.androidtown.seobang_term_project.ui.recipe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.androidtown.seobang_term_project.R;

import butterknife.ButterKnife;

public class PageFragment extends Fragment {

    private String mPageString;
    TextView hour;
    TextView minute;
    TextView second;
    LinearLayout timerLayout;
    int time = 0;

    public static PageFragment create(String pageContents) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString("page", pageContents);
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
        ButterKnife.bind(this, rootView);

        ((TextView) rootView.findViewById(R.id.pageTextView)).setText(mPageString.substring(mPageString.indexOf("+") + 1, mPageString.indexOf("&")));
        if (mPageString.indexOf("last") != -1) {
            mPageString = mPageString.substring(0, mPageString.indexOf("last"));
            (rootView.findViewById(R.id.addToHistory)).setVisibility(View.VISIBLE);
        }

        ((TextView) rootView.findViewById(R.id.recipeString)).setText(processString);
        ImageView imageView = rootView.findViewById(R.id.page_image);

        timerLayout = rootView.findViewById(R.id.timerLayout);
        hour = rootView.findViewById(R.id.hourText);
        minute = rootView.findViewById(R.id.minuteText);
        second = rootView.findViewById(R.id.secondText);

        checkTime(rootView,processString);

        if (!mPageString.substring(mPageString.indexOf("|") + 1).isEmpty()) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.placeholder).override(250, 200);
            Glide.with(this).load((mPageString.substring(mPageString.indexOf("|") + 1))).thumbnail(0.2f).apply(options).into(imageView);
        }
        return rootView;
    }

    private class CountDownTask extends AsyncTask<Void, Integer, Void> {
        Context cnt;

        CountDownTask(Context context) {
            cnt = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = time; i >= 0; i--) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i);

                } catch (Exception e) {
                    Log.d(this.getClass().getName(), "error-exception");
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

        @Override
        protected void onPostExecute(Void aVoid) {
            Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
            NotificationManager notificationmanager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notiIntent = new Intent(getActivity(), RecipeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("selectedRecipe", mPageString.substring(0, mPageString.indexOf("+")));
            notiIntent.putExtras(bundle);

            notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 1, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mbuilder;

            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel mChannel = new NotificationChannel("Noti", "alarm", NotificationManager.IMPORTANCE_DEFAULT);
                notificationmanager.createNotificationChannel(mChannel);
                mbuilder = new NotificationCompat.Builder(getActivity(), mChannel.getId());
            } else {
                mbuilder = new NotificationCompat.Builder(getActivity());
            }
            mbuilder.setSmallIcon(R.drawable.spoon)
                    .setContentTitle("타이머 종료!")
                    .setContentText("서방")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLargeIcon(mLargeIconForNoti)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            notificationmanager.notify(1, mbuilder.build());

        }
    }

    private int extractTime(String process, int curIndex) {
        int timeIndex = 0;

        if (curIndex == 0)
            return 0;

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

    private void checkTime(ViewGroup rootView, String processString){
        if (processString.indexOf("시간") != -1 || processString.indexOf("분") != -1) {
            double extractedTime = 0;
            int timeIndex = 0;
            String timeInProcess = "";
            final Button timerStartButton = rootView.findViewById(R.id.timerStartButton);

            if (processString.indexOf("시간") != -1 && processString.indexOf("시간") != 0 && processString.charAt(processString.indexOf("시간") - 1) != ' ') {
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

            if (processString.indexOf("분") != -1 && processString.indexOf("분") != 0 && processString.charAt(processString.indexOf("분") - 1) != ' ') {
                int index = processString.indexOf("분");
                timeIndex = extractTime(processString, index);
                char errorCheck = processString.charAt(processString.indexOf("분") - 1);

                if (Character.isDigit(errorCheck)) {
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
                    new CountDownTask(getContext()).execute();
                    Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                    timerStartButton.startAnimation(anim);
                    timerStartButton.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
