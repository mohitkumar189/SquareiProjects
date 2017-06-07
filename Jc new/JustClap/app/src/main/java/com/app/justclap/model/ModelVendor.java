package com.app.justclap.model;

import org.json.JSONObject;

/**
 * Created by hemanta on 25-04-2017.
 */

public class ModelVendor {

    private String LeadId, RequestId, RequestCode, RequestLatitude, RequestLongitude, IsReviewed, OfferPercentage, RewardPoint, RequestQueriesArray;
    private String RequestDate, RequestTime, UserId, UserName, UserEmail, UserMobile, UserImage, UserCoverImage, ServiceId, IsUnidirectional, ServiceName, ServiceIcon;
    private int RowType;
    private String answer, question;
    private JSONObject jsonArray;

    public String getLeadId() {
        return LeadId;
    }

    public void setLeadId(String leadId) {
        LeadId = leadId;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
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

    public String getIsReviewed() {
        return IsReviewed;
    }

    public void setIsReviewed(String isReviewed) {
        IsReviewed = isReviewed;
    }

    public String getOfferPercentage() {
        return OfferPercentage;
    }

    public void setOfferPercentage(String offerPercentage) {
        OfferPercentage = offerPercentage;
    }

    public String getRewardPoint() {
        return RewardPoint;
    }

    public void setRewardPoint(String rewardPoint) {
        RewardPoint = rewardPoint;
    }

    public String getRequestQueriesArray() {
        return RequestQueriesArray;
    }

    public void setRequestQueriesArray(String requestQueriesArray) {
        RequestQueriesArray = requestQueriesArray;
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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getUserCoverImage() {
        return UserCoverImage;
    }

    public void setUserCoverImage(String userCoverImage) {
        UserCoverImage = userCoverImage;
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

    public int getRowType() {
        return RowType;
    }

    public void setRowType(int rowType) {
        RowType = rowType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public JSONObject getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONObject jsonArray) {
        this.jsonArray = jsonArray;
    }
}
