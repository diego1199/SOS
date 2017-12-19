package com.certificacion.dauza.sos;

/**
 * Created by Ignacio on 12/18/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.certificacion.dauza.sos.Helpers.UserInterfaceHelper;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;


public class ConfigurationsActivity extends AppCompatActivity {
    private android.support.v7.widget.SwitchCompat callContactsSwitch;
    private android.support.v7.widget.SwitchCompat sendSmsSwitch;
    private android.support.v7.widget.SwitchCompat notificationsSwitch;


}
