package org.androidtown.seobang_term_project;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> ingredientList;
    private int itemLayout;


    public  IngredientAdapter(List<Ingredient> items, int itemLayout){
        this.ingredientList = items;
        this.itemLayout = itemLayout;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    /*public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView meat, fish, vege, etc;

        public MyViewHolder(View view){
            super(view);
            meat = view.findViewById(R.id.IngredientType_meat);
            fish = view.findViewById(R.id.IngredientType_fish);
            vege = view.findViewById(R.id.IngredientType_vege);
            etc = view.findViewById(R.id.IngredientType_etc);

        }
    }

    @Override
    public IngredientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_row, parent, false);

        return new MyViewHolder(itemView);
    }*/

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        Ingredient ingredient = ingredientList.get(position);
        //viewHolder.meat.setText(ingredient.getIngredientType());
        //viewHolder.fish.setText(ingredient.getIngredientType());
        //viewHolder.vege.setText(ingredient.getIngredientType());
        viewHolder.ingredientType.setText(ingredient.getIngredientType());

        viewHolder.img.setBackgroundResource(ingredient.getImage());
    }

    @Override
    public int getItemCount(){
        return ingredientList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView ingredientType;
        public ImageView img;
        //public TextView imgTitle; //재료 이름

        public ViewHolder(View itemView){
            super(itemView);

            ingredientType = itemView.findViewById(R.id.IngredientType_meat);
            img = itemView.findViewById(R.id.lettuceEx);
           // imgTitle = itemView.findFocus(R.id.);
        }




    }

}
