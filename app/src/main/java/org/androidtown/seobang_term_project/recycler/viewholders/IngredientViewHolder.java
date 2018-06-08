package org.androidtown.seobang_term_project.recycler.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.skydoves.baserecyclerviewadapter.BaseViewHolder;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.items.Ingredient;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class IngredientViewHolder extends BaseViewHolder {

    private Ingredient ingredient;
    private Delegate delegate;

   protected @BindView(R.id.item_ingredient_image) CircleImageView item_image;
   protected @BindView(R.id.item_ingredient_name) TextView item_name;

    public interface Delegate {
        void onItemClick(Ingredient ingredient, boolean isOnClicked);
    }

    public IngredientViewHolder(View view, Delegate delegate) {
        super(view);
        ButterKnife.bind(this, view);
        this.delegate = delegate;
        Log.e("IngredientViewHolder","IngredientViewHolder");
    }

    @Override
    public void bindData(Object o) throws Exception {
        if(o instanceof Ingredient) {
            Log.e("IngredientViewHolder","bindData");
            this.ingredient = (Ingredient) o;
            RequestOptions options = new RequestOptions().placeholder(R.drawable.placeholder_food).diskCacheStrategy(DiskCacheStrategy.ALL).override(75, 75);
            Glide.with(context()).asBitmap().load(this.ingredient.getImage()).thumbnail(0.2f).apply(options).into(item_image);
            this.item_name.setText(this.ingredient.getIngredientType());
            if(ingredient.getIsListItem()) {
                this.item_image.setAlpha(0.5f);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.e("IngredientViewHolder","onClick");
        if(ingredient.getIsListItem()) {
            if (item_image.getAlpha() == 1) {
                item_image.setAlpha(0.5f);
                delegate.onItemClick(ingredient, false);
            } else {
                item_image.setAlpha(1f);
                delegate.onItemClick(ingredient, true);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
