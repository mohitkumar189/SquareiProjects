package com.app.justclap.model;

/**
 * Created by admin on 18-06-2016.
 */
public class ModelChat {


    String sender_id;
    String reciever_id;
    String message;
    String date_time;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    String receiverName;
    long time_in_milli;
    int is_On_Server;
    int isRead;
    int rowType;


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }


    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    String sender_name = "";

    public int is_On_Server() {
        return is_On_Server;
    }

    public void setIs_On_Server(int is_On_Server) {
        this.is_On_Server = is_On_Server;
    }

    public int isRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }


    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public long getTime_in_milli() {
        return time_in_milli;
    }

    public void setTime_in_milli(long time_in_milli) {
        this.time_in_milli = time_in_milli;
    }


}
