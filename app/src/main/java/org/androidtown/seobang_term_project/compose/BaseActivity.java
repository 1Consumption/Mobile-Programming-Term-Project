package org.androidtown.seobang_term_project.compose;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.androidtown.seobang_term_project.R;
import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder; // ButterKnife unbinder

    protected @BindView(R.id.toolbar) Toolbar toolbar;
    protected @BindView(R.id.toolbar_name) TextView toolbar_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContentViewById(int layout) {
        setContentView(layout);
        this.unbinder = ButterKnife.bind(this); // bind ButterKnife
        setToolbar(toolbar);
        setDisplayHomeButton();
    }

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    public void setDisplayHomeButton() {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setToolbarName(String name) {
        toolbar_name.setText(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbinder.unbind(); // unbind ButterKnife
    }
}
