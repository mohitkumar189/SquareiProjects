package com.app.schoolapp.model;

import java.util.ArrayList;

/**
 * Created by hemanta on 26-11-2016.
 */

public class ModelTimeTable {

    /*  String PeriodName;
      String daysArray;
      String day;
      String classname;
      String subject;
      String time;
      String teacher_name;


      public String getPeriodName() {
          return PeriodName;
      }

      public void setPeriodName(String periodName) {
          PeriodName = periodName;
      }

      public String getDaysArray() {
          return daysArray;
      }

      public void setDaysArray(String daysArray) {
          this.daysArray = daysArray;
      }

      public String getDay() {
          return day;
      }

      public void setDay(String day) {
          this.day = day;
      }

      public String getClassname() {
          return classname;
      }

      public void setClassname(String classname) {
          this.classname = classname;
      }

      public String getSubject() {
          return subject;
      }

      public void setSubject(String subject) {
          this.subject = subject;
      }

      public String getTime() {
          return time;
      }

      public void setTime(String time) {
          this.time = time;
      }

      public String getTeacher_name() {
          return teacher_name;
      }

      public void setTeacher_name(String teacher_name) {
          this.teacher_name = teacher_name;
      }

  */
/*
     private String  day, subject, teacher;
    private int periodName;

     public ModelTimeTable(int periodName, String day, String subject, String teacher) {
         this.periodName = periodName;
         this.day = day;
         this.subject = subject;
         this.teacher = teacher;
     }

     public int getPeriodName() {
         return periodName;
     }

     public void setPeriodName(int periodName) {
         this.periodName = periodName;
     }

     public String getDay() {
         return day;
     }

     public void setDay(String day) {
         this.day = day;
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
     }*/
    private ArrayList<PeriodDetails> periodDetails;
    private String day;
    private int period;

    public ModelTimeTable(ArrayList<PeriodDetails> periodDetails, String day, int period) {
        this.periodDetails = periodDetails;
        this.day = day;
        this.period = period;
    }

    public ArrayList<PeriodDetails> getPeriodDetails() {
        return periodDetails;
    }

    public void setPeriodDetails(ArrayList<PeriodDetails> periodDetails) {
        this.periodDetails = periodDetails;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "ModelTimeTable{" +
                "periodDetails=" + periodDetails +
                ", day='" + day + '\'' +
                ", period=" + period +
                '}';
    }
}
