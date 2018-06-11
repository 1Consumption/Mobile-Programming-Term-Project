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

/**
 * @When:
 * This fragment is shown when the user enters 그래프 보기 button from the HistoryActivity
 *
 * @Function:
 * this fragment show pie chart that read data from frequency.db.
 *
 * @Technique:
 * This fragment using the sqlite db for read data.
 *  delivery convenience to view using pie Chart API.
 */

public class HistoryTwoFragment extends android.support.v4.app.Fragment {

    PieChart pieChart;
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    ArrayList<String> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new MySQLiteOpenHelper(getContext(), "frequency.db", null, 3);
        View view = inflater.inflate(R.layout.history_two, container, false);
        if (countAll() != 0) {
            view.findViewById(R.id.noRecipeLayout_3).setVisibility(View.INVISIBLE);
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
                for (int j = 0; j < countAll() - i - 1; j++) {
                    int curTimes = Integer.parseInt(list.get(j).substring(list.get(j).indexOf(",") + 1));
                    int nextTimes = Integer.parseInt(list.get(j + 1).substring(list.get(j + 1).indexOf(",") + 1));
                    if (curTimes <= nextTimes) {
                        String temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                    }
                }
            }

            for (int i = 0; i < list.size(); i++) {
                String name = list.get(i).substring(0, list.get(i).indexOf(","));
                int frequency = Integer.parseInt(list.get(i).substring(list.get(i).indexOf(",") + 1));
                yValues.add(new PieEntry((((float) frequency / (float) (countFrequencyAll()) * 100)), name));
            }


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
    }

    public void select(String id) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select id,frequency from frequency where id=\"" + id + "\"", null);
        while (c.moveToNext()) {
            int frequency = c.getInt(c.getColumnIndex("frequency"));
            String _id = c.getString(c.getColumnIndex("id"));
        }
    }

    public void selectAll() {
        db = helper.getReadableDatabase();
        Cursor c = db.query("frequency", null, null, null, null, null, null);
        while (c.moveToNext()) {
            int frequency = c.getInt(c.getColumnIndex("frequency"));
            String id = c.getString(c.getColumnIndex("id"));
            list.add(id + "," + String.valueOf(frequency));
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

