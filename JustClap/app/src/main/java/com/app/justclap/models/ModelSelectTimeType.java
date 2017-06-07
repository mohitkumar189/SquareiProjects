package com.app.justclap.models;

import java.util.ArrayList;

/**
 * Created by admin on 20-03-2016.
 */
public class ModelSelectTimeType {

    String questionID;
    String questionText;
    String isMandatory;
    String questionType;
    String input_type;
    String placeholder;
    String questionTypeID;
    String selectedTime="";
    ArrayList<String> time_id = new ArrayList<>();
    ArrayList<String> optionArray = new ArrayList<>();
    int selectedTimeId;



    public ArrayList<String> getTime_id() {
        return time_id;
    }

    public void setTime_id(ArrayList<String> time_id) {
        this.time_id = time_id;
    }


    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }



    public ArrayList<String> getOptionArray() {
        return optionArray;
    }

    public void setOptionArray(ArrayList<String> optionArray) {
        this.optionArray = optionArray;
    }


    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getInput_type() {
        return input_type;
    }

    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getQuestionTypeID() {
        return questionTypeID;
    }

    public void setQuestionTypeID(String questionTypeID) {
        this.questionTypeID = questionTypeID;
    }

    public int getSelectedTimeId() {
        return selectedTimeId;
    }

    public void setSelectedTimeId(int selectedTimeId) {
        this.selectedTimeId = selectedTimeId;
    }
}
