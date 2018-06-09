package org.androidtown.seobang_term_project.recycler.adapters;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.skydoves.baserecyclerviewadapter.BaseAdapter;
import com.skydoves.baserecyclerviewadapter.BaseViewHolder;
import com.skydoves.baserecyclerviewadapter.SectionRow;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.items.Ingredient;
import org.androidtown.seobang_term_project.recycler.viewholders.IngredientViewHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class IngredientAdapter extends BaseAdapter {

    private IngredientViewHolder.Delegate delegate;

    public IngredientAdapter(IngredientViewHolder.Delegate delegate) {
        this.delegate = delegate;
        addSection(new ArrayList<Ingredient>());
    }

    public void addItem(Ingredient ingredient) {
        addItemOnSection(0, ingredient);
        notifyDataSetChanged();
    }

    public void removeItem(Ingredient ingredient) {
        for (int i = 0; i < sections().get(0).size(); i++) {
            Ingredient item = (Ingredient) (sections().get(0).get(i));
            if (item.getIngredientType().equals(ingredient.getIngredientType())) {
                sections().get(0).remove(item);
            }
        }
        notifyDataSetChanged();
    }

    public void removeAll() {
        removeSection(0);
        addSection(new ArrayList<Ingredient>());
        notifyDataSetChanged();
    }

    public void addItems(List<Ingredient> ingredients) {
        addItemsOnSection(0, ingredients);
        notifyDataSetChanged();
    }

    @Override
    protected int layout(SectionRow sectionRow) {
        return R.layout.item_ingredient;
    }

    @NotNull
    @Override
    protected BaseViewHolder viewHolder(int i, View view) {
        return new IngredientViewHolder(view, delegate);
    }


}
