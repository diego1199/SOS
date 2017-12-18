package com.certificacion.dauza.sos.Helpers;

import android.content.Context;

import com.certificacion.dauza.sos.R;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by imac on 16/12/17.
 * 2017 SOS. All rights reserverd.
 */

public class UserInterfaceHelper {

    public static SweetAlertDialog showLoadingAlert(Context context) {

        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(context.getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    public static SweetAlertDialog showSuccessAlert(Context context, String title, String contentMessage) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(contentMessage)
                .setConfirmText("Aceptar");
        dialog.show();
        return dialog;
    }

    public static SweetAlertDialog showErrorAlert(Context context, String title, String contentMessage) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(contentMessage);
        dialog.show();
        return dialog;
    }

}
