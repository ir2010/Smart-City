package com.ir.smartcity.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ir.smartcity.R;
import com.ir.smartcity.home.HomeActivity;
import com.ir.smartcity.job.JobDetailsActivity;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;


import com.google.android.material.textfield.TextInputEditText;
import com.ir.smartcity.R;

public class LoginActivity extends AppCompatActivity {
    String mongoAppID = "smartcity-tiwjb";

    private TextInputEditText userName, password;
    private ProgressBar progressBar;
    private Button loginButton;
    private TextView registerButton, guestButton;
    private DatabaseReference databaseReference;
    public static boolean isGuest = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.uname);
        password = findViewById(R.id.pwd);
        progressBar = findViewById(R.id.loginPagePb);
        loginButton = findViewById(R.id.submitbutton);
        registerButton = findViewById(R.id.signup);
        guestButton = findViewById(R.id.guest);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        //Initiate MongoDB Realm for this app
        Realm.init(this);
        App mongoApp = new App(new AppConfiguration.Builder(mongoAppID).build());


        userName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i== EditorInfo.IME_ACTION_DONE){
                if (userName.getText().toString().isEmpty()) {
                    userName.setError("Fill the username");
                    userName.requestFocus();
                    //Toast.makeText(LoginActivity.this, "Fill the Details", Toast.LENGTH_LONG).show();
                }
            }
            return true;
        });

        password.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i== EditorInfo.IME_ACTION_DONE){
                if (password.getText().toString().isEmpty()) {
                    password.setError("Fill the password");
                    password.requestFocus();
                    //Toast.makeText(LoginActivity.this, "Fill the Details", Toast.LENGTH_LONG).show();
                }
            }
            return true;
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userName.getText().toString().isEmpty())
                {
                    userName.setError("Fill this field");
                    userName.requestFocus();
                }
                else if (password.getText().toString().isEmpty())
                {
                    password.setError("Fill this field");
                    password.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    // loginButton.setClickable(false);

                    signIn(userName.getText().toString().trim(), password.getText().toString().trim());
                }
            }

            private void signIn(String username, String pwd) {
                databaseReference.child("usernames").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            userName.setError("Username doesn't exist");
                            userName.requestFocus();
                            progressBar.setVisibility(View.GONE);
                        }
                        else
                        {
                            String actualPassword = snapshot.child("password").getValue(String.class);
                            if(pwd.equals(actualPassword))
                            {
                                Toast.makeText(LoginActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();

                                //Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                //on pressing back button, won't return to this activity again
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                userName.setError("Invalid Credentials");
                                password.setError("Invalid Credentials");
                                progressBar.setVisibility(View.GONE);
                                password.requestFocus();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Oops! Ran into some problem. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void register(View view) {
        registerButton.setTextColor(Color.RED);
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

    }
    public void continueAsGuest(View view) {
        guestButton.setTextColor(Color.RED);
        isGuest = true;
        startActivity(new Intent(LoginActivity.this, JobDetailsActivity.class));
    }


    public void resetPassword(View view)
    {
    }
}