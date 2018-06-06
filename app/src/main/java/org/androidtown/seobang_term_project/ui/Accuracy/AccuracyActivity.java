package org.androidtown.seobang_term_project.ui.Accuracy;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.androidtown.seobang_term_project.R;

public class AccuracyActivity extends Activity {

    TextView textView;
    SeekBar seekBar;
    Button confirm;
    Button cancel;
    public static int accuracy = 50;
    public static int tempAccuracy = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_accuracy);
        textView = findViewById(R.id.accuracyTextView);
        seekBar = findViewById(R.id.seekbar);
        confirm = findViewById(R.id.seekbarConfirm);
        cancel = findViewById(R.id.seekbarCancel);
        seekBar.setProgress(tempAccuracy);
        textView.setText("정확도 : " + tempAccuracy + "%");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("정확도 : " + progress + "%");
                accuracy = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("seekbar", "start");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("seekbar", "stop");
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempAccuracy = accuracy;
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

    public int getAccuracy() {
        return this.accuracy;
    }
}
