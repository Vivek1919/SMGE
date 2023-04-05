package com.example.smge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Bundle;

public class Status_Activity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference gasEmergenciesRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("gas_emergencies");
    private CollectionReference repairRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("repair");
    private CollectionReference serviceRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("services");

    private TextView gasEmergenciesTextView;
    private TextView repairTextView;
    private TextView serviceTextView;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        gasEmergenciesTextView = findViewById(R.id.gas_emergencies_text_view);
        repairTextView = findViewById(R.id.repair_text_view);
        serviceTextView = findViewById(R.id.service_text_view);
        refreshButton = findViewById(R.id.refresh_button);

        // Get all the appointments for gas emergencies
        gasEmergenciesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String gasEmergenciesText = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String name = documentSnapshot.getString("name");
                    String referenceNumber = documentSnapshot.getString("reference_number");
                    String status = documentSnapshot.getString("status");

                    gasEmergenciesText += "Customer name: " + name + "\nReference Number: " + referenceNumber + "\nStatus: " + status + "\n\n";
                }
                gasEmergenciesTextView.setText(gasEmergenciesText);
            }

        });

        // Get all the appointments for repairs
        repairRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String repairText = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String name = documentSnapshot.getString("name");
                    String referenceNumber = documentSnapshot.getString("reference_number");
                    String status = documentSnapshot.getString("status");

                    repairText += "Customer name: " + name + "\nReference Number: " + referenceNumber + "\nStatus: " + status + "\n\n";
                }
                repairTextView.setText(repairText);
            }
        });

        // Get all the appointments for services
        serviceRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String serviceText = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String name = documentSnapshot.getString("name");
                    String referenceNumber = documentSnapshot.getString("reference_number");
                    String status = documentSnapshot.getString("status");

                    serviceText += "Customer name: " + name + "\nReference Number: " + referenceNumber + "\nStatus: " + status + "\n\n";
                }
                serviceTextView.setText(serviceText);
            }
        });


        Button refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the methods to retrieve the appointments for each type of service
                retrieveGasEmergencies();
                retrieveRepairs();
                retrieveServices();
            }
            private void retrieveGasEmergencies() {
                gasEmergenciesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String gasEmergenciesText = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String name = documentSnapshot.getString("name");
                            String referenceNumber = documentSnapshot.getString("reference_number");
                            String status = documentSnapshot.getString("status");

                            gasEmergenciesText += "Customer name: " + name + "\nReference Number: " + referenceNumber + "\nStatus: " + status + "\n\n";
                        }
                        gasEmergenciesTextView.setText(gasEmergenciesText);
                    }

                });

            }

            private void retrieveRepairs() {

                repairRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String repairText = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String name = documentSnapshot.getString("name");
                            String referenceNumber = documentSnapshot.getString("reference_number");
                            String status = documentSnapshot.getString("status");

                            repairText += "Customer name: " + name + "\nReference Number: " + referenceNumber + "\nStatus: " + status + "\n\n";
                        }
                        repairTextView.setText(repairText);
                    }
                });
            }

            private void retrieveServices() {
                serviceRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String serviceText = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String name = documentSnapshot.getString("name");
                            String referenceNumber = documentSnapshot.getString("reference_number");
                            String status = documentSnapshot.getString("status");

                            serviceText += "Customer name: " + name + "\nReference Number: " + referenceNumber + "\nStatus: " + status + "\n\n";
                        }
                        serviceTextView.setText(serviceText);
                    }
                });
            }

        });
    }
}
