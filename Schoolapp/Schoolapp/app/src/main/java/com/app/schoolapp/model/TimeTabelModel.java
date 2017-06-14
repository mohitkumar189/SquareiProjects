package com.app.schoolapp.model;

/**
 * Created by mohit kumar on 6/12/2017.
 */

public class TimeTabelModel {
    private String period, subject, day, teacher;

    public TimeTabelModel(String period, String subject, String day, String teacher) {
        this.period = period;
        this.subject = subject;
        this.day = day;
        this.teacher = teacher;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
