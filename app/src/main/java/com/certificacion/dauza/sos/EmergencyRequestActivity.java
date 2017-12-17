package com.certificacion.dauza.sos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.certificacion.dauza.sos.Helpers.UserInterfaceHelper;
import com.certificacion.dauza.sos.Models.EmergencyServiceRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmergencyRequestActivity extends AppCompatActivity {

    private static final String TAG = EmergencyRequestActivity.class.getSimpleName();
    private Switch sameAsCurrentUserSwitch;
    private EditText commentsEditText;
    private Button confirmationButton;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_request);

        progressBar = findViewById(R.id.progressBar);
        sameAsCurrentUserSwitch = findViewById(R.id.sameAsCurrentUserSwitch);
        commentsEditText = findViewById(R.id.commentsEditText);
        confirmationButton = findViewById(R.id.confirmationButton);
        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendConfirmation();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        context = this;
    }

    private void sendConfirmation() {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

        UserInterfaceHelper.showLoadingAlert(context);

        Intent intent = getIntent();
        String userId = firebaseAuth.getCurrentUser().getUid();
        double latitude = intent.getDoubleExtra("latitude", 0.0);
        double longitude = intent.getDoubleExtra("longitude", 0.0);
        int serviceType = intent.getIntExtra("serviceType", 0);
        boolean sameAsCurrentUser = sameAsCurrentUserSwitch.isChecked();
        String comments = commentsEditText.getText().toString();
        String medicalRedcordId = null;
        if (sameAsCurrentUser) {
            medicalRedcordId = "ss3gbxW42fs";
        }

        CollectionReference requests = db.collection("requests");
        EmergencyServiceRequest newRequest = new EmergencyServiceRequest(serviceType, latitude, longitude, userId, medicalRedcordId, sameAsCurrentUser, comments);

        requests.add(newRequest)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                UserInterfaceHelper.showSuccessAlert(context, "Servicio de emergencia en camino", "Espera en tu ubicación");
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                UserInterfaceHelper.showErrorAlert(context, "Oops", "Hubo un problema al solicitar el servicio. Intenta de nuevo.");
                Toast.makeText(context, getString(R.string.request_save_error), Toast.LENGTH_SHORT);
                Log.w(TAG, "Error writing document", e);
            }
        });
    }
}
