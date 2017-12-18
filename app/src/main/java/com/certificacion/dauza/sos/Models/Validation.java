package com.certificacion.dauza.sos.Models;

/**
 * Created by Zarpick on 18/12/17.
 * 2017 SOS. All rights reserverd.
 */

public class Validation {

    public String message;
    public boolean valid;

    public Validation(boolean valid, String message) {
        this.message = message;
        this.valid = valid;
    }
}
