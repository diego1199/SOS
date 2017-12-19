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
    public Long dateOfService;

    public EmergencyServiceRequest(){

    }

    public EmergencyServiceRequest(int emergencyServiceType, double latitude, double longitude, String userId, String medicalRecordId, boolean sameAsCurrentUser, String comments, Long date) {
        this.emergencyServiceType = emergencyServiceType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.medicalRecordId = medicalRecordId;
        this.sameAsCurrentUser = sameAsCurrentUser;
        this.comments = comments;
        this.dateOfService = date;
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
        return dateOfService;
    }

    public void setDateOfService(Long dateOfService) {
        this.dateOfService = dateOfService;
    }
}
