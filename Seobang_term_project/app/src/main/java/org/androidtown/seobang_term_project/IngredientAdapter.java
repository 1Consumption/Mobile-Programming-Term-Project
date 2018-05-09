package org.androidtown.seobang_term_project;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {

    private List<Ingredient> ingredientList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView meat, fish, vege;

        public MyViewHolder(View view){
            super(view);
            meat = view.findViewById(R.id.meat);
            fish = view.findViewById(R.id.fish);
            vege = view.findViewById(R.id.vege);

        }
    }
    public IngredientAdapter(List<Ingredient> ingredientList){
        this.ingredientList = ingredientList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Ingredient ingredient = ingredientList.get(position);
        holder.meat.setText(ingredient.getIngredientType());
        holder.fish.setText(ingredient.getIngredientType());
        holder.vege.setText(ingredient.getIngredientType());
    }

    @Override
    public int getItemCount(){
        return ingredientList.size();
    }

}
