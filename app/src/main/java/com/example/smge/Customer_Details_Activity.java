package com.example.smge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smge.Object.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;

public class Customer_Details_Activity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, mobileEditText, dobEditText;
    private Button retrieveButton, saveButton;
    private FirebaseFirestore db;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        // initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        dobEditText = findViewById(R.id.dobEditText);
        retrieveButton = findViewById(R.id.retrieveButton);
        saveButton = findViewById(R.id.saveButton);




        // set onClickListener for retrieve button
        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveCustomerDetails();
            }
        });

        // set onClickListener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveConfirmationDialog();
            }
        });
    }

    // function to retrieve customer details from Firestore database
    private void retrieveCustomerDetails() {
        String email = emailEditText.getText().toString();
        db.collection("users2")
                .whereEqualTo("emailNo", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            if (documents.size() > 0) {
                                // display customer details in EditText fields
                                DocumentSnapshot document = documents.get(0);
                                nameEditText.setText(document.getString("name"));
                                mobileEditText.setText(document.getString("mobileNo"));
                                dobEditText.setText(document.getString("dateOfBirth"));
                            } else {
                                Toast.makeText(Customer_Details_Activity.this,
                                        "Customer not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Customer_Details_Activity.this,
                                    "Error retrieving customer details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showSaveConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Details_Activity.this);
        builder.setTitle("Save Customer Details");
        builder.setMessage("Are you sure you want to save the details?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveCustomerDetails();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
    // function to save customer details to Firestore database
    private void saveCustomerDetails() {
        // Get input values from EditText fields
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();

        db = FirebaseFirestore.getInstance();
        String emailId = auth.getCurrentUser().getUid();

        // Check if all fields are filled
        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || dob.isEmpty()) {
            Toast.makeText(Customer_Details_Activity.this,
                    "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new customer object with the input values
        Customer customer = new Customer(name, email, mobile, dob);

        // Save the customer object to Firestore database
        db.collection("users2")
                .document(emailId)
                .set(customer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Customer_Details_Activity.this,
                                    "Customer details saved successfully", Toast.LENGTH_SHORT).show();
                            nameEditText.setText("");
                            emailEditText.setText("");
                            mobileEditText.setText("");
                            dobEditText.setText("");


                        } else {
                            Toast.makeText(Customer_Details_Activity.this,
                                    "Error saving customer details", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}

