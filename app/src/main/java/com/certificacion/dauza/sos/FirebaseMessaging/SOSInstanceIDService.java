package com.certificacion.dauza.sos.FirebaseMessaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by imac on 18/12/17.
 * 2017 SOS. All rights reserverd.
 */

public class SOSInstanceIDService extends FirebaseInstanceIdService {

    private final static String TAG = "SOSInstanceIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d(TAG, FirebaseInstanceId.getInstance().getToken());
    }

}
