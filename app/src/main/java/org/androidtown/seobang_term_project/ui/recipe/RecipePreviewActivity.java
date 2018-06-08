package org.androidtown.seobang_term_project.ui.recipe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    @BindView(R.id.classification)
    TextView classificationTextView;
    @BindView(R.id.totalTime)
    TextView totalTimeTextView;
    @BindView(R.id.explanation)
    TextView explanationTextView;
    @BindView(R.id.ingredient)
    TextView ingredientTextView;
    @BindView(R.id.recipe_preview_image)
    ImageView image;
    //    @BindView(R.id.preview_title) TextView tv_title;
    @BindView(R.id.calorie)
    TextView tv_calorie;
    @BindView(R.id.preview_fab)
    FloatingActionButton btn;
    @BindView(R.id.relativeLayout)
    RelativeLayout layout;

    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_basic_information.db";
    public static final String TABLE_NAME = "recipe_basic_information";
    public static final String DB_Name_2 = "recipe_ingredient_info.db";
    public static final String TABLE_NAME_2 = "recipe_ingredient_info";

    private SQLiteDatabase db;
    private SQLiteDatabase db_2;
    private Cursor cursor;

    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("onCreate", "생성되었다");
        super.onCreate(savedInstanceState);
        setContentViewById(R.layout.activity_recipe_preview);
        setToolbarName("레시피 미리보기 - " + getSelectedRecipeName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) supportPostponeEnterTransition();

        String ingredient = "";

        DBUtils.setDB(this, ROOT_DIR, DB_Name);
        DBUtils.setDB(this, ROOT_DIR, DB_Name_2);

        db = DatabaseFactory.create(this, DB_Name);
        db_2 = DatabaseFactory.create(this, DB_Name_2);

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    startPostponedEnterTransition();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    startPostponedEnterTransition();
                return false;
            }
        }).into(image);

        cursor = db_2.rawQuery("SELECT ingredient_name,ingredient_amount FROM " + TABLE_NAME_2 + " WHERE recipe_code=\"" + getSelectedRecipeId() + "\"", null);
        while (cursor.moveToNext()) {
            ingredient += cursor.getString(0) + " " + cursor.getString(1) + ", ";
        }

        ingredient = ingredient.substring(0, ingredient.length() - 2);
        ingredientTextView.setText(ingredient);

        final Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        final Animation hide = AnimationUtils.loadAnimation(this, R.anim.hide_layout);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.bounce_interpolator));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    flag = false;
                    btn.setVisibility(View.INVISIBLE);
                    btn.startAnimation(hide);
                } else {
                    flag = true;
                    btn.setVisibility(View.VISIBLE);
                    btn.startAnimation(slide);
                }
            }
        });
    }

    @OnClick(R.id.preview_fab)
    public void onClickFab(View view) {
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("selectedRecipe", getSelectedRecipeId());
        bundle.putString("RecipeName", getSelectedRecipeName());
        intent.putExtras(bundle);
        startActivity(intent);
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

