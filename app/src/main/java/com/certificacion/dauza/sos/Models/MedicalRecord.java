package com.certificacion.dauza.sos.Models;

/**
 * Created by imac on 17/12/17.
 * 2017 SOS. All rights reserverd.
 */

public class MedicalRecord {

    public String firstName;
    public String lastName;
    public double weight;
    public double height;
    public String allergies;
    public String bloodType;
    public String userId;
    public boolean authCompleted;


    public MedicalRecord () {

    }

    public MedicalRecord(String firstName, String lastName, double weight, double height, String allergies, String bloodType, String userId, boolean authCompleted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.weight = weight;
        this.height = height;
        this.allergies = allergies;
        this.bloodType = bloodType;
        this.userId = userId;
        this.authCompleted = authCompleted;
    }
}
