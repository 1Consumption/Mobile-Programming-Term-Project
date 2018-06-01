package org.androidtown.seobang_term_project.ui.history;

import android.graphics.drawable.Drawable;

public class HistoryList {
    private String img;
    private String Title;
    private String context;
    private String code;

    public void setCode(String code){
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
