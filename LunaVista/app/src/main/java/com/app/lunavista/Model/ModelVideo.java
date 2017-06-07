package com.app.lunavista.Model;

/**
 * Created by admin on 17-07-2016.
 */
public class ModelVideo {


    String songId;
    String songThumb;
    String songTitle;
    String songDescription;
    String viewCount;
    String isFavorite;
    String comment;
    String name;
    String date;
    String recordingRemarks;

    public String getRecordingRemarks() {
        return recordingRemarks;
    }

    public void setRecordingRemarks(String recordingRemarks) {
        this.recordingRemarks = recordingRemarks;
    }

    public String getRecordingURL() {
        return recordingURL;
    }

    public void setRecordingURL(String recordingURL) {
        this.recordingURL = recordingURL;
    }

    public String getRecordingThumbURL() {
        return recordingThumbURL;
    }

    public void setRecordingThumbURL(String recordingThumbURL) {
        this.recordingThumbURL = recordingThumbURL;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    String recordingURL;
    String recordingThumbURL;
    String created_on;

    public String getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(String isRecording) {
        this.isRecording = isRecording;
    }

    String isRecording;

    public String getSongVideoURL() {
        return songVideoURL;
    }

    public void setSongVideoURL(String songVideoURL) {
        this.songVideoURL = songVideoURL;
    }

    String songVideoURL;

    public String getSongAspectRatio() {
        return songAspectRatio;
    }

    public void setSongAspectRatio(String songAspectRatio) {
        this.songAspectRatio = songAspectRatio;
    }

    String songAspectRatio;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    String commentId;

    public String getSongDetail() {
        return songDetail;
    }

    public void setSongDetail(String songDetail) {
        this.songDetail = songDetail;
    }

    String songDetail;
    int rowType;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongThumb() {
        return songThumb;
    }

    public void setSongThumb(String songThumb) {
        this.songThumb = songThumb;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongDescription() {
        return songDescription;
    }

    public void setSongDescription(String songDescription) {
        this.songDescription = songDescription;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }


}
