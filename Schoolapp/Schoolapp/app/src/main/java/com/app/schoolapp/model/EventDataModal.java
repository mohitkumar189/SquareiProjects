package com.app.schoolapp.model;

/**
 * Created by mohit kumar on 6/2/2017.
 */

public class EventDataModal {
    String id, title, content, eventDate, created, writtenBy;

    public EventDataModal(String id, String title, String content, String eventDate, String created, String writtenBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.eventDate = eventDate;
        this.created = created;
        this.writtenBy = writtenBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
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
}