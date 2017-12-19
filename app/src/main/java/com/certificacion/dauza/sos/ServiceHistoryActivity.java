package com.certificacion.dauza.sos;

/**
 * Created by Ignacio on 12/18/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.certificacion.dauza.sos.Helpers.RequestsHelper;
import com.certificacion.dauza.sos.Models.EmergencyServiceRequest;


public class ServiceHistoryActivity extends AppCompatActivity {
    
    private RecyclerView requestsRecyclerView;
    private RequestsHelper requestsHelper;

    //requestsRecyclerView = (RecyclerView) findViewById(R.id.requestsRecyclerView);
   //requestsRecyclerView.setHasFixedSize(true);
   //requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    //requestsHelper = new RequestsHelper(this);
    //requestsRecyclerView.setAdapter(requestsHelper);
    //db = FirebaseFirestore.getInstance();


    private void loadData() {
/*
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
*/
    }


}
