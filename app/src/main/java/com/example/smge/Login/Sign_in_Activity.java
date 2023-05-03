package com.example.smge.Login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smge.Admin.Admin_Control_Activity;
import com.example.smge.Decision_Activity;
import com.example.smge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class Sign_in_Activity extends AppCompatActivity {

    public FirebaseAuth auth;
    public FirebaseFirestore firestore;
    private TextView text2;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    Button submit;
    TextView emailText, passwordText;
    ImageButton googleSignInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        submit = findViewById(R.id.Signinbutton);
        emailText = findViewById(R.id.Email);
        passwordText = findViewById(R.id.Password);
        text2 = findViewById(R.id.txt2);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        googleSignInBtn = findViewById(R.id.google_sign_in_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google sign in button click listener
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        //Check the user details and take them to the next page
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                if (email.equals("admin@gmail.com") && password.equals("admin")) {
                    Intent intent = new Intent(Sign_in_Activity.this, Admin_Control_Activity.class);
                    Toast.makeText(Sign_in_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(Sign_in_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                // Get the user's UID and email
                                String uid = auth.getCurrentUser().getUid();
                                String userEmail = auth.getCurrentUser().getEmail();
                                // Create a new document in the "users" collection with the UID as the document ID and email field
                                User user;
                                user = new User(userEmail);
                                firestore.collection("users").document(uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(Sign_in_Activity.this, Decision_Activity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(Sign_in_Activity.this, "Failed to create user document", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Sign_in_Activity.this, "Sign in Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        passwordText.setError("Password cannot be empty");
                    }
                } else if (email.isEmpty()) {
                    emailText.setError("Email cannot be Empty");
                } else {
                    emailText.setError("Please enter valid Email");
                }
            }

        });

        //This is a text called "Register" that will take user to another activity
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TO BE REMOVED
                Intent intent = new Intent(Sign_in_Activity.this, Register_Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // Show progress bar while signing in
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss(); // Dismiss progress bar when sign-in is complete
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();
                            String userEmail = user.getEmail();
                            // Create a new document in the "users" collection with the UID as the document ID and email field
                            User newuser;
                            newuser = new User(userEmail);
                            firestore.collection("users").document(uid).set(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(Sign_in_Activity.this, Decision_Activity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Sign_in_Activity.this, "Failed to create user document", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Sign_in_Activity.this, "Sign in with Google failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign-In failed, update UI appropriately
                Toast.makeText(Sign_in_Activity.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}