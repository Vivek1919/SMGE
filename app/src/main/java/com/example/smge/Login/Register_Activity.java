package com.example.smge.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smge.R;
import com.example.smge.Object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register_Activity extends AppCompatActivity {
    private EditText name, dateOfBirth, mobileNo, email, confirmEmail, password, confirmPassword;
    private Button registerButton;

    private TextView text2;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.Name);
        dateOfBirth = findViewById(R.id.Date_of_Birth);
        mobileNo = findViewById(R.id.Mobile_No);
        email = findViewById(R.id.Email);
        confirmEmail = findViewById(R.id.Confirm_Email);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.Confirm_Password);
        registerButton = findViewById(R.id.Registerbutton);
        text2 = findViewById(R.id.txt2);


        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String nameText = name.getText().toString().trim();
                String dobText = dateOfBirth.getText().toString().trim();
                String mobileNoText = mobileNo.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String confirmEmailText = confirmEmail.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String confirmPasswordText = confirmPassword.getText().toString().trim();

                if (nameText.isEmpty() || dobText.isEmpty() || mobileNoText.isEmpty() ||
                        emailText.isEmpty() || confirmEmailText.isEmpty() || passwordText.isEmpty() ||
                        confirmPasswordText.isEmpty()) {
                    name.setError("Please enter your name");
                    dateOfBirth.setError("Please enter your date of birth");
                    mobileNo.setError("Please enter your mobile number");
                    email.setError("Please enter your email address");
                    confirmEmail.setError("Please confirm your email address");
                    password.setError("Please enter your password");
                    confirmPassword.setError("Please confirm your password");
                    return;
                }

                if (!nameText.matches("[a-zA-Z ]+")) {
                    name.setError("Name should contain only letters and spaces");
                    return;
                }
                if (!mobileNoText.matches("0[0-9]{10}")) {
                    mobileNo.setError("Please enter a valid 10 digit mobile number starting with 0");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    email.setError("Please enter a valid email address");
                    return;
                }

                if (!emailText.equals(confirmEmailText)) {
                    confirmEmail.setError("Email addresses do not match");
                    return;
                }
                if (!passwordText.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
                    password.setError("Password should contain at least 8 characters including at least one uppercase letter, one lowercase letter and one number");
                    return;
                }


                if (!passwordText.equals(confirmPasswordText)) {
                    confirmPassword.setError("Passwords do not match");
                    return;
                }

                else {
                    auth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult>task) {
                            if (task.isSuccessful()){

                                // Get the current user's UID
                                String uid = auth.getCurrentUser().getUid();

                                // Create a new User object with the user's data
                                User user = new User(nameText, dobText, mobileNoText, emailText);

                                // Save the User object to the "users" collection in Firestore
                                FirebaseFirestore.getInstance()
                                        .collection("users2")
                                        .document(uid)
                                        .set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register_Activity.this, "User data saved successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Register_Activity.this, "User data not saved: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                Toast.makeText(Register_Activity.this, "Register Successful ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register_Activity.this, Sign_in_Activity.class));

                            }
                            else {
                                Toast.makeText(Register_Activity.this, "Register Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register_Activity.this , Sign_in_Activity.class));
                finish();
            }
        });
    }
}
