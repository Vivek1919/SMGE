package com.example.smge.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smge.R;

public class Main_Activity extends AppCompatActivity {
    Button bt1, bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = findViewById(R.id.LoginButton);
        bt2 = findViewById(R.id.Registerbutton);

        //This button will take user to sign in page
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(Main_Activity.this, Sign_in_Activity.class));

            }
        });

        //This button will take user to Register page
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_Activity.this, Register_Activity.class));

            }
        });
    }

    //When user click on back button, it shows this dialog box
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to close the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity(); // close all activities
                        System.exit(0); // close the app
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