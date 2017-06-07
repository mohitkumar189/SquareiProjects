package com.app.justclap.models;

/**
 * Created by admin on 20-03-2016.
 */
public class ModelRideLocationType {

    String questionText;
    String isMandatory;
    String questionType;
    String input_type;
    String placeholder;
    String questionTypeID;
    String srcaddress = "";
    String destaddress = "";
    String srclatitude = "";
    String destlatitude = "";
    String srclongitude = "";
    String destlongitude = "";
    String selectedTime = "";
    String selectedDate = "";
    String questionID;


    public String getSrcaddress() {
        return srcaddress;
    }

    public void setSrcaddress(String srcaddress) {
        this.srcaddress = srcaddress;
    }

    public String getDestaddress() {
        return destaddress;
    }

    public void setDestaddress(String destaddress) {
        this.destaddress = destaddress;
    }

    public String getSrclatitude() {
        return srclatitude;
    }

    public void setSrclatitude(String srclatitude) {
        this.srclatitude = srclatitude;
    }

    public String getDestlatitude() {
        return destlatitude;
    }

    public void setDestlatitude(String destlatitude) {
        this.destlatitude = destlatitude;
    }

    public String getSrclongitude() {
        return srclongitude;
    }

    public void setSrclongitude(String srclongitude) {
        this.srclongitude = srclongitude;
    }

    public String getDestlongitude() {
        return destlongitude;
    }

    public void setDestlongitude(String destlongitude) {
        this.destlongitude = destlongitude;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
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

    public String getQuestionImageUrl() {
        return QuestionImageUrl;
    }

    public void setQuestionImageUrl(String questionImageUrl) {
        QuestionImageUrl = questionImageUrl;
    }

    String QuestionImageUrl;

}
