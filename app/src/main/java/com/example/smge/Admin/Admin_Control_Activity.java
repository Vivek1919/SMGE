package com.example.smge.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.smge.Customer_Details_Activity;
import com.example.smge.Gas_Emergency_Jobs;
import com.example.smge.Login.Main_Activity;
import com.example.smge.R;
import com.example.smge.Repair_Jobs;
import com.example.smge.Search_and_Status;
import com.example.smge.Service_Jobs;
import com.example.smge.Login.Sign_in_Activity;

public class Admin_Control_Activity extends AppCompatActivity {

    Button customerdetails, gasemergency, repair, service, searchandstatus, adminchat, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);


        customerdetails = findViewById(R.id.Customer_Details);
        gasemergency = findViewById(R.id.Gas_Emergency_Jobs);
        repair = findViewById(R.id.Repair_Jobs);
        service = findViewById(R.id.service_Jobs);
        searchandstatus = findViewById(R.id.Search_and_Status);
        adminchat = findViewById(R.id.Admin_chat);
        logout = findViewById(R.id.Logout);

        customerdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_Control_Activity.this, Customer_Details_Activity.class));
            }
        });

        gasemergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_Control_Activity.this, Gas_Emergency_Jobs.class));
            }
        });

        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_Control_Activity.this, Repair_Jobs.class));
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_Control_Activity.this, Service_Jobs.class));
            }
        });

        searchandstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_Control_Activity.this, Search_and_Status.class));
            }
        });

        adminchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_Control_Activity.this, AdminChat.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Control_Activity.this);
                builder.setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(Admin_Control_Activity.this, Main_Activity.class));
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


        });


    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Control_Activity.this);
        builder.setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Admin_Control_Activity.this, Sign_in_Activity.class));
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

