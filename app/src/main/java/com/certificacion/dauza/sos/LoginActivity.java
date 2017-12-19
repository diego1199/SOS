package com.certificacion.dauza.sos;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static com.certificacion.dauza.sos.Helpers.Constant.MEDICAL_RECORDS_COLLECTION_NAME;
import static com.certificacion.dauza.sos.Helpers.Constant.MEDICAL_RECORD_ID_SP_KEY;
import static com.certificacion.dauza.sos.Helpers.Constant.USER_ALREADY_CREATED_IE_KEY;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private Button openRegisterButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

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
                goToRegisterScreen(false);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                            searchMedicalRecordForUser();
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

    private void goToRegisterScreen(boolean userAlreadyCreated) {
        Intent intent = new Intent(this, RegisterActivity.class);
        if (userAlreadyCreated) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(USER_ALREADY_CREATED_IE_KEY, true);
        }
        startActivity(intent);
    }

    private boolean validations() {

        //Validations from bottom to top. Because we need to focus the first text edit error before any other errors.

        boolean isValid = true;

        Validation passwordMinLengthValidation = DataHelper.passwordMinLength(passwordEditText);
        if (!passwordMinLengthValidation.valid) {
            isValid = isValid && passwordMinLengthValidation.valid;
        }

        Validation emailValidation = DataHelper.isEmail(emailEditText);
        if (!emailValidation.valid) {
            isValid = isValid && emailValidation.valid;
        }

        return isValid;
    }

    private void searchMedicalRecordForUser() {
        final SweetAlertDialog loadingAlert = UserInterfaceHelper.showLoadingAlert(this);
        db.collection(MEDICAL_RECORDS_COLLECTION_NAME)
                .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loadingAlert.dismiss();
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (DocumentSnapshot medicalRecord : task.getResult()) {
                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString(MEDICAL_RECORD_ID_SP_KEY, medicalRecord.getId());
                                    editor.apply();
                                    break;
                                }
                                goToMainScreen();
                            }
                            else {
                                goToRegisterScreen(true);
                            }
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            UserInterfaceHelper.showErrorAlert(context, "Oops", "No se pudo iniciar sesión. Intenta de nuevo.");
                        }
                    }
                });
    }
}
