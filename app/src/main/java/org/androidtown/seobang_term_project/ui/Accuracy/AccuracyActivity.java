package org.androidtown.seobang_term_project.ui.Accuracy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.androidtown.seobang_term_project.R;

/**
 * @When
 * This activity will be shown when the user access to the power menu
 * and click 일치도 설정 button
 *
 * @Functions
 * User can adjust the bar of setting accuracy of the ingredients
 * by scrolling the bar
 * with the 517 of recipes' ingredients
 *
 * @Technique
 * It is maintained by shared preference to keep it same every time user opens the app
 */

public class AccuracyActivity extends Activity {

    TextView textView;
    SeekBar seekBar;
    Button confirm;
    Button cancel;
    public static int accuracy = 50;
    public static int tempAccuracy = 50;
    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_accuracy);
        textView = findViewById(R.id.accuracyTextView);
        seekBar = findViewById(R.id.seekbar);
        confirm = findViewById(R.id.seekbarConfirm);
        cancel = findViewById(R.id.seekbarCancel);
        applySharedPreference();
        seekBar.setProgress(tempAccuracy);
        textView.setText("일치도 : " + tempAccuracy + "%");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("일치도 : " + progress + "%");
                accuracy = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempAccuracy = accuracy;
                sharedPreferences();
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accuracy = tempAccuracy;
                finish();
            }
        });

    }

    public void sharedPreferences() {
        sh_Pref = getSharedPreferences("Accuracy", MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        toEdit.putInt("Accuracy", tempAccuracy);
        toEdit.commit();
    }

    public void applySharedPreference() {
        sh_Pref = getSharedPreferences("Accuracy", MODE_PRIVATE);
        if (sh_Pref != null && sh_Pref.contains("Accuracy")) {
            int Accuracy = sh_Pref.getInt("Accuracy", 50);
            textView.setText("일치도 : " + Accuracy + "%");
            tempAccuracy = Accuracy;
        }
    }
}

