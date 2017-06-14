package com.app.schoolapp.model;

/**
 * Created by mohit kumar on 6/13/2017.
 */

public class TeachersListData {
    private String id, name, image, subject;

    public TeachersListData(String id, String name, String image, String subject) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
