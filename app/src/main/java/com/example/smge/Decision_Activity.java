package com.example.smge;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smge.Booking.Check_and_repair_Activity;
import com.example.smge.Booking.Gas_Emergency_Activity;
import com.example.smge.Booking.Service_Activity;
import com.example.smge.Chat.Chat_with_an_engineer;
import com.example.smge.Login.Main_Activity;

public class Decision_Activity extends AppCompatActivity {
    ImageButton bt1, bt2, bt3, bt4;
    Button bt5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_decision);
        bt1 = findViewById(R.id.imgbtn1);
        bt2 = findViewById(R.id.imgbtn2);
        bt3 = findViewById(R.id.imgbtn3);
        bt4 = findViewById(R.id.imgbtn4);
        bt5 = findViewById(R.id.imgbtn5);


        //Takes user to Gas Emergency Page
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Decision_Activity.this, Gas_Emergency_Activity.class));
            }
        });

        //Takes user to Check and Repair Page
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Decision_Activity.this, Check_and_repair_Activity.class));
            }
        });

        //Takes user to Service Page
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Decision_Activity.this, Service_Activity.class));
            }
        });

        //Takes user to Status Page
        bt4.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                startActivity(new Intent(Decision_Activity.this, Status_Activity.class));
            }
        });

        //Takes user to Chat with an Engineer Page
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Decision_Activity.this, Chat_with_an_engineer.class));
            }
        });
    }


    //Show the dialog message when user press back button
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to logout")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Decision_Activity.this, Main_Activity.class));
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
