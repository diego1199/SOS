package com.certificacion.dauza.sos.Models;

import com.certificacion.dauza.sos.EmergencyRequestActivity;

import java.util.Date;

/**
 * Created by imac on 16/12/17.
 */

public class EmergencyServiceRequest {

    public int emergencyServiceType;
    public double latitude;
    public double longitude;
    public String userId;
    public String medicalRecordId;
    public boolean sameAsCurrentUser;
    public String comments;
    public Long requestDate;
    public String deviceToken;

    public EmergencyServiceRequest(){

    }

    public EmergencyServiceRequest(int emergencyServiceType, double latitude, double longitude, String userId, String medicalRecordId, boolean sameAsCurrentUser, String comments, Long date, String deviceToken) {
        this.emergencyServiceType = emergencyServiceType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.medicalRecordId = medicalRecordId;
        this.sameAsCurrentUser = sameAsCurrentUser;
        this.comments = comments;
        this.requestDate = date;
        this.deviceToken = deviceToken;
    }

    public int getEmergencyServiceType() {
        return emergencyServiceType;
    }

    public void setEmergencyServiceType(int emergencyServiceType) {
        this.emergencyServiceType = emergencyServiceType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getDateOfService() {
        return requestDate;
    }

    public void setDateOfService(Long dateOfService) {
        this.requestDate = dateOfService;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public boolean isSameAsCurrentUser() {
        return sameAsCurrentUser;
    }

    public void setSameAsCurrentUser(boolean sameAsCurrentUser) {
        this.sameAsCurrentUser = sameAsCurrentUser;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Long requestDate) {
        this.requestDate = requestDate;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
