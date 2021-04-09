package com.ir.smartcity.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ir.smartcity.R;
import com.ir.smartcity.home.HomeActivity;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private TextInputLayout inputLayout;
    private ProgressBar progressBar;
    private TextInputEditText otpEditText;
    private String OTPsent, phoneNo;
    private FirebaseAuth firebaseAuth;
    private boolean newUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        otpEditText = findViewById(R.id.otp);
        progressBar = findViewById(R.id.verificationPagePb);
        inputLayout = findViewById(R.id.inputOTP);
        firebaseAuth = FirebaseAuth.getInstance();

        phoneNo = getIntent().getExtras().getString("phoneNumber");
        newUser = !getIntent().getExtras().getBoolean("registered");
        RegisterActivity.progressBar.setVisibility(View.GONE);

        registerWithPhone(phoneNo);
    }

    private void registerWithPhone(String phoneNo)
    {
        //sending OTP
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNo)
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        //AIzaSyAkEzjGKGEq5MZfneijT6-nYLCRJ-JK1Nc

        inputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = otpEditText.getText().toString().trim();
                if(OTP.isEmpty() || OTP.length() < 6)
                {
                    otpEditText.setError("Fill the code correctly");
                }
                else
                    verifyOTP(OTP);
            }
        });

        otpEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i== EditorInfo.IME_ACTION_DONE){
                String OTP = otpEditText.getText().toString().trim();
                if(OTP.isEmpty() || OTP.length() < 6)
                {
                    otpEditText.setError("Fill the code correctly");
                }
                else
                    verifyOTP(OTP);
            }
            return true;
        });
    }

    private void verifyOTP(String OTPtyped)
    {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTPsent, OTPtyped);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(VerificationActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    if(newUser)
                    {
                        Intent intent = new Intent(VerificationActivity.this, PsDetailsActivity.class);
                        //on pressing back button, won't return to this activity again
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
                        //on pressing back button, won't return to this activity again
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    otpEditText.setError("Some error occurred! Check the OTP again.");
                    Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            OTPsent = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String OTPdetected =  phoneAuthCredential.getSmsCode();
            if(OTPdetected != null)
            {
                otpEditText.setText(OTPdetected);
                verifyOTP(OTPdetected);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}