package com.ir.smartcity.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.ir.smartcity.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout inputLayout;
    public static ProgressBar progressBar;
    private TextInputEditText phoneNumber;
    public static String pNo;
    private CountryCodePicker country_picker;
    private String country_code;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneNumber = findViewById(R.id.phno);
        progressBar = findViewById(R.id.registerPagePb);
        inputLayout = findViewById(R.id.inputPNo);
        country_picker = findViewById(R.id.country_picker);
        country_code = country_picker.getSelectedCountryCode().trim();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if (phoneNumber.getText().toString().isEmpty()) {
                        phoneNumber.setError("Fill this field");
                        phoneNumber.requestFocus();
                        //Toast.makeText(VerificationActivity.this, "Fill the details", Toast.LENGTH_LONG).show();
                    }
                    else if(phoneNumber.getText().toString().length() != 10) {
                        phoneNumber.setError("Phone number should have 10 digits.");
                        phoneNumber.requestFocus();
                        //Toast.makeText(VerificationActivity.this, "Fill the details", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        phoneNumber.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i== EditorInfo.IME_ACTION_DONE){
                if (phoneNumber.getText().toString().isEmpty()) {
                    phoneNumber.setError("Fill this field");
                    phoneNumber.requestFocus();
                    //Toast.makeText(VerificationActivity.this, "Fill the details", Toast.LENGTH_LONG).show();
                }
                else if(phoneNumber.getText().toString().length() != 10) {
                    phoneNumber.setError("Phone number should have 10 digits.");
                    phoneNumber.requestFocus();
                    //Toast.makeText(VerificationActivity.this, "Fill the details", Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    registerWithPhone(phoneNumber.getText().toString().trim());
                }
            }
            return true;
        });

        inputLayout.setEndIconOnClickListener(view -> {
            if (phoneNumber.getText().toString().isEmpty()) {
                phoneNumber.setError("Fill this field");
                phoneNumber.requestFocus();
                //Toast.makeText(VerificationActivity.this, "Fill the details", Toast.LENGTH_LONG).show();
            }
            else if(phoneNumber.getText().toString().length() != 10) {
                phoneNumber.setError("Phone number should have 10 digits.");
                phoneNumber.requestFocus();
                //Toast.makeText(VerificationActivity.this, "Fill the details", Toast.LENGTH_LONG).show();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                registerWithPhone(phoneNumber.getText().toString().trim());
            }
        });
    }

    private void registerWithPhone(String phoneNo) {
        //String code = "+91";

        pNo = "+" + country_code + phoneNo;

        databaseReference.child("phoneNos").child(pNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) //already registered
                {
                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                    intent.putExtra("phoneNumber", pNo);
                    intent.putExtra("registered", true);
                    startActivity(intent);

//                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
//                    alertDialog.setTitle("How do you want to login?");
//                    alertDialog.setMessage("This phone number is already registered.");
//
//                    alertDialog.setIcon(R.drawable.ic_baseline_login_24);
//
//                    alertDialog.setPositiveButton("Login with OTP",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
//                                    intent.putExtra("phoneNumber", pNo);
//                                    intent.putExtra("registered", true);
//                                    startActivity(intent);
//                                }
//                            });
//
//                    alertDialog.setNeutralButton("Login with username and password", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                            finish();
//                        }
//                    });
//
//                    alertDialog.setNegativeButton("Try with different phone number",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    alertDialog.show();
                }
                else
                {
                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                    intent.putExtra("phoneNumber", pNo);
                    intent.putExtra("registered", false);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Oops! Ran into some problem. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}