package com.ir.smartcity.job;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.ir.smartcity.R;
import com.ir.smartcity.home.HomeActivity;
import com.ir.smartcity.user.User;

public class JobDetailsActivity extends AppCompatActivity {

    private Job job;
    private TextView jobTitle, jobPayment, jobLocation, jobDetails, jobDeadline, hirerName, hirerPhone;
    private User jobHirer;
    private Button applyButton;
    private String hirerId, jobID, currentUserID;
    private DatabaseReference databaseReference;
    private ImageView hirerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_job_details);

        Gson gson = new Gson();
        job = gson.fromJson(getIntent().getStringExtra("job"), Job.class);
        jobID = getIntent().getStringExtra("jobID");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        jobTitle = findViewById(R.id.job_title);
        jobPayment = findViewById(R.id.job_payment);
        jobDeadline = findViewById(R.id.job_deadline);
        jobDetails = findViewById(R.id.job_details);
        jobLocation = findViewById(R.id.job_location);
        applyButton = findViewById(R.id.button_apply);
        hirerName = findViewById(R.id.hirer_name);
        hirerPhone = findViewById(R.id.hirer_phone);
        hirerImage = findViewById(R.id.hirer_image);


        jobTitle.setText(job.getJobName());
        jobPayment.setText(job.getJobPayment());
        jobDeadline.setText(job.getJobDeadline());
        jobDetails.setText(job.getJobDetails());
        jobLocation.setText(job.getJobLocation());
        hirerId = job.getHirerID();

        databaseReference.child("users").child(hirerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobHirer = snapshot.getValue(User.class);
                hirerName.setText(jobHirer.getName());
                hirerPhone.setText(jobHirer.getPhoneNumber());
                //TODO: Image of hirer
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JobDetailsActivity.this, "Couldn't access hirer details", Toast.LENGTH_SHORT).show();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("jobs").child(jobID).child("applications").child(currentUserID).setValue(HomeActivity.userDetails.getName());
                databaseReference.child("jobHistory").child(currentUserID).child("sentHelps").child(jobID).setValue(jobTitle);
                applyButton.setText("Application Sent. Wait for approval.");
                applyButton.setEnabled(false);
            }
        });

    }
}