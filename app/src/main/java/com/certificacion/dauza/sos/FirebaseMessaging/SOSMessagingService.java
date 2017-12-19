package com.certificacion.dauza.sos.FirebaseMessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.certificacion.dauza.sos.Helpers.UserInterfaceHelper;
import com.certificacion.dauza.sos.MainActivity;
import com.certificacion.dauza.sos.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by imac on 18/12/17.
 * 2017 SOS. All rights reserverd.
 */

public class SOSMessagingService extends FirebaseMessagingService {

    private final static String TAG = "SOSMessagingService";

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.e(TAG, "NEW NOTIFICATION: " + remoteMessage.getNotification().getTitle() + " - " + remoteMessage.getNotification().getBody());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(getApplicationContext());
//                }
//                builder.setTitle(remoteMessage.getNotification().getTitle())
//                        .setMessage(remoteMessage.getNotification().getBody())
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
                //UserInterfaceHelper.showSuccessAlert(getBaseContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                Toast.makeText(getApplicationContext(), remoteMessage.getNotification().getBody(), Toast.LENGTH_LONG).show();


            }
        });
    }
}


