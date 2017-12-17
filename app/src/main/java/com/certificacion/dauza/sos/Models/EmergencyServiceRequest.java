package com.certificacion.dauza.sos.Models;

import com.certificacion.dauza.sos.EmergencyRequestActivity;

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

    public EmergencyServiceRequest(){

    }

    public EmergencyServiceRequest(int emergencyServiceType, double latitude, double longitude, String userId, String medicalRecordId, boolean sameAsCurrentUser, String comments) {
        this.emergencyServiceType = emergencyServiceType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.medicalRecordId = medicalRecordId;
        this.sameAsCurrentUser = sameAsCurrentUser;
        this.comments = comments;
    }
}
