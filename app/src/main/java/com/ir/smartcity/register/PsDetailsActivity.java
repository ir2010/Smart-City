package com.ir.smartcity.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ir.smartcity.R;

  public class PsdetailsActivity extends AppCompatActivity {

        private TextInputEditText nameEditText, usernameEditText, passwordEditText, passwordEditText2, dataEditText;
        private TextInputLayout nameLayout, usernameEditTextLayout, passwordLayout, password2Layout, dataLayout;
        private Button submitButton;
        private ProgressBar progressBar;
        private String name, username, password, password2, data;
        private DatabaseReference databaseReference;
        private FirebaseUser user;
        private String uid;
        private FusedLocationProviderClient fusedLocationProviderClient;
        private Double lat,longt;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ps_details);

            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
            {
                if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location !=null)
                            {
                                lat=location.getLatitude();
                                longt=location.getLongitude();
                                Toast.makeText(PsdetailsActivity.this,"Success",Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
                else
                {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION },44);
                }
            }

            nameEditText = findViewById(R.id.etUID);
            usernameEditText = findViewById(R.id.et2UID);
            passwordEditText = findViewById(R.id.et3UID);
            passwordEditText2 = findViewById(R.id.et4UID);
            dataEditText = findViewById(R.id.et5UID);

            nameLayout = findViewById(R.id.inputUID);
            usernameEditTextLayout = findViewById(R.id.input2UID);
            passwordLayout = findViewById(R.id.input3UID);
            password2Layout = findViewById(R.id.input4UID);
            dataLayout = findViewById(R.id.input5UID);

            submitButton = findViewById(R.id.submitbutton);
            progressBar = findViewById(R.id.detailsPagePb);

            databaseReference = FirebaseDatabase.getInstance().getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
            Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = nameEditText.getText().toString();
                    username = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    password2 = passwordEditText2.getText().toString();
                    data = dataEditText.getText().toString();

                    //TODO: add username and password constraints

                    validateData();
                }
            });
        }


        private void validateData()
        {
            boolean passwordValidate = false, password2Validate = false;

            if(password2.isEmpty())
            {
                passwordEditText2.setError("Fill this detail.");
                passwordEditText2.requestFocus();
            }
//                else if(data.isEmpty())
//                {
//                    dataEditText.setError("Fill this detail.");
//                    dataEditText.requestFocus();
//                }
            else if(!password.equals(password2))
            {
                //passwordEditText.setError("Passwords don't match.");
                passwordEditText2.setError("Passwords don't match.");
                //passwordEditText.requestFocus();
                passwordEditText2.requestFocus();
            }
            else
                password2Validate = true;

            if(password.isEmpty())
            {
                passwordEditText.setError("Fill this detail.");
                passwordEditText.requestFocus();
            }
            else if(password.length() < 6)
            {
                passwordEditText.setError("Password must have atleast 6 characters");
                passwordEditText.requestFocus();
            }
            else
                passwordValidate = true;

            if(name.isEmpty())
            {
                nameEditText.setError("Fill this detail.");
                nameEditText.requestFocus();
            }

            if(username.isEmpty())
            {
                usernameEditText.setError("Fill this detail");
                usernameEditText.requestFocus();
            }
            else
                checkUsernameAndProceed(passwordValidate, password2Validate);
        }

        private void checkUsernameAndProceed(boolean passwordValidate, boolean password2Validate) {

            databaseReference.child("usernames").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //Toast.makeText(PsdetailsActivity.this, snapshot.toString() + " "+snapshot.getValue(), Toast.LENGTH_SHORT).show();

                    if(snapshot.exists())
                    {
                        usernameEditText.setError("Username already taken!");
                        usernameEditText.requestFocus();
                    }
                    else {
                        if(!name.isEmpty() && passwordValidate && password2Validate)
                        {
                            addDetailsIntoDatabase(name, username, password, data);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PsdetailsActivity.this, "Oops! Ran into some problem. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void addDetailsIntoDatabase(String name, String username, String password, String data) {

            User user = new User(name, username, data, RegisterActivity.pNo);
            Username uname = new Username(uid, password);

            databaseReference.child("users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    databaseReference.child("usernames").child(username).setValue(uname);

                    databaseReference.child("phoneNos").child(RegisterActivity.pNo).setValue(uid);

                    Toast.makeText(PsdetailsActivity.this, "Profile created successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PsdetailsActivity.this, PreferenceActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PsdetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
}