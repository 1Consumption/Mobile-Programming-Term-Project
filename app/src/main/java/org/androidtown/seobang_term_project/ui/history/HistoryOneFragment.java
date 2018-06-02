package org.androidtown.seobang_term_project.ui.history;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.utils.DBUtils;
import org.androidtown.seobang_term_project.utils.MySQLiteOpenHelper;

public class HistoryOneFragment extends android.support.v4.app.Fragment {

    SQLiteDatabase db;
    SQLiteDatabase db_2;
    MySQLiteOpenHelper helper;
    private Cursor cursor;

    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_basic_information.db";
    public static final String TABLE_NAME = "recipe_basic_information";

    private ListView listview;
    private HistoryAdapter adapter;

    String[] list = new String[500];
    int listLength = 0;

    boolean flag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] URLList = new String[500];

        View view = inflater.inflate(R.layout.history_one, container, false);
        helper = new MySQLiteOpenHelper(getContext(), "frequency.db", null, 3);
        DBUtils.setDB(getContext(), ROOT_DIR, DB_Name);
        db_2 = DatabaseFactory.create(getContext(), DB_Name);

        adapter = new HistoryAdapter(getActivity().getApplicationContext());
        listview = (ListView) view.findViewById(R.id.List_view);
        adapter.notifyDataSetChanged();
        //어뎁터 할당
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        selectAll();

        Cursor cursor;

        for (int i = 0; i < countAll(); i++) {
            cursor = db_2.rawQuery("SELECT URL FROM " + TABLE_NAME + " WHERE recipe_name=\"" + list[i].substring(0, list[i].indexOf(",")) + "\"", null);
            cursor.moveToNext();
            URLList[i] = cursor.getString(0);
            Log.e("URLURLRULR", URLList[i]);
        }

        for (int i = 0; i < countAll(); i++) {
            for (int j = 0; j < countAll() - i - 1; j++) {
                if (Integer.parseInt(list[j].substring(list[j].indexOf(",") + 1)) <= Integer.parseInt(list[j + 1].substring(list[j + 1].indexOf(",") + 1))) {
                    String temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }

        //adapter를 통한 값 전달
        for (int i = 0; i < countAll(); i++) {
            String name = list[i].substring(0, list[i].indexOf(","));

            cursor = db_2.rawQuery("SELECT URL,recipe_code FROM " + TABLE_NAME + " WHERE recipe_name=\"" + name + "\"", null);
            cursor.moveToNext();
            String URL = cursor.getString(0);
            String code = cursor.getString(1);
            String frequency = list[i].substring(list[i].indexOf(",") + 1) + "번";
            adapter.addHistory(URL, name, frequency, code);
        }


        return view;
    }

    public void insert(String id, int frequency) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("frequency", frequency);
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
            list[listLength] = id + "," + String.valueOf(frequency);
            listLength++;
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

    public int countFrequencyAll() {
        int count = 0;
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select count(frequency),frequency from frequency", null);
        while (c.moveToNext()) {
            if (c.getInt(0) == 0)
                count += 0;
            else
                count += c.getInt(1);
        }

        return count;
    }

    public int countAll() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from frequency", null);
        c.moveToNext();

        return Integer.parseInt(c.getString(0));
    }
}
