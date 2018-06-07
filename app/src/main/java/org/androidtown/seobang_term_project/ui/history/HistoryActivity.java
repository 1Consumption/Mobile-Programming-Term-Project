package org.androidtown.seobang_term_project.ui.history;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.utils.MySQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */
public class HistoryActivity extends BaseActivity {
    ViewPager mPager;
    TabLayout tabLayout;
    String recipeName = "";

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_history);
        helper = new MySQLiteOpenHelper(HistoryActivity.this, "frequency.db", null, 3);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            recipeName = bundle.getString("RecipeName");
            if (recipeName.contains("mod")) {
                int tempFre = Integer.parseInt(recipeName.substring(0, recipeName.indexOf("mod")));
                recipeName = recipeName.substring(recipeName.indexOf("d") + 1);
                update(recipeName, tempFre);
            } else if (recipeName.contains("del")) {
                recipeName = recipeName.substring(recipeName.indexOf("l") + 1);
                delete(recipeName);
            } else {
                if (countFrequency(recipeName) == 0) {
                    insert(recipeName, 1);
                } else {
                    update(recipeName, countFrequency(recipeName) + 1);
                }
            }
            selectAll();
        }

        tabLayout = findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("요리별 기록 보기"));
        tabLayout.addTab(tabLayout.newTab().setText("그래프로 보기"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mPager = findViewById(R.id.history_pager);
        mPager.setAdapter(pagerAdapter);
        mPager.setOffscreenPageLimit(2);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void insert(String id, int frequency) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd");
        String formatDate = simple.format(date);
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("frequency", frequency);
        values.put("date", formatDate);
        db.insert("frequency", null, values);
    }

    public void update(String id, int frequency) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("frequency", frequency);
        db.update("frequency", values, "id=?", new String[]{id});
    }

    public void delete(String id) {
        db = helper.getWritableDatabase();
        db.delete("frequency", "id=?", new String[]{id});
        Log.i("db1", id + "정상적으로 삭제 되었습니다.");
    }

    public void select(String id) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select id,frequency from frequency where id=\"" + id + "\"", null);
        while (c.moveToNext()) {
            int frequency = c.getInt(c.getColumnIndex("frequency"));
            String _id = c.getString(c.getColumnIndex("id"));
            Log.i("db1", "id: " + _id + ", frequency : " + String.valueOf(frequency));
        }
    }

    public void selectAll() {
        db = helper.getReadableDatabase();
        Cursor c = db.query("frequency", null, null, null, null, null, null);
        while (c.moveToNext()) {
            int frequency = c.getInt(c.getColumnIndex("frequency"));
            String id = c.getString(c.getColumnIndex("id"));
            Log.i("db1", "id: " + id + ", frequency : " + frequency);
        }
    }

    public int countFrequency(String id) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select count(frequency),frequency from frequency where id=\"" + id + "\"", null);
        c.moveToNext();
        if (c.getInt(0) == 0)
            return 0;
        else
            return c.getInt(1);
    }

    public int countAll() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from frequency", null);
        c.moveToNext();

        return Integer.parseInt(c.getString(0));
    }

}

