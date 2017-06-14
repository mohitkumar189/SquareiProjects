package com.app.schoolapp.model;

/**
 * Created by mohit kumar on 5/29/2017.
 */

public class ModelStudentDiary {
    String title, content, subject, postedBy,sentDate;

    public ModelStudentDiary(String title, String content, String subject, String postedBy, String sentDate) {
        this.title = title;
        this.content = content;
        this.subject = subject;
        this.postedBy = postedBy;
        this.sentDate = sentDate;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }
}
