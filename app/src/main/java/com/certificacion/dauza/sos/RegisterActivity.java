package com.certificacion.dauza.sos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText allergiesEditText;
    private EditText bloodGroupEditText;
    private Button emergencyContactsButton;
    private Button registerButton;

    //private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//variables para el login
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
//variables de la ficha médica
        firstNameEditText  = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText  = (EditText) findViewById(R.id.lastNameEditText);
        heightEditText  = (EditText) findViewById(R.id.heightEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        allergiesEditText = (EditText) findViewById(R.id.allergiesEditText);
        bloodGroupEditText = (EditText) findViewById(R.id.bloodGroupEditText);
        emergencyContactsButton = (Button) findViewById(R.id.emergencyContactsButton);
        emergencyContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implementar goEmergencyContacts();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);


        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void register() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            goLogIn();
                        } else {
                            showErrorMessage(task.getException().toString());
                        }
                    }
                });
    }

    private void showErrorMessage(String s) {
        Toast.makeText(getApplicationContext(), "Se detectó un error: " + s, Toast.LENGTH_LONG).show();
    }

    private void goLogIn() {
        finish();
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
