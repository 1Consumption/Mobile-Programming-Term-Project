package org.androidtown.seobang_term_project.recycler.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.seobang_term_project.items.Ingredient;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private List<Ingredient> ingredientList;
    private int itemLayout;

    //생성자
    public RecyclerAdapter(List<Ingredient> items, int itemLayout){
        this.ingredientList = items;
        this.itemLayout = itemLayout;
    }

    //레이아웃을 만들어서 Holder에 저장
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout,viewGroup,false);
        return new ViewHolder(view);
    }

    //listView getView를 대체, 넘겨받은 데이터를 화면에 출력
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        Ingredient item = ingredientList.get(position);
        viewHolder.ingredientType.setText(item.getIngredientType());
        viewHolder.ingredientImage.setBackgroundResource(item.getImage());
        viewHolder.itemView.setTag(item);
    }

    @Override
    public int getItemCount(){
        return ingredientList.size();
    }

    //뷰 재활용을 위한 viewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView ingredientType;
        public ImageView ingredientImage;

        public ViewHolder(View itemView){
            super(itemView);
            }
    }
}
