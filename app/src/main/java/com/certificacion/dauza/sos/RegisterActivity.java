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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.certificacion.dauza.sos.Helpers.UserInterfaceHelper;
import com.certificacion.dauza.sos.Models.MedicalRecord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static com.certificacion.dauza.sos.Helpers.Constant.MEDICAL_RECORDS_COLLECTION_NAME;
import static com.certificacion.dauza.sos.Helpers.Constant.MEDICAL_RECORD_ID_SP_KEY;
import static com.certificacion.dauza.sos.Helpers.Constant.USER_AUTH_COMPLETED_SP_KEY;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText birthdateEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText allergiesEditText;
    private Spinner bloodGroupSpinner;
    private Button registerButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        repeatPasswordEditText = (EditText) findViewById(R.id.repeatPasswordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        firstNameEditText  = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText  = (EditText) findViewById(R.id.lastNameEditText);
        birthdateEditText = findViewById(R.id.birthDateEditText);
        heightEditText  = (EditText) findViewById(R.id.heightEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        allergiesEditText = (EditText) findViewById(R.id.allergiesEditText);
        bloodGroupSpinner = (Spinner) findViewById(R.id.bloodGroupSpinner);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        context = this;

    }

    private void register() {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        final SweetAlertDialog loadingAlert = UserInterfaceHelper.showLoadingAlert(this);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingAlert.dismiss();
                        if (task.isSuccessful()) {
                            saveNewUserMedicalRecord(firebaseAuth.getCurrentUser().getUid());
                            //goToMainScreen();
                        } else {
                            showErrorMessage(task.getException().toString());
                        }
                    }
                });
    }

    private void showErrorMessage(String s) {
        Toast.makeText(getApplicationContext(), "Se detectó un error: " + s, Toast.LENGTH_LONG).show();
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void saveNewUserMedicalRecord(String userId) {

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        double height = Double.parseDouble(heightEditText.getText().toString());
        double weight = Double.parseDouble(weightEditText.getText().toString());
        String allergies = allergiesEditText.getText().toString();
        String bloodType = bloodGroupSpinner.getSelectedItem().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
        boolean authCompleted = true;
        try {
            Date d = dateFormat.parse(birthdateEditText.getText().toString());
        } catch (ParseException e) {
            UserInterfaceHelper.showErrorAlert(context, "Oops", "No es una fecha valida. Intenta de nuevo");
            return;
        }

        final SweetAlertDialog loadingAlert = UserInterfaceHelper.showLoadingAlert(this);

        CollectionReference medicalRecords = db.collection(MEDICAL_RECORDS_COLLECTION_NAME);
        MedicalRecord newMedicalRecord = new MedicalRecord(firstName, lastName, weight, height, allergies, bloodType, userId, authCompleted);
        medicalRecords.add(newMedicalRecord)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //TODO remove preferences on log out
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(MEDICAL_RECORD_ID_SP_KEY, documentReference.getId());
                editor.putBoolean(USER_AUTH_COMPLETED_SP_KEY, true);
                editor.apply();
                loadingAlert.dismiss();
                goToMainScreen();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingAlert.dismiss();
                UserInterfaceHelper.showErrorAlert(context, "Oops", "Hubo un problema al crear el usuario. Intenta de nuevo.");
                Log.w(TAG, "Error writing document", e);
            }
        });

    }
}
