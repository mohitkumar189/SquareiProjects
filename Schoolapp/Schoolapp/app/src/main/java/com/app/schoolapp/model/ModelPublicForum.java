package com.app.schoolapp.model;

import java.io.Serializable;

/**
 * Created by mohit kumar on 5/31/2017.
 */

public class ModelPublicForum implements Serializable {
    private String id;
    private String subject;
    private String contentType;
    private String created;
    private String writtenBy;
    private String answers;

    public ModelPublicForum(String id, String subject, String contentType, String created, String writtenBy, String answers, String content) {

        this.id = id;
        this.subject = subject;
        this.contentType = contentType;
        this.created = created;
        this.writtenBy = writtenBy;
        this.answers = answers;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

}
