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

public class Repair_Jobs extends AppCompatActivity {

    private TextView repairText;
    private FirebaseFirestore db;
    private ArrayList<String> repairList;
    private ArrayList<String> uidList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_jobs);

        repairText = findViewById(R.id.repair_text);
        db = FirebaseFirestore.getInstance();
        repairList = new ArrayList<>();
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

                                // get the repair subcollection for this user
                                CollectionReference repairCollection = userDocument.getReference().collection("repair");

                                // retrieve all the documents in the repair subcollection
                                repairCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> repairTask) {
                                        if (repairTask.isSuccessful()) {
                                            for (QueryDocumentSnapshot repairDocument : repairTask.getResult()) {
                                                // get the fields inside the gas_emergency document
                                                String address = repairDocument.getString("address");
                                                String mobileNo = repairDocument.getString("mobile_no");
                                                String name = repairDocument.getString("name");
                                                String referenceNumber = repairDocument.getString("reference_number");
                                                String timeslot = repairDocument.getString("timeslot");
                                                String status = repairDocument.getString("status");




                                                // add the retrieved data to the repair
                                                String repairString = "Reference Number: " + referenceNumber
                                                        + "\nName: " + name + "\nAddress: " + address + "\nMobile No: " + mobileNo
                                                        + "\nTime Slot: " + timeslot + "\nStatus: " + status+ "\nEmail: " + userid + "\n\n= = = = = = = = = = = = = = = = = = = = = = = = = = =   \n\n";

                                                repairList.add(repairString);
                                            }
                                            // update the text view with the retrieved data
                                            String repairTextString = "";
                                            for (String repair : repairList) {
                                                repairTextString += repair;
                                            }
                                            repairText.setText(repairTextString);

                                            int totalrepair = repairList.size();
                                            TextView totalCountText = findViewById(R.id.total_count_text);
                                            String totalCountString = "Total Number of Repair Jobs: " + totalrepair;
                                            totalCountText.setText(totalCountString);


                                        } else {
                                            Log.d("TAG",
                                                    "Error getting gas_emergencies documents: ", repairTask.getException());
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