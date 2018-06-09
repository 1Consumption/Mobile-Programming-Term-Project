
package org.androidtown.seobang_term_project.ui.recipe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.ui.history.HistoryActivity;

import butterknife.ButterKnife;

public class PageFragment extends Fragment {

    private String mPageString;
    TextView hour;
    TextView minute;
    TextView second;
    LinearLayout timerLayout;
    FloatingActionButton showFABBtn;
    FloatingActionButton addToHistoryBtn;
    FloatingActionButton goToCameraBtn;
    int result = 0;
    int time = 0;
    int tempTime = 0;

    int intHour;
    int intMinute;
    int intSecond;

    LinearLayout cameraLayout;
    LinearLayout historyLayout;
    Animation hideBtn;
    Animation showBtn;
    Animation hideLayout;
    Animation showLayout;

    boolean timerStateflag = false;
    CountDownTimer countDownTimer;


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
        final String processString = mPageString.substring(mPageString.indexOf("&") + 1, mPageString.indexOf("|"));
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_page, container, false);

        ButterKnife.bind(this, rootView);

        cameraLayout = rootView.findViewById(R.id.cameraLayout);
        historyLayout = rootView.findViewById(R.id.historyLayout);

        hideBtn = AnimationUtils.loadAnimation(getActivity(), R.anim.hide_button);
        showBtn = AnimationUtils.loadAnimation(getActivity(), R.anim.show_button);
        hideLayout = AnimationUtils.loadAnimation(getActivity(), R.anim.hide_layout);
        showLayout = AnimationUtils.loadAnimation(getActivity(), R.anim.show_layout);
        showLayout.setInterpolator(AnimationUtils.loadInterpolator(getActivity(), android.R.anim.bounce_interpolator));

        addToHistoryBtn = rootView.findViewById(R.id.addToHistory);
        goToCameraBtn = rootView.findViewById(R.id.goToCamera);
        showFABBtn = rootView.findViewById(R.id.showFAB);
        showFABBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraLayout.getVisibility() == View.VISIBLE && historyLayout.getVisibility() == View.VISIBLE) {
                    showFABBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4B8A3D")));
                    cameraLayout.setVisibility(View.GONE);
                    historyLayout.setVisibility(View.GONE);
                    cameraLayout.startAnimation(hideLayout);
                    historyLayout.startAnimation(hideLayout);
                    showFABBtn.startAnimation(hideBtn);
                } else {
                    showFABBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#da4b3d")));
                    cameraLayout.setVisibility(View.VISIBLE);
                    historyLayout.setVisibility(View.VISIBLE);
                    cameraLayout.startAnimation(showLayout);
                    historyLayout.startAnimation(showLayout);
                    showFABBtn.startAnimation(showBtn);
                }
            }
        });

        goToCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
            }
        });

        addToHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = mPageString.substring(0, mPageString.indexOf("+"));
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("RecipeName", recipeName);
                intent.putExtras(bundle);
                Toast.makeText(getContext().getApplicationContext(), recipeName + "이(가) 정상적으로 추가 되었어요!", Toast.LENGTH_LONG).show();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        ((TextView) rootView.findViewById(R.id.pageTextView)).setText(mPageString.substring(mPageString.indexOf("+") + 1, mPageString.indexOf("&")));
        if (mPageString.indexOf("last") != -1) {
            mPageString = mPageString.substring(0, mPageString.indexOf("last"));
            (rootView.findViewById(R.id.showFAB)).setVisibility(View.VISIBLE);
        }

        ((TextView) rootView.findViewById(R.id.recipeString)).setText(processString);
        ImageView imageView = rootView.findViewById(R.id.page_image);

        timerLayout = rootView.findViewById(R.id.timerLayout);
        hour = rootView.findViewById(R.id.hourText);
        minute = rootView.findViewById(R.id.minuteText);
        second = rootView.findViewById(R.id.secondText);

        checkTime(rootView, processString);

        if (!mPageString.substring(mPageString.indexOf("|") + 1).isEmpty()) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.placeholder).override(250, 200);
            String url = (mPageString.substring(mPageString.indexOf("|") + 1));
            if (url.equals("No URL")) {
                Glide.with(this).load(R.drawable.default_2).into(imageView);
            } else {
//                Glide.with(this).load((mPageString.substring(mPageString.indexOf("|") + 1))).into(imageView);
                Glide.with(this).load((mPageString.substring(mPageString.indexOf("|") + 1))).thumbnail(0.5f).apply(options).into(imageView);
            }
        }
        return rootView;
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


    private void checkTime(ViewGroup rootView, final String processString) {
        if (processString.indexOf("시간") != -1 || processString.indexOf("분") != -1) {
            double extractedTime = 0;
            int timeIndex = 0;
            String timeInProcess = "";
            LinearLayout timerButtonLayout = rootView.findViewById(R.id.timerButtonLayout);
            final Button timerStartButton = rootView.findViewById(R.id.timerStartButton);
            Button timerPauseButton = rootView.findViewById(R.id.timerPauseButton);
            Button timerStopButton = rootView.findViewById(R.id.timerStopButton);

            if (processString.indexOf("시간") != -1 && processString.indexOf("시간") != 0 && processString.charAt(processString.indexOf("시간") - 1) != ' ') {
                timerLayout.setVisibility(View.VISIBLE);
                timerButtonLayout.setVisibility(View.VISIBLE);
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
                    timerButtonLayout.setVisibility(View.VISIBLE);
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
            tempTime = time + 1;
            timerStartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (RecipeActivity.timerOn == false && timerStateflag == false) {
                        RecipeActivity.timerOn = true;
                        timerStateflag = true;
                        countDownTimer = setTimer(processString, tempTime);
                        countDownTimer.start();

                    } else if (RecipeActivity.timerOn == true) {
                        Toast.makeText(getActivity().getApplicationContext(), "이미 다른 타이머를 실행 중입니다!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            timerPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (timerStateflag == true) {
                        countDownTimer.cancel();
                        timerStateflag = false;
                        RecipeActivity.timerOn = false;
                    }
                }
            });

            timerStopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempTime = time + 1;
                    countDownTimer.cancel();
                    timerStateflag = false;
                    RecipeActivity.timerOn = false;
                    intHour = (tempTime - 1) / 3600;
                    intMinute = ((tempTime - 1) - (intHour * 3600)) / 60;
                    intSecond = ((tempTime - 1) - (intHour * 3600 + intMinute * 60));

                    hour.setText(String.format("%02d", intHour));
                    minute.setText(String.format("%02d", intMinute));
                    second.setText(String.format("%02d", intSecond));
                }
            });
        }
    }

    public CountDownTimer setTimer(final String process, final int time) {
        countDownTimer = new CountDownTimer((time * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempTime -= 1;
                intHour = tempTime / 3600;
                intMinute = (tempTime - (intHour * 3600)) / 60;
                intSecond = (tempTime - (intHour * 3600 + intMinute * 60));
                hour.setText(String.format("%02d", intHour));
                minute.setText(String.format("%02d", intMinute));
                second.setText(String.format("%02d", intSecond));
            }

            @Override
            public void onFinish() {
                RecipeActivity.timerOn = false;
                timerStateflag = false;
                tempTime = time;
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
                        .setContentText(process)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setLargeIcon(mLargeIconForNoti)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                notificationmanager.notify(1, mbuilder.build());
            }
        };
        return countDownTimer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null)
            countDownTimer.cancel();
        RecipeActivity.timerOn = false;
        timerStateflag = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        showFABBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4B8A3D")));
        cameraLayout.setVisibility(View.GONE);
        historyLayout.setVisibility(View.GONE);
        cameraLayout.startAnimation(hideLayout);
        historyLayout.startAnimation(hideLayout);
        showFABBtn.startAnimation(hideBtn);
    }


}
