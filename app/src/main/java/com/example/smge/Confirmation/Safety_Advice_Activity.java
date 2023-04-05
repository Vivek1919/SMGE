package com.example.smge.Confirmation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smge.Chat.Chat_with_an_engineer;
import com.example.smge.Decision_Activity;
import com.example.smge.R;

public class Safety_Advice_Activity extends AppCompatActivity {

    private TextView referenceNumberTextView;
    private Button Chat_with_an_Engineer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_advice);


        referenceNumberTextView = findViewById(R.id.referenceNumberTextView);

        // Retrieve the reference number from the intent extras
        String referenceNumber = getIntent().getStringExtra("referenceNumber");

        // Set the reference number to the TextView
        referenceNumberTextView.setText("Please keep this Reference Number for future enquiry: " + referenceNumber);
        Chat_with_an_Engineer = findViewById(R.id.ChatwithanEngineer);
        Intent intent = getIntent();

        Chat_with_an_Engineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Safety_Advice_Activity.this, Chat_with_an_engineer.class));

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit this page?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Safety_Advice_Activity.this, Decision_Activity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}