package org.androidtown.seobang_term_project.recycler.adapters;

import android.view.View;

import com.skydoves.baserecyclerviewadapter.BaseAdapter;
import com.skydoves.baserecyclerviewadapter.BaseViewHolder;
import com.skydoves.baserecyclerviewadapter.SectionRow;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.items.Recipe;
import org.androidtown.seobang_term_project.recycler.viewholders.RecipeViewHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

/**
 * @When
 * This class is for Adapting the Recipe items to the holder
 *
 * @functions & @technique:
 * by RecipeAdapter, adapting the items can be done by delegating
 */


public class RecipeAdapter extends BaseAdapter {

    private RecipeViewHolder.Delegate delegate;

    public RecipeAdapter(RecipeViewHolder.Delegate delegate) {
        this.delegate = delegate;
        addSection(new ArrayList<Recipe>());
    }

    public void addItem(Recipe recipe) {
        addItemOnSection(0, recipe);
        notifyDataSetChanged();
    }

    public void addItems(List<Recipe> recipeList) {
        addItemsOnSection(0, recipeList);
        notifyDataSetChanged();
    }

    @Override
    protected int layout(SectionRow sectionRow) {
        return R.layout.item_recipe;
    }

    @NotNull
    @Override
    protected BaseViewHolder viewHolder(int i, View view) {
        return new RecipeViewHolder(view, delegate);
    }
}
