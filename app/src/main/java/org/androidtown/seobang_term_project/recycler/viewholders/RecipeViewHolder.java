package org.androidtown.seobang_term_project.recycler.viewholders;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.skydoves.baserecyclerviewadapter.BaseViewHolder;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.items.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Developed by hayeon0824 on 2018-05-19.
 * Copyright (c) 2018 hayeon0824 rights reserved.
 */

/**
 * @When
 * This class is for holding the recipe items and adapt it by the adapter
 *
 * @Functions & @Technique:
 * RecipeViewHolder, items can be shown to the page
 * from this RecipeViewHolder, the settings of the items can be changed or managed
 * We utilized the the open source libraries for the designing which is small but completes the level of completion
 */


public class RecipeViewHolder extends BaseViewHolder {

    private Recipe recipe;
    private Delegate delegate;

    protected @BindView(R.id.recipe_image) ImageView image;
    protected @BindView(R.id.recipe_name) TextView name;
    protected @BindView(R.id.item_recipe_label) LinearLayout label;

    public interface Delegate {
        void onItemClick(Recipe recipe, View view);
    }

    public RecipeViewHolder(View view, Delegate delegate) {
        super(view);
        ButterKnife.bind(this, view);
        this.delegate = delegate;
    }

    @Override
    public void bindData(Object o) throws Exception {
        if (o instanceof Recipe) {
            this.recipe = (Recipe) o;
            name.setText(recipe.getName());
            RequestOptions options = new RequestOptions().placeholder(R.drawable.placeholder_food).diskCacheStrategy(DiskCacheStrategy.ALL).override(150, 150);
            Glide.with(context()).asBitmap().load(recipe.getUrl()).thumbnail(0.2f).apply(options).into(new BitmapImageViewTarget(image) {
                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                    super.onResourceReady(bitmap, transition);
                    try {
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                if (palette == null) return;
                                label.setBackgroundColor(palette.getDarkVibrantColor(0));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        delegate.onItemClick(recipe, image);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
