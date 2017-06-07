package com.app.justclap.models;

/**
 * Created by admin on 28-03-2016.
 */
public class ModelVendorData {

    private String seervicename;
    private String bookinTime;
    private String serviceID;
    private String customerID;
    private String email;
    private String latitude;
    private String longitude;
    private String mobile;
    private String status;
    private String vendorname;
    private String serviceType;
    private String address;
    private String responseCount;
    private String responseTime;
    private String question;
    private String is_hire;

    public String getVendorLatitude() {
        return vendorLatitude;
    }

    public void setVendorLatitude(String vendorLatitude) {
        this.vendorLatitude = vendorLatitude;
    }

    public String getVendorLongitude() {
        return vendorLongitude;
    }

    public void setVendorLongitude(String vendorLongitude) {
        this.vendorLongitude = vendorLongitude;
    }

    public String getVendorDistance() {
        return vendorDistance;
    }

    public void setVendorDistance(String vendorDistance) {
        this.vendorDistance = vendorDistance;
    }

    public String getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(String sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public String getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(String sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    String vendorLatitude;
    String vendorLongitude;
    String vendorDistance;
    String sourceLatitude;
    String sourceLongitude;

    public String getCustomerResponse() {
        return CustomerResponse;
    }

    public void setCustomerResponse(String customerResponse) {
        CustomerResponse = customerResponse;
    }

    String CustomerResponse;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    String profileImage;

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    String serviceCharge;

    public String getRequestId() {
        return requestId;
    }
    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    String searchId;
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    String requestId;
    public String getIs_quoted() {
        return is_quoted;
    }

    public void setIs_quoted(String is_quoted) {
        this.is_quoted = is_quoted;
    }

    public String getIs_hire() {
        return is_hire;
    }

    public void setIs_hire(String is_hire) {
        this.is_hire = is_hire;
    }

    String is_quoted;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    String answer;
    int rowType;


    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(String responseCount) {
        this.responseCount = responseCount;
    }


    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getSeervicename() {
        return seervicename;
    }

    public void setSeervicename(String seervicename) {
        this.seervicename = seervicename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBookinTime() {
        return bookinTime;
    }

    public void setBookinTime(String bookinTime) {
        this.bookinTime = bookinTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }


}
