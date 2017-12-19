package com.certificacion.dauza.sos.FirebaseMessaging;

import android.util.Log;

import com.certificacion.dauza.sos.Helpers.UserInterfaceHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by imac on 18/12/17.
 * 2017 SOS. All rights reserverd.
 */

public class SOSMessagingService extends FirebaseMessagingService {

    private final static String TAG = "SOSMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String messageId = remoteMessage.getMessageId();
        String titulo = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.e(TAG, messageId + " - " + titulo + " - " + message);
        UserInterfaceHelper.showSuccessAlert(getApplicationContext(), titulo, message);
    }

}
