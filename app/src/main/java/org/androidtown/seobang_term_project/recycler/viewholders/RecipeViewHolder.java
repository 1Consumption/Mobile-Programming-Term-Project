package org.androidtown.seobang_term_project.recycler.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.skydoves.baserecyclerviewadapter.BaseViewHolder;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.items.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

public class RecipeViewHolder extends BaseViewHolder {

    private Recipe recipe;
    private Delegate delegate;

    protected @BindView(R.id.recipe_image) ImageView image;
    protected @BindView(R.id.recipe_name) TextView name;

    public interface Delegate {
        void onItemClick(Recipe recipe);
    }

    public RecipeViewHolder(View view, Delegate delegate) {
        super(view);
        ButterKnife.bind(this, view);
        this.delegate = delegate;
    }

    @Override
    public void bindData(Object o) throws Exception {
        if(o instanceof Recipe) {
            this.recipe = (Recipe) o;
            RequestOptions options = new RequestOptions().placeholder(R.drawable.about_icon_github);
            Glide.with(context()).load(recipe.getRepresentUrl()).apply(options).into(image);
            name.setText(recipe.getName());
        }
    }

    @Override
    public void onClick(View v) {
        delegate.onItemClick(recipe);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
