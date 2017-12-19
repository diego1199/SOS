package com.certificacion.dauza.sos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.certificacion.dauza.sos.Helpers.DataHelper;
import com.certificacion.dauza.sos.Helpers.UserInterfaceHelper;
import com.certificacion.dauza.sos.Models.MedicalRecord;
import com.certificacion.dauza.sos.Models.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.certificacion.dauza.sos.Helpers.Constant.MEDICAL_RECORDS_COLLECTION_NAME;
import static com.certificacion.dauza.sos.Helpers.Constant.MEDICAL_RECORD_ID_SP_KEY;
import static com.certificacion.dauza.sos.Helpers.Constant.USER_ALREADY_CREATED_IE_KEY;
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

    private Date birthdate;

    private Context context;

    public boolean userWasCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        repeatPasswordEditText = (EditText) findViewById(R.id.repeatPasswordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);

        Intent intent = getIntent();
        if (intent.hasExtra(USER_ALREADY_CREATED_IE_KEY)) {
            userWasCreated = true;
            disableUserEdit();
            emailEditText.setText(firebaseAuth.getCurrentUser().getEmail());
            passwordEditText.setText("•••••••••");
            repeatPasswordEditText.setText("•••••••••");
        }
        else {
            userWasCreated = false;
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userWasCreated) {
                    saveNewUserMedicalRecord(firebaseAuth.getCurrentUser().getUid());
                }
                else {
                    register();
                }
            }
        });

        firstNameEditText  = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText  = (EditText) findViewById(R.id.lastNameEditText);
        birthdateEditText = findViewById(R.id.birthDateEditText);
        heightEditText  = (EditText) findViewById(R.id.heightEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        allergiesEditText = (EditText) findViewById(R.id.allergiesEditText);
        bloodGroupSpinner = (Spinner) findViewById(R.id.bloodGroupSpinner);

    }

    private void register() {

        if (!validations()) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        final SweetAlertDialog loadingAlert = UserInterfaceHelper.showLoadingAlert(this);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingAlert.dismiss();
                        if (task.isSuccessful()) {
                            userWasCreated = true;
                            registerButton.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { saveNewUserMedicalRecord(firebaseAuth.getCurrentUser().getUid());}});
                            disableUserEdit();
                            saveNewUserMedicalRecord(firebaseAuth.getCurrentUser().getUid());
                        } else {
                            UserInterfaceHelper.showErrorAlert(context, "Oops", "No se pudo registrar el usuario. Intente de nuevo.");
                        }
                    }
                });
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void saveNewUserMedicalRecord(String userId) {

        if (!validations()) {
            return;
        }

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        double height = Double.parseDouble(heightEditText.getText().toString());
        double weight = Double.parseDouble(weightEditText.getText().toString());
        String allergies = allergiesEditText.getText().toString();
        String bloodType = bloodGroupSpinner.getSelectedItem().toString();
        boolean authCompleted = true;

        final SweetAlertDialog loadingAlert = UserInterfaceHelper.showLoadingAlert(this);

        CollectionReference medicalRecords = db.collection(MEDICAL_RECORDS_COLLECTION_NAME);
        MedicalRecord newMedicalRecord = new MedicalRecord(firstName, lastName, birthdate, weight, height, allergies, bloodType, userId, authCompleted);
        medicalRecords.add(newMedicalRecord)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //TODO remove preferences on log out
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(MEDICAL_RECORD_ID_SP_KEY, documentReference.getId());
                editor.apply();
                loadingAlert.dismiss();
                goToMainScreen();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingAlert.dismiss();
                UserInterfaceHelper.showErrorAlert(context, "Oops", "No se pudo registrar el usuario. Intente de nuevo.");
                Log.w(TAG, "Error writing document", e);
            }
        });

    }

    private Date getBirthDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        try {
            return dateFormat.parse(birthdateEditText.getText().toString());
        } catch (ParseException e) {
            birthdateEditText.requestFocus();
            birthdateEditText.setError("Fecha invalida. La fecha debe estar en el formato dd/MM/yyyy.");
            return null;
        }

    }

    private void disableUserEdit() {
        emailEditText.setEnabled(false);
        emailEditText.setInputType(InputType.TYPE_NULL);

        passwordEditText.setEnabled(false);
        passwordEditText.setInputType(InputType.TYPE_NULL);

        repeatPasswordEditText.setEnabled(false);
        repeatPasswordEditText.setInputType(InputType.TYPE_NULL);
    }

    private boolean validations() {

        //Validations from bottom to top. Because we need to focus the first text edit error before any other errors.

        boolean isValid = true;

        if (bloodGroupSpinner.getSelectedItemPosition() == 0) {
            isValid = false;
            UserInterfaceHelper.showErrorAlert(context, "Oops", "Debes seleccionar tu grupo sanguíneo");
        }

        Validation allergiesValidation = DataHelper.isNotEmpty("Alergias", allergiesEditText);
        isValid = isValid && allergiesValidation.valid;

        Validation weightValidation = DataHelper.isNotEmpty("Peso", weightEditText);
        isValid = isValid && weightValidation.valid;

        Validation heightValidation = DataHelper.isNotEmpty("Altura", heightEditText);
        isValid = isValid && heightValidation.valid;

        // Getting birthdate here because using Date is expensive to do it twice.
        birthdate = getBirthDate();
        if (birthdate == null) {
            isValid = false;
        }

        Validation lastNameValidation = DataHelper.onlyHasLetters("Apellido(s)", lastNameEditText);
        isValid = isValid && lastNameValidation.valid;

        Validation firstNameValidation = DataHelper.onlyHasLetters("Nombre(s)", firstNameEditText);
        isValid = isValid && firstNameValidation.valid;

        if (!userWasCreated) {
            Validation samePasswordRepeatValidation = DataHelper.textMatches("Contraseña", passwordEditText, "Confirmar contraseña", repeatPasswordEditText);
            isValid = isValid && samePasswordRepeatValidation.valid;

            Validation passwordMinLengthValidation = DataHelper.passwordMinLength(passwordEditText);
            isValid = isValid && passwordMinLengthValidation.valid;

            Validation emailValidation = DataHelper.isEmail(emailEditText);
            isValid = isValid && emailValidation.valid;
        }

        return isValid;
    }

}
