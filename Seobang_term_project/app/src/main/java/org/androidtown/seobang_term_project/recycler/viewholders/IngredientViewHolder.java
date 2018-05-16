package org.androidtown.seobang_term_project.recycler.viewholders;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

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
    }

    @Override
    public void bindData(Object o) throws Exception {
        if(o instanceof Ingredient) {
            this.ingredient = (Ingredient) o;
            this.item_image.setImageDrawable(ContextCompat.getDrawable(context(), this.ingredient.getImage()));
            this.item_image.setAlpha(0.5f);
            this.item_name.setText(this.ingredient.getIngredientType());
        }
    }

    @Override
    public void onClick(View v) {
        if(item_image.getAlpha() == 1) {
            item_image.setAlpha(0.5f);
            delegate.onItemClick(ingredient, false);
        }
        else {
            item_image.setAlpha(1f);
            delegate.onItemClick(ingredient, true);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
