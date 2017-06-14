package com.app.schoolapp.model;

/**
 * Created by SQ3 on 5/15/2017.
 */

public class ModelData {
    private String title;
    private String attn_status;
    private String attn_date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAttn_status(String attn_status) {
        this.attn_status = attn_status;
    }

    public void setAttn_date(String attn_date) {
        this.attn_date = attn_date;
    }

    public String getAttn_status() {
        return attn_status;
    }

    public String getAttn_date() {
        return attn_date;
    }
}
