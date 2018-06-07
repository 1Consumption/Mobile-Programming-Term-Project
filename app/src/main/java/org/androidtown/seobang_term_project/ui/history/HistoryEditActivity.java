package org.androidtown.seobang_term_project.ui.history;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
                if (!receive.getText().toString().equals("")) {
                    int num = Integer.parseInt(receive.getText().toString());
                    if (num < 0)
                        Toast.makeText(getApplicationContext(), "숫자 범위를 다시 확인 해주세요.", Toast.LENGTH_LONG).show();
                    else {
                        Intent receiveIntent = getIntent();
                        Bundle rec = receiveIntent.getExtras();

                        if (num != 0) {
                            Toast.makeText(getApplicationContext(), "성공적으로 업데이트 되었습니다.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("RecipeName", String.valueOf(num) + "mod" + rec.getString("RecipeName"));
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), lastConfirmActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("RecipeName", String.valueOf(num) + "del" + rec.getString("RecipeName"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "숫자를 입력해 주세요.", Toast.LENGTH_LONG).show();
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
