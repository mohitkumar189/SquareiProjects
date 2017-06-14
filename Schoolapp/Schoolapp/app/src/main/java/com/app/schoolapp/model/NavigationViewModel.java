package com.app.schoolapp.model;

import android.graphics.drawable.Drawable;

/**
 * Created by mohit kumar on 6/2/2017.
 */

public class NavigationViewModel {
    private String navText;
    private String id;
    private Drawable navImage;

    public NavigationViewModel(String navText, Drawable navImage, String id) {
        this.navText = navText;
        this.navImage = navImage;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNavText() {
        return navText;
    }

    public void setNavText(String navText) {
        this.navText = navText;
    }

    public Drawable getNavImage() {
        return navImage;
    }

    public void setNavImage(Drawable navImage) {
        this.navImage = navImage;
    }
}
