package org.androidtown.seobang_term_project.ui.recipe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import org.androidtown.seobang_term_project.R;
import org.androidtown.seobang_term_project.factory.DatabaseFactory;
import org.androidtown.seobang_term_project.utils.DBUtils;

public class RecipePreviewActivity extends AppCompatActivity {

    WebView previewWebView;
    TextView classificationTextView;
    TextView totalTimeTextView;
    TextView explanationTextView;
    Button goToProcessButton;

    public static final String ROOT_DIR = "/data/data/org.androidtown.seobang_term_project/databases/";
    public static final String DB_Name = "recipe_basic_information.db";
    public static final String TABLE_NAME = "recipe_basic_information";

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_preview);

        DBUtils.setDB(this, ROOT_DIR, DB_Name);

        db = DatabaseFactory.create(this, DB_Name);
        cursor = db.rawQuery("SELECT recipe_name,simplification,food_classification,cooking_time,URL FROM " + TABLE_NAME + " WHERE recipe_code=\"" + getSelectedRecipeId() + "\"", null);

        goToProcessButton = findViewById(R.id.goToProcess);
        explanationTextView = findViewById(R.id.explanation);
        totalTimeTextView = findViewById(R.id.totalTime);
        classificationTextView = findViewById(R.id.classification);
        previewWebView = findViewById(R.id.previewWebView);
        previewWebView.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = previewWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        while (cursor.moveToNext()) {
            explanationTextView.setText(cursor.getString(1));
            classificationTextView.setText(cursor.getString(2));
            totalTimeTextView.setText(cursor.getString(3));
            previewWebView.loadUrl(cursor.getString(4));
        }

        goToProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), RecipeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("selectedRecipe",getSelectedRecipeId());
                bundle.putString("RecipeName",getSelectedRecipeName());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
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
