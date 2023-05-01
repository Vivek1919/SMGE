package com.example.smge.Booking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smge.Confirmation.Safety_Advice_Activity;
import com.example.smge.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class Gas_Emergency_Activity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView name, address, mobile_no;
    RadioButton internal, external, n_a;

    public AutoCompleteTextView getAutoCompleteTextView() {
        return autoCompleteTextView;
    }

    String[] item ={"CO Alarm", "Exposed Pipework", "Rusty Pipework", "No Gas", "Damage Pipework"};
    Button bt1;
    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_emergency);

        name = findViewById(R.id.Name);
        address = findViewById(R.id.Address);
        mobile_no = findViewById(R.id.Mobile_No);
        internal = findViewById(R.id.Internal);
        external = findViewById(R.id.External);
        n_a = findViewById(R.id.N_A);


        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        bt1 = findViewById(R.id.Submitbutton);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Gas_Emergency_Activity.this, "Item:" + item, Toast.LENGTH_SHORT).show();
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = name.getText().toString();
                String addressStr = address.getText().toString();
                String mobileNoStr = mobile_no.getText().toString();
                String selectedItemStr = autoCompleteTextView.getText().toString();
                boolean radioButton1Checked = internal.isChecked();
                boolean radioButton2Checked = external.isChecked();
                boolean radioButton3Checked = n_a.isChecked();
                String uid = auth.getCurrentUser().getUid();

                // Check if any of the text fields are empty
                if (nameStr.isEmpty()) {
                    name.setError("Please enter your name");
                    return;
                }
                if (addressStr.isEmpty()) {
                    address.setError("Please enter your address");
                    return;
                }
                if (mobileNoStr.isEmpty()) {
                    mobile_no.setError("Please enter your mobile number");
                    return;
                }
                if (selectedItemStr.isEmpty()) {
                    autoCompleteTextView.setError("Please select an item from the list");
                    return;
                }

                // Check if the user has selected a radio button
                if (!radioButton1Checked && !radioButton2Checked && !radioButton3Checked) {
                    n_a.setError("Please select one of the options");
                    return;
                }

                // Generate a unique reference number for the customer
                int randomNumber = (int) (Math.random() * 999) + 1; // Generate a random number between 1 and 999
                String referenceNumber = "100" + randomNumber; // Format the random number with leading zeros and add the prefix "100"

                // Create the AlertDialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(Gas_Emergency_Activity.this);
                builder.setTitle("Confirm submission");
                builder.setMessage("Are you sure you want to submit these details?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressDialog progressDialog = new ProgressDialog(Gas_Emergency_Activity.this);
                        progressDialog.setTitle("Submitting Details");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        // Store the customer's data and the reference number in Firestore
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", nameStr);
                        data.put("address", addressStr);
                        data.put("mobile_no", mobileNoStr);
                        data.put("selected_item", selectedItemStr);
                        data.put("internal_radio", radioButton1Checked);
                        data.put("external_radio", radioButton2Checked);
                        data.put("n/a_radio", radioButton3Checked);
                        data.put("reference_number", referenceNumber);
                        data.put("status", "in Progress");

                        db.collection("users").document(uid).collection("gas_emergencies")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Gas_Emergency_Activity.this, "Details saved", Toast.LENGTH_SHORT).show();

                                        // Start the new activity and pass the reference number as an extra
                                        Intent intent = new Intent(Gas_Emergency_Activity.this, Safety_Advice_Activity.class);
                                        intent.putExtra("referenceNumber", referenceNumber);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Gas_Emergency_Activity.this, "Error storing details", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", null);

                // Show the AlertDialog box
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

}