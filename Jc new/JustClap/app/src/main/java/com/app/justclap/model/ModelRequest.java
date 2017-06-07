package com.app.justclap.model;

/**
 * Created by hemanta on 16-04-2017.
 */

public class ModelRequest {

    private String RequestId, ServiceId, IsUnidirectional, RequestCode, RequestLatitude, RequestLongitude, RequestDate,
            RequestTime, RequestStatusId, RequestStatus, ServiceName, ServiceIcon, QuoteVendorsArray, QuoteId,
            QuoteValue, QuoteDate, VendorName, VendorId, VendorEmail, VendorMobile, VendorAddress, VendorLongitude, VendorLatitude,
            VendorProfileImage,RequestStatusImage;
    private int QuoteCount;
    private int rowType;


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }


    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getIsUnidirectional() {
        return IsUnidirectional;
    }

    public void setIsUnidirectional(String isUnidirectional) {
        IsUnidirectional = isUnidirectional;
    }

    public String getRequestCode() {
        return RequestCode;
    }

    public void setRequestCode(String requestCode) {
        RequestCode = requestCode;
    }

    public String getRequestLatitude() {
        return RequestLatitude;
    }

    public void setRequestLatitude(String requestLatitude) {
        RequestLatitude = requestLatitude;
    }

    public String getRequestLongitude() {
        return RequestLongitude;
    }

    public void setRequestLongitude(String requestLongitude) {
        RequestLongitude = requestLongitude;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }

    public String getRequestTime() {
        return RequestTime;
    }

    public void setRequestTime(String requestTime) {
        RequestTime = requestTime;
    }

    public String getRequestStatusId() {
        return RequestStatusId;
    }

    public void setRequestStatusId(String requestStatusId) {
        RequestStatusId = requestStatusId;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServiceIcon() {
        return ServiceIcon;
    }

    public void setServiceIcon(String serviceIcon) {
        ServiceIcon = serviceIcon;
    }


    public String getQuoteVendorsArray() {
        return QuoteVendorsArray;
    }

    public void setQuoteVendorsArray(String quoteVendorsArray) {
        QuoteVendorsArray = quoteVendorsArray;
    }

    public String getQuoteId() {
        return QuoteId;
    }

    public void setQuoteId(String quoteId) {
        QuoteId = quoteId;
    }

    public String getQuoteValue() {
        return QuoteValue;
    }

    public void setQuoteValue(String quoteValue) {
        QuoteValue = quoteValue;
    }

    public String getQuoteDate() {
        return QuoteDate;
    }

    public void setQuoteDate(String quoteDate) {
        QuoteDate = quoteDate;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public String getVendorId() {
        return VendorId;
    }

    public void setVendorId(String vendorId) {
        VendorId = vendorId;
    }

    public String getVendorEmail() {
        return VendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        VendorEmail = vendorEmail;
    }

    public String getVendorMobile() {
        return VendorMobile;
    }

    public void setVendorMobile(String vendorMobile) {
        VendorMobile = vendorMobile;
    }

    public String getVendorAddress() {
        return VendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        VendorAddress = vendorAddress;
    }

    public String getVendorLongitude() {
        return VendorLongitude;
    }

    public void setVendorLongitude(String vendorLongitude) {
        VendorLongitude = vendorLongitude;
    }

    public String getVendorLatitude() {
        return VendorLatitude;
    }

    public void setVendorLatitude(String vendorLatitude) {
        VendorLatitude = vendorLatitude;
    }

    public String getVendorProfileImage() {
        return VendorProfileImage;
    }

    public void setVendorProfileImage(String vendorProfileImage) {
        VendorProfileImage = vendorProfileImage;
    }

    public int getQuoteCount() {
        return QuoteCount;
    }

    public void setQuoteCount(int quoteCount) {
        QuoteCount = quoteCount;
    }

    public String getRequestStatusImage() {
        return RequestStatusImage;
    }

    public void setRequestStatusImage(String requestStatusImage) {
        RequestStatusImage = requestStatusImage;
    }
}
