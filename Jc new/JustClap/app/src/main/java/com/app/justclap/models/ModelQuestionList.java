package com.app.justclap.models;

/**
 * Created by admin on 09-02-2016.
 */
public class ModelQuestionList {


    String questionText;
    String questionID;
    String isMandatory;
    String questionType;
    String questionTypeID;
    String optionText;
    String optionID;
    String optionValue;
    String optionArray;
    String questionArray;
    String day;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    String date;

    public String getInput_type() {
        return input_type;
    }

    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }

    String input_type;

    public int getSelection_position() {
        return Selection_position;
    }

    public void setSelection_position(int selection_position) {
        Selection_position = selection_position;
    }

    int Selection_position;
    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    String placeholder;

    public String getQuestionArray() {
        return questionArray;
    }

    public void setQuestionArray(String questionArray) {
        this.questionArray = questionArray;
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

    public String getQuestionTypeID() {
        return questionTypeID;
    }

    public void setQuestionTypeID(String questionTypeID) {
        this.questionTypeID = questionTypeID;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getOptionID() {
        return optionID;
    }

    public void setOptionID(String optionID) {
        this.optionID = optionID;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionArray() {
        return optionArray;
    }

    public void setOptionArray(String optionArray) {
        this.optionArray = optionArray;
    }


}
