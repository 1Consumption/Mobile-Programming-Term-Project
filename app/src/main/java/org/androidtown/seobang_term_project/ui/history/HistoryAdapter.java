package org.androidtown.seobang_term_project.ui.history;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.seobang_term_project.R;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter{
    private ArrayList<HistoryList> history = new ArrayList<HistoryList>() ;
    public HistoryAdapter() {

    }

    @Override
    public int getCount() {
        return history.size() ;
    }

    // ** 이 부분에서 리스트뷰에 데이터를 넣어줌 **
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //postion = ListView의 위치      /   첫번째면 position = 0
        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_history, parent, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.historyWebView) ;
        TextView title = (TextView) convertView.findViewById(R.id.title) ;
        TextView Context = (TextView) convertView.findViewById(R.id.context) ;


        HistoryList listViewItem = history.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        image.setImageDrawable(listViewItem.getImg());
        title.setText(listViewItem.getTitle());
        Context.setText(listViewItem.getContext());


        //리스트뷰 클릭 이벤트
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, (pos+1)+"번째 리스트가 클릭되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, (pos+1)+"번째 꾸우우욱리스트가 클릭되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });



        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return position ;
    }


    @Override
    public Object getItem(int position) {
        return history.get(position) ;
    }

    // 데이터값 넣어줌
    public void addHistory(Drawable icon, String title, String desc) {
        HistoryList item = new HistoryList();

        item.setImg(icon);
        item.setTitle(title);
        item.setContext(desc);

        history.add(item);
    }
}
