package com.certificacion.dauza.sos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.HashMap;
import java.util.Map;

import static com.certificacion.dauza.sos.Helpers.Constant.MEDICAL_RECORD_ID_SP_KEY;

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

        final SweetAlertDialog loadingAlert = UserInterfaceHelper.showLoadingAlert(context);

        Intent intent = getIntent();
        String userId = firebaseAuth.getCurrentUser().getUid();
        double latitude = intent.getDoubleExtra("latitude", 0.0);
        double longitude = intent.getDoubleExtra("longitude", 0.0);
        int serviceType = intent.getIntExtra("serviceType", 0);
        boolean sameAsCurrentUser = sameAsCurrentUserSwitch.isChecked();
        String comments = commentsEditText.getText().toString();
        String medicalRedcordId = null;
        if (sameAsCurrentUser) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            medicalRedcordId = sharedPref.getString(MEDICAL_RECORD_ID_SP_KEY, null);
        }
        long date = System.currentTimeMillis();
        CollectionReference requests = db.collection("requests");
        EmergencyServiceRequest newRequest = new EmergencyServiceRequest(serviceType, latitude, longitude, userId, medicalRedcordId, sameAsCurrentUser, comments, date);
        requests.add(newRequest)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                loadingAlert.dismiss();
                SweetAlertDialog successAlert = UserInterfaceHelper.showSuccessAlert(context, "SOS en camino", "Espera en tu ubicación");
                successAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                         @Override
                                                         public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                             sweetAlertDialog.dismiss();
                                                             finish();
                                                         }
                                                     });
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingAlert.dismiss();
                UserInterfaceHelper.showErrorAlert(context, "Oops", "Hubo un problema al solicitar el servicio. Intenta de nuevo.");
                Log.w(TAG, "Error writing document", e);
            }
        });
    }
}
