package com.example.smge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.smge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Gas_Emergency_Jobs extends AppCompatActivity {

    private TextView gasEmergencyText;
    private FirebaseFirestore db;
    private ArrayList<String> gasEmergenciesList;
    private ArrayList<String> uidList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_emergency_jobs);

        gasEmergencyText = findViewById(R.id.gas_emergency_text);
        db = FirebaseFirestore.getInstance();
        gasEmergenciesList = new ArrayList<>();
        uidList = new ArrayList<>();

        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot userDocument : task.getResult()) {
                                // get the uid of the user document
                                String userid = userDocument.getString("uid");
                                if (!uidList.contains(userid)) {
                                    uidList.add(userid);
                                }

                                // get the gas_emergencies subcollection for this user
                                CollectionReference gasEmergenciesCollection = userDocument.getReference().collection("gas_emergencies");

                                // retrieve all the documents in the gas_emergencies subcollection
                                gasEmergenciesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> gasEmergenciesTask) {
                                        if (gasEmergenciesTask.isSuccessful()) {
                                            for (QueryDocumentSnapshot gasEmergencyDocument : gasEmergenciesTask.getResult()) {
                                                // get the fields inside the gas_emergency document
                                                String address = gasEmergencyDocument.getString("address");
                                                String mobileNo = gasEmergencyDocument.getString("mobile_no");
                                                String name = gasEmergencyDocument.getString("name");
                                                String referenceNumber = gasEmergencyDocument.getString("reference_number");
                                                String selectedItem = gasEmergencyDocument.getString("selected_item");


                                                // add the retrieved data to the gasEmergenciesList
                                                String gasEmergencyString = "Reference Number: " + referenceNumber
                                                        + "\nName: " + name + "\nAddress: " + address + "\nMobile No: " + mobileNo
                                                        + "\nSelected Item: " + selectedItem + "\nEmail: " + userid + "\n\n= = = = = = = = = = = = = = = = = = = = = = = = = = =   \n\n";

                                                gasEmergenciesList.add(gasEmergencyString);
                                            }
                                            // update the text view with the retrieved data
                                            String gasEmergencyTextString = "";
                                            for (String gasEmergency : gasEmergenciesList) {
                                                gasEmergencyTextString += gasEmergency;
                                            }
                                            gasEmergencyText.setText(gasEmergencyTextString);

                                            int totalGasEmergencies = gasEmergenciesList.size();
                                            TextView totalCountText = findViewById(R.id.total_count_text);
                                            String totalCountString = "Total Number of Gas Emergency jobs: " + totalGasEmergencies;
                                            totalCountText.setText(totalCountString);


                                        } else {
                                            Log.d("TAG",
                                                    "Error getting gas_emergencies documents: ", gasEmergenciesTask.getException());
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("TAG",
                                    "Error getting users documents: ", task.getException());
                        }
                    }
                });
    }
}