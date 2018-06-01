package org.androidtown.seobang_term_project.ui.history;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;

public class HistoryEditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history_edit);

        Button update = findViewById(R.id.updateBtn);
        Button cancel = findViewById(R.id.cancelBtn);
        final EditText receive = findViewById(R.id.editHistory);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(receive.getText().toString());
                if (num <= 0)
                    Toast.makeText(getApplicationContext(), "0보다 큰 수 업데이트", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getApplicationContext(), "업데이트", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "캔슬", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}
