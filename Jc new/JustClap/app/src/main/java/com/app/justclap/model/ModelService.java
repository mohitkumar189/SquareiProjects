package com.app.justclap.model;

/**
 * Created by hemanta on 17-03-2017.
 */

public class ModelService {

    private String serviceId;
    private String serviceName;
    private String imageUrl;
    private String description;
    private String is_uniDirectional;
    private String is_naukri;
    private String categoryID;
    private String categoryName;
    private String categoryBGImage;
    private String servicesCount;
    private String serviceIcon;
    private String servicesArray, ServiceOffer;
    private int ServiceRatting;
    private String ReviewId, ReviewSubject, ReviewText, CreatedOn, UserId, UserName, UserEmail;
    private String VideoId, VideoUrl, VideoThumbUrl, Id;


    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoId) {
        VideoId = videoId;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getVideoThumbUrl() {
        return VideoThumbUrl;
    }

    public void setVideoThumbUrl(String videoThumbUrl) {
        VideoThumbUrl = videoThumbUrl;
    }

    public String getReviewId() {
        return ReviewId;
    }

    public void setReviewId(String reviewId) {
        ReviewId = reviewId;
    }

    public String getReviewSubject() {
        return ReviewSubject;
    }

    public void setReviewSubject(String reviewSubject) {
        ReviewSubject = reviewSubject;
    }

    public String getReviewText() {
        return ReviewText;
    }

    public void setReviewText(String reviewText) {
        ReviewText = reviewText;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
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

    public String getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(String serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public String getIs_naukri() {
        return is_naukri;
    }

    public void setIs_naukri(String is_naukri) {
        this.is_naukri = is_naukri;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryBGImage() {
        return categoryBGImage;
    }

    public void setCategoryBGImage(String categoryBGImage) {
        this.categoryBGImage = categoryBGImage;
    }

    public String getServicesCount() {
        return servicesCount;
    }

    public void setServicesCount(String servicesCount) {
        this.servicesCount = servicesCount;
    }


    public String getIs_uniDirectional() {
        return is_uniDirectional;
    }

    public void setIs_uniDirectional(String is_uniDirectional) {
        this.is_uniDirectional = is_uniDirectional;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setServicesArray(String servicesArray) {
        this.servicesArray = servicesArray;
    }

    public String getServicesArray() {
        return servicesArray;
    }

    public String getServiceOffer() {
        return ServiceOffer;
    }

    public void setServiceOffer(String serviceOffer) {
        ServiceOffer = serviceOffer;
    }

    public int getServiceRatting() {
        return ServiceRatting;
    }

    public void setServiceRatting(int serviceRatting) {
        ServiceRatting = serviceRatting;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
