package org.androidtown.seobang_term_project.recycler.adapters;

import android.view.View;

import com.skydoves.baserecyclerviewadapter.BaseAdapter;
import com.skydoves.baserecyclerviewadapter.BaseViewHolder;
import com.skydoves.baserecyclerviewadapter.SectionRow;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.items.IngredientList;
import org.androidtown.seobang_term_project.recycler.viewholders.IngredientListViewHolder;
import org.androidtown.seobang_term_project.recycler.viewholders.IngredientViewHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IngredientListAdapter extends BaseAdapter {

    private IngredientViewHolder.Delegate delegate;

    public IngredientListAdapter(IngredientViewHolder.Delegate delegate) {
        this.delegate = delegate;
        addSection(new ArrayList<IngredientList>());
    }

    public void addIngredientListItem(IngredientList ingredientList) {
        addItemOnSection(0, ingredientList);
        notifyDataSetChanged();
    }

    public void addIngredientListItems(List<IngredientList> ingredientLists) {
        addItemsOnSection(0, ingredientLists);
        notifyDataSetChanged();
    }

    @Override
    protected int layout(SectionRow sectionRow) {
        return R.layout.item_ingredient_list;
    }

    @NotNull
    @Override
    protected BaseViewHolder viewHolder(int i, View view) {
        return new IngredientListViewHolder(view, delegate);
    }
}
