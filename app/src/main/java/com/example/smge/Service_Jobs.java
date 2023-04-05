package com.example.smge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Service_Jobs extends AppCompatActivity {

    private TextView serviceText;
    private FirebaseFirestore db;
    private ArrayList<String> serviceList;
    private ArrayList<String> uidList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_jobs);

        serviceText = findViewById(R.id.service_text);
        db = FirebaseFirestore.getInstance();
        serviceList = new ArrayList<>();
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

                                // get the service subcollection for this user
                                CollectionReference servicesCollection = userDocument.getReference().collection("services");

                                // retrieve all the documents in the service subcollection
                                servicesCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> serviceTask) {
                                        if (serviceTask.isSuccessful()) {
                                            for (QueryDocumentSnapshot serviceDocument : serviceTask.getResult()) {
                                                // get the fields inside the gas_emergency document
                                                String address = serviceDocument.getString("address");
                                                String mobileNo = serviceDocument.getString("mobile_no");
                                                String name = serviceDocument.getString("name");
                                                String referenceNumber = serviceDocument.getString("reference_number");
                                                String timeslot = serviceDocument.getString("timeslot");
                                                String status = serviceDocument.getString("status");




                                                // add the retrieved data to the service
                                                String serviceString = "Reference Number: " + referenceNumber
                                                        + "\nName: " + name + "\nAddress: " + address + "\nMobile No: " + mobileNo
                                                        + "\nTime Slot: " + timeslot + "\nStatus: " + status+ "\nEmail: " + userid + "\n\n= = = = = = = = = = = = = = = = = = = = = = = = = = =   \n\n";

                                                serviceList.add(serviceString);
                                            }
                                            // update the text view with the retrieved data
                                            String serviceTextString = "";
                                            for (String service : serviceList) {
                                                serviceTextString += service;
                                            }
                                            serviceText.setText(serviceTextString);

                                            int totalservice = serviceList.size();
                                            TextView totalCountText = findViewById(R.id.total_count_text);
                                            String totalCountString = "Total Number of service Jobs: " + totalservice;
                                            totalCountText.setText(totalCountString);

                                        } else {
                                            Log.d("TAG",
                                                    "Error getting gas_emergencies documents: ", serviceTask.getException());
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
