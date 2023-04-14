package com.example.smge.Booking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smge.Confirmation.Service_Confirmation_Activity;
import com.example.smge.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Check_and_repair_Activity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView name, address, mobile_no, dateText;
    String[] time = {"8:00 - 12:00", "12:00 - 16:00", "16:00 - 20:00"};
    Button bt1, dateButton;
    String[] appliances = {"Boiler", "Cooker", "Hob", "Radiator", "Oven"};
    Calendar selectedDate = Calendar.getInstance();

    AutoCompleteTextView timeslot, appliance;

    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_and_repair);

        name = findViewById(R.id.Name);
        address = findViewById(R.id.Address);
        mobile_no = findViewById(R.id.Mobile_No);
        dateText = findViewById(R.id.dateText);
        dateText.setEnabled(false); //Disable to edit the text

        dateButton = findViewById(R.id.dateButton);
        bt1 = findViewById(R.id.Submitbutton);


        timeslot = findViewById(R.id.Timeslot);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, time);
        timeslot.setAdapter(adapterItems);

        dateText = findViewById(R.id.dateText);
        dateText.setEnabled(false);
        dateButton = findViewById(R.id.dateButton);
        bt1 = findViewById(R.id.Submitbutton);

        appliance = findViewById(R.id.Appliance);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, appliances);
        appliance.setAdapter(adapterItems);


        timeslot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String time = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Check_and_repair_Activity.this, "TimeSlot:" + time, Toast.LENGTH_SHORT).show();
            }
        });

        appliance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String appliances = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Check_and_repair_Activity.this, "Item:" + appliances, Toast.LENGTH_SHORT).show();
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = name.getText().toString();
                String addressStr = address.getText().toString();
                String mobileNoStr = mobile_no.getText().toString();
                String dateTextStr = dateText.getText().toString();
                String timeslotStr = timeslot.getText().toString();
                String applianceStr = appliance.getText().toString();
                String uid = auth.getCurrentUser().getUid();


                // Check if any of the text fields are empty
                if (nameStr.isEmpty() || addressStr.isEmpty() || mobileNoStr.isEmpty() || dateTextStr.isEmpty() || timeslotStr.isEmpty() || applianceStr.isEmpty()) {
                    Toast.makeText(Check_and_repair_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }



                // Create an AlertDialog to confirm the submission of the repair request
                AlertDialog.Builder builder = new AlertDialog.Builder(Check_and_repair_Activity.this);
                builder.setTitle("Confirm Submission");
                builder.setMessage("Are you sure you want to submit this repair request?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Create a ProgressDialog to show while the repair request is being submitted
                        ProgressDialog progressDialog = new ProgressDialog(Check_and_repair_Activity.this);
                        progressDialog.setTitle("Submitting Request");
                        progressDialog.setMessage("Please wait while we submit your repair request...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        // Generate a unique reference number for the customer
                        int randomNumber = (int) (Math.random() * 999) + 1; // Generate a random number between 1 and 999
                        String referenceNumber = "200" + randomNumber; // Format the random number with leading zeros and add the prefix "200"

                        // Create a new document with a generated ID
                        Map<String, Object> repair = new HashMap<>();
                        repair.put("name", nameStr);
                        repair.put("address", addressStr);
                        repair.put("mobile_no", mobileNoStr);
                        repair.put("timeslot", timeslotStr);
                        repair.put("appliance", applianceStr);
                        repair.put("dateText", dateTextStr);
                        repair.put("reference_number", referenceNumber);
                        repair.put("status", "in Progress");

                        db.collection("users").document(uid).collection("repair")
                                .add(repair)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        // Document was successfully created
                                        progressDialog.dismiss(); // Dismiss the progress dialog
                                        Toast.makeText(Check_and_repair_Activity.this, "Repair request submitted successfully!", Toast.LENGTH_SHORT).show();

                                        // Pass the selected time and appliance values to the Service_Confirmation_Activity
                                        Intent intent = new Intent(Check_and_repair_Activity.this, Service_Confirmation_Activity.class);
                                        intent.putExtra("time", timeslot.getText().toString());
                                        intent.putExtra("appliance", appliance.getText().toString());
                                        intent.putExtra("dateText", dateText.getText().toString());
                                        intent.putExtra("referenceNumber", referenceNumber);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Document creation failed
                                        progressDialog.dismiss(); // Dismiss the progress dialog
                                        Toast.makeText(Check_and_repair_Activity.this, "Error submitting service request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show date picker dialog
                new DatePickerDialog(Check_and_repair_Activity.this,
                        dateSetListener,
                        selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

        private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // create a calendar instance for tomorrow's date
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_YEAR, 1);

            // create a calendar instance for the selected date
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, day);

            // check if the selected date is valid
            if (selectedDate.before(tomorrow)) {
                Toast.makeText(Check_and_repair_Activity.this, "Please select a date after tomorrow", Toast.LENGTH_SHORT).show();
            } else {
                // format the selected date as string and set it to the dateText field
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
                dateText.setText(sdf.format(selectedDate.getTime()));
            }
        }

    };

}

