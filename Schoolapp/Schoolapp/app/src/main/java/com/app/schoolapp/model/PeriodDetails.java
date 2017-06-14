package com.app.schoolapp.model;

/**
 * Created by mohit kumar on 6/12/2017.
 */

public class PeriodDetails {
    private String subject;
    private String teacher;

    public PeriodDetails(String subject, String teacher) {
        this.subject = subject;
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "PeriodDetails{" +
                "subject='" + subject + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}
