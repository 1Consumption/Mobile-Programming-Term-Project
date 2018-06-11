package org.androidtown.seobang_term_project.recycler.viewholders;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.skydoves.baserecyclerviewadapter.BaseViewHolder;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.items.IngredientList;
import org.androidtown.seobang_term_project.recycler.adapters.IngredientAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @When
 * This class is for holding the items and adapt it by the adapter
 *
 * @functions & @technique:
 * by {@link org.androidtown.seobang_term_project.recycler.adapters.IngredientListAdapter}, items can be shown to the page by the adapter
 */

public class IngredientListViewHolder extends BaseViewHolder {

    protected @BindView(R.id.item_ingredient_list_name) TextView name;
    protected @BindView(R.id.item_ingredient_list_recyclerVIew) RecyclerView recyclerView;

    private IngredientViewHolder.Delegate delegate;

    public IngredientListViewHolder(View view, IngredientViewHolder.Delegate delegate) {
        super(view);
        ButterKnife.bind(this, view);
        this.delegate = delegate;
    }

    @Override
    public void bindData(Object o) throws Exception {
        if(o instanceof IngredientList) {
            IngredientList item = (IngredientList) o;
            name.setText(item.getName());

            IngredientAdapter adapter = new IngredientAdapter(delegate);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(context(), 6));
            adapter.addItems(item.getIngredients());
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
