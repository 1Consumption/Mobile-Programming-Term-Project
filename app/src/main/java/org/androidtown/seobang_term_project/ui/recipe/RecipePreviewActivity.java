package org.androidtown.seobang_term_project.ui.recipe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.compose.BaseActivity;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.utils.DBUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RecipePreviewActivity extends BaseActivity {

    @BindView(R.id.classification) TextView classificationTextView;
    @BindView(R.id.totalTime) TextView totalTimeTextView;
    @BindView(R.id.explanation) TextView explanationTextView;
    @BindView(R.id.recipe_preview_image) ImageView image;
//    @BindView(R.id.preview_title) TextView tv_title;
    @BindView(R.id.calorie) TextView tv_calorie;

    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_basic_information.db";
    public static final String TABLE_NAME = "recipe_basic_information";

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe_preview);
        setToolbarName("레시피 미리보기 - " + getSelectedRecipeName());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) supportPostponeEnterTransition();

        DBUtils.setDB(this, ROOT_DIR, DB_Name);

        db = DatabaseFactory.create(this, DB_Name);
        cursor = db.rawQuery("SELECT recipe_name, simplification, food_classification, cooking_time, URL, amount FROM " + TABLE_NAME + " WHERE recipe_name=\"" + getSelectedRecipeName() + "\"", null);
        cursor.moveToFirst();
        explanationTextView.setText(cursor.getString(1));
        classificationTextView.setText(cursor.getString(2));
        totalTimeTextView.setText(cursor.getString(3));
//        tv_title.setText(getSelectedRecipeName());
        tv_calorie.setText(cursor.getString(5));
        Glide.with(this).load(cursor.getString(4)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) startPostponedEnterTransition();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) startPostponedEnterTransition();
                return false;
            }
        }).into(image);
    }

    @OnClick(R.id.preview_fab)
    public void onClickFab(View view) {
        Intent intent=new Intent(getApplicationContext(), RecipeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("selectedRecipe",getSelectedRecipeId());
        bundle.putString("RecipeName",getSelectedRecipeName());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private String getSelectedRecipeId() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        return bundle.getString("selectedRecipe");
    }

    private String getSelectedRecipeName() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        return bundle.getString("RecipeName");
    }
}
