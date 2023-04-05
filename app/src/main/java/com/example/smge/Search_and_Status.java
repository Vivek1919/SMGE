package com.example.smge;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Search_and_Status extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText referenceNumberEditText;
    private EditText statusEditText;
    private Button searchButton, submitButton;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_status);

        referenceNumberEditText = findViewById(R.id.referenceNumberEditText);
        statusEditText = findViewById(R.id.statusEditText);
        searchButton = findViewById(R.id.searchButton);
        submitButton = findViewById(R.id.submitButton);

        db = FirebaseFirestore.getInstance();
        String userId = auth.getCurrentUser().getUid();


        //search the database using a reference_number and get the status
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String referenceNumber = referenceNumberEditText.getText().toString().trim();

                if (!referenceNumber.isEmpty()) {
                    db.collection("users")
                            .document(userId)
                            .collection("gas_emergencies")
                            .whereEqualTo("reference_number", referenceNumber)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                        String status = documentSnapshot.getString("status");
                                        statusEditText.setText(status);
                                    } else {
                                        // Search in "repair" collection if not found in "services" collection
                                        db.collection("users")
                                                .document(userId)
                                                .collection("repair")
                                                .whereEqualTo("reference_number", referenceNumber)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if (!queryDocumentSnapshots.isEmpty()) {
                                                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                                            String status = documentSnapshot.getString("status");
                                                            statusEditText.setText(status);
                                                            // Set the collection name to "repair" for updating the status
                                                            submitButton.setTag("repair");
                                                        } else {
                                                            db.collection("users")
                                                                    .document(userId)
                                                                    .collection("services")
                                                                    .whereEqualTo("reference_number", referenceNumber)
                                                                    .get()
                                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                                                                String status = documentSnapshot.getString("status");
                                                                                statusEditText.setText(status);
                                                                                // Set the collection name to "services" for updating the status
                                                                                submitButton.setTag("services");
                                                                            } else {
                                                                                statusEditText.setText("");
                                                                                Toast.makeText(Search_and_Status.this, "No data found with this reference number", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            statusEditText.setText("");
                                                                            Toast.makeText(Search_and_Status.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        statusEditText.setText("");
                                                        Toast.makeText(Search_and_Status.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    statusEditText.setText("");
                                    Toast.makeText(Search_and_Status.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    statusEditText.setText("");
                    Toast.makeText(Search_and_Status.this, "Please enter a reference number", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //submit the edited status to the database
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newStatus = statusEditText.getText().toString().trim();
                String referenceNumber = referenceNumberEditText.getText().toString().trim();

                // Search for matching reference number in gas_emergencies collection
                CollectionReference gasEmergenciesCollection = db.collection("users").document(userId).collection("gas_emergencies");
                Query gasEmergenciesQuery = gasEmergenciesCollection.whereEqualTo("reference_number", referenceNumber);
                gasEmergenciesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentReference documentReference = gasEmergenciesCollection.document(querySnapshot.getDocuments().get(0).getId());
                                updateStatus(documentReference, newStatus);
                            } else {
                                // Search for matching reference number in repair collection
                                CollectionReference repairCollection = db.collection("users").document(userId).collection("repair");
                                Query repairQuery = repairCollection.whereEqualTo("reference_number", referenceNumber);
                                repairQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot querySnapshot = task.getResult();
                                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                DocumentReference documentReference = repairCollection.document(querySnapshot.getDocuments().get(0).getId());
                                                updateStatus(documentReference, newStatus);
                                            } else {
                                                // Search for matching reference number in services collection
                                                CollectionReference servicesCollection = db.collection("users").document(userId).collection("services");
                                                Query servicesQuery = servicesCollection.whereEqualTo("reference_number", referenceNumber);
                                                servicesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            QuerySnapshot querySnapshot = task.getResult();
                                                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                                DocumentReference documentReference = servicesCollection.document(querySnapshot.getDocuments().get(0).getId());
                                                                updateStatus(documentReference, newStatus);
                                                            } else {
                                                                Toast.makeText(Search_and_Status.this, "No document found with the given reference number", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(Search_and_Status.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(Search_and_Status.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(Search_and_Status.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            // Update the status of the document and display a success message
            private void updateStatus(DocumentReference documentReference, String newStatus) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Search_and_Status.this);
                builder.setTitle("Submit Status");
                builder.setMessage("Are you sure you want to submit the updated status?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Update the status field of the document
                        documentReference.update("status", newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Search_and_Status.this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                                    statusEditText.setText("");
                                    referenceNumberEditText.setText("");
                                } else {
                                    Toast.makeText(Search_and_Status.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                    }
                });

                builder.create().show();
            }
        });
    }
}



