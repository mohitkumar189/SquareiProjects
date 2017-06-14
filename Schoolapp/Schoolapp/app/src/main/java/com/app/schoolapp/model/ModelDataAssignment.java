package com.app.schoolapp.model;

/**
 * Created by mohit kumar on 6/2/2017.
 */

public class ModelDataAssignment {
    private String id, title, subject, assignedClass, assignedSection, content, submissiondate, created, writtenBy, imageurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModelDataAssignment(String id, String title, String subject, String assignedClass, String assignedSection, String content, String submissiondate, String created, String writtenBy, String imageurl) {

        this.id = id;

        this.title = title;
        this.subject = subject;
        this.assignedClass = assignedClass;
        this.assignedSection = assignedSection;
        this.content = content;
        this.submissiondate = submissiondate;
        this.created = created;
        this.writtenBy = writtenBy;
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAssignedClass() {
        return assignedClass;
    }

    public void setAssignedClass(String assignedClass) {
        this.assignedClass = assignedClass;
    }

    public String getAssignedSection() {
        return assignedSection;
    }

    public void setAssignedSection(String assignedSection) {
        this.assignedSection = assignedSection;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubmissiondate() {
        return submissiondate;
    }

    public void setSubmissiondate(String submissiondate) {
        this.submissiondate = submissiondate;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}