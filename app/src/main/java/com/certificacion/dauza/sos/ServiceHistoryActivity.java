package com.certificacion.dauza.sos;

/**
 * Created by Ignacio on 12/18/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.certificacion.dauza.sos.Helpers.RequestsHelper;
import com.certificacion.dauza.sos.Models.EmergencyServiceRequest;


public class ServiceHistoryActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView = (RecyclerView) findViewById(R.id.requestsRecyclerView);
    private RequestsHelper requestsHelper;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_request);

        requestsRecyclerView.setHasFixedSize(true);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestsHelper = new RequestsHelper(this);
        requestsRecyclerView.setAdapter(requestsHelper);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        context = this;
    }

    private void loadData() {

        db.collection("requests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            requestsHelper.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                String typeOfService = document.getString("typeOfService");
                                String date = document.getString("date");

                                EmergencyServiceRequest c = new EmergencyServiceRequest();

                                requestsHelper.addEmergencyServiceRequest(c);

                            }

                        } else {
                            // manejar error
                        }
                    }
                });

    }


}
