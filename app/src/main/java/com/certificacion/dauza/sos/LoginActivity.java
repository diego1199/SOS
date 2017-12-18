package com.certificacion.dauza.sos;


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

import com.certificacion.dauza.sos.Helpers.DataHelper;
import com.certificacion.dauza.sos.Models.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.certificacion.dauza.sos.Helpers.UserInterfaceHelper;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private Button openRegisterButton;

    private FirebaseAuth firebaseAuth;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        openRegisterButton = (Button) findViewById(R.id.openRegisterButton);
        openRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterScreen();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        context = this;
    }

    private void logIn() {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!validations()) {
            return;
        }
        final SweetAlertDialog loadingAlert = UserInterfaceHelper.showLoadingAlert(this);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingAlert.dismiss();
                        if (task.isSuccessful()) {
                            //TODO retrieve medical record id
                            goToMainScreen();
                        } else {
                            UserInterfaceHelper.showErrorAlert(context, "Oops", "No se pudo iniciar sesión. Intenta de nuevo.");
                        }
                    }
                });
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToRegisterScreen() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean validations() {

        boolean isValid = true;

        Validation emailValidation = DataHelper.isEmail(emailEditText);
        if (!emailValidation.valid) {
            //UserInterfaceHelper.showErrorAlert(context, "Oops", emailValidation.message);
            isValid = emailValidation.valid;
        }

        Validation passwordMinLengthValidation = DataHelper.passwordMinLength(passwordEditText);
        if (!passwordMinLengthValidation.valid) {
            //UserInterfaceHelper.showErrorAlert(context, "Oops", passwordMinLengthValidation.message);
            isValid = passwordMinLengthValidation.valid;
        }

        return isValid;
    }
}
