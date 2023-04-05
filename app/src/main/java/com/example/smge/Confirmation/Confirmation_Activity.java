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

public class Confirmation_Activity extends AppCompatActivity {

    private String time;
    private String refNumber;
    private String appliance;

    private String dateText;
    private Button Chat_with_an_Engineer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        generateReferenceNumber();

        // Display the reference number on the page
        TextView refNumberTextView = findViewById(R.id.refNumberTextView);
        refNumberTextView.setText(refNumber);
        Chat_with_an_Engineer = findViewById(R.id.ChatwithanEngineer);

    // Get the selected time and appliance from the previous activity
    Intent intent = getIntent();
    time = intent.getStringExtra("time");
    appliance = intent.getStringExtra("appliance");
    dateText = intent.getStringExtra("dateText");

    // Display the selected time and appliance on the page
    TextView timeTextView = findViewById(R.id.TimeView);
        timeTextView.setText("TimeSlot: " + time);

    TextView applianceTextView = findViewById(R.id.ApplianceView);
        applianceTextView.setText("Appliance: " +appliance);

        TextView dateTextView = findViewById(R.id.dateview);
        dateTextView.setText("Date: " + dateText);


        Chat_with_an_Engineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Confirmation_Activity.this, Chat_with_an_engineer.class));
            }
        });
}



    public void generateReferenceNumber() {
        // You can replace this with your own logic for generating the reference number
        int number = (int) (Math.random() * 10000000);
        refNumber = "Your Ref No is - REP" + String.format("%07d", number);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit this page?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Confirmation_Activity.this, Decision_Activity.class));
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
