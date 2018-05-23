package org.androidtown.seobang_term_project.ui.history;

import android.os.Bundle;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;

import butterknife.ButterKnife;

public class HistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_history);
        ButterKnife.bind(this);
    }

}
