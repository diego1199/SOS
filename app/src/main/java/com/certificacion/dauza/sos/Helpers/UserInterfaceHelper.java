package com.certificacion.dauza.sos.Helpers;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

/**
 * Created by imac on 16/12/17.
 */

public class UserInterfaceHelper {

    public static void showLoading(ProgressBar progressBar, Window window){
        progressBar.setVisibility(View.VISIBLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void dismissLoading(ProgressBar progressBar, Window window){
        progressBar.setVisibility(View.INVISIBLE);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
