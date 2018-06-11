package org.androidtown.seobang_term_project.ui.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import org.androidtown.seobang_term_project.R;

/**
 * @When:
 * This activity is shown when the user input 0 into HistoryEditActivity and click 확인 button.
 *
 * @Function:
 * this activity show 확인 button and 취소 Button, so user can decide delete or keep up database.
 * if user press 확인 button, then call HistoryActivity.
 * if user press 취소 button, just go back to previous activity.
 *
 * @Technique:
 * using Bundle into intent for wrapping data to send.
 * set intent flag FLAG_ACTIVITY_CLEAR_TOP for prevent activity duplicate execution.
 */

public class lastConfirmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_last_confirm);

        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent get = getIntent();
                Bundle bundle = get.getExtras();
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        findViewById(R.id.cancelBtn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
