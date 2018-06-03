package org.androidtown.seobang_term_project.ui.history;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;

public class HistoryTwoFragment extends android.support.v4.app.Fragment {

    PieChart pieChart;
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    String[] list = new String[500];
    int listLength = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new MySQLiteOpenHelper(getContext(), "frequency.db", null, 3);
        View view = inflater.inflate(R.layout.history_two, container, false);
        if (countAll() != 0) {
            view.findViewById(R.id.noList).setVisibility(View.INVISIBLE);
            pieChart = (PieChart) view.findViewById(R.id.piechart);
            pieChart.setVisibility(View.VISIBLE);
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(5, 10, 5, 5);

            pieChart.setDragDecelerationFrictionCoef(0.95f);

            pieChart.setDrawHoleEnabled(false);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setTransparentCircleRadius(100f);
            selectAll();
            ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

            for (int i = 0; i < countAll(); i++) {
                for(int j=0;j<countAll()-i-1;j++){
                    if(Integer.parseInt(list[j].substring(list[j].indexOf(",")+1))<=Integer.parseInt(list[j+1].substring(list[j+1].indexOf(",")+1))){
                        String temp=list[j];
                        list[j]=list[j+1];
                        list[j+1]=temp;
                    }
                }
            }

            for (int i = 0; i < listLength; i++) {
                String name = list[i].substring(0, list[i].indexOf(","));
                int frequency = Integer.parseInt(list[i].substring(list[i].indexOf(",") + 1));
                yValues.add(new PieEntry((((float) frequency / (float) (countFrequencyAll()) * 100)), name));
            }

//            yValues.add(new PieEntry(10f, ""));

            Description description = new Description();
            description.setText("요리 목록(%)"); //라벨
            description.setTextSize(15);
            pieChart.setDescription(description);

            pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

            PieDataSet dataSet = new PieDataSet(yValues, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            PieData data = new PieData((dataSet));
            data.setValueTextSize(20f);
            data.setValueTextColor(Color.YELLOW);

            pieChart.setData(data);
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
        Cursor c = db.rawQuery("select id,frequency from frequency where id=\"" + id+"\"", null);
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
        Cursor c = db.rawQuery("select count(frequency),frequency from frequency where id=\"" + id+"\"", null);
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

