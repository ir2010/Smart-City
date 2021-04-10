

package com.ir.smartcity.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;

import java.io.File;
import java.text.DateFormat;
import android.text.format.*;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ir.smartcity.R;
import com.ir.smartcity.home.HomeActivity;
import com.ir.smartcity.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RaiseAlarmActivity extends AppCompatActivity
{
    private Button addButton;
    private TextInputEditText jobTitleEditText, jobDetailsEditText;
    private ProgressBar progressBar;
    private TextView jobPhotosButton;
    private Double jobLocationLat, jobLocationLon;
    private String jobDeadline, jobTitle, jobDetails, uid;
    private ArrayList<Uri> jobPhotoList = new ArrayList<Uri>();
    private ArrayList<String> downloadUrlList = new ArrayList<String>();
    private int day = 0, month = 0, year = 0, hour = -1, minute = -1;
    private static final int CHOOSE_FILE = 1;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog loadingBar;
    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;
    private Double alarmRange = 0.009090090090;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_alarm);

        jobTitleEditText = findViewById(R.id.risk_title);
        jobDetailsEditText = findViewById(R.id.risk_details);
        progressBar = findViewById(R.id.addJobPagePb);
        addButton = findViewById(R.id.submitbutton);
        jobPhotosButton = findViewById(R.id.job_photos);
        loadingBar = new ProgressDialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean locationValidate = false;

                jobTitle = jobTitleEditText.getText().toString().trim();
                jobDetails = jobDetailsEditText.getText().toString().trim();

                //TODO: get location from map, input into lat and lan, and validate=true

                SimpleDateFormat currentDate=new SimpleDateFormat("E, MMM dd HH:mm");

                //TODO: get deadline
//                Calendar calFordTime = Calendar.getInstance();
//                String saveCurrentTime = currentDate.format(calFordTime.getTime());
//                jobDeadline =

                if(locationValidate) {
                    if(!jobPhotoList.isEmpty())
                        uploadPhotos();
                    else
                        addDetailsIntoDatabase();
                }
            }
        });
    }

    private void uploadPhotos() {

        loadingBar.setTitle("Raising Alarm..");
        loadingBar.setMessage("Please wait....");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        addButton.setVisibility(View.INVISIBLE);

        for (int i = 0; i < jobPhotoList.size(); i++) {
            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            String saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            String saveCurrentTime = currentTime.format(calFordTime.getTime());

            String jobPicName = saveCurrentDate + saveCurrentTime + i;

            storageReference.child("Alarm Images/" + uid + "/" + jobPicName).putFile(jobPhotoList.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        downloadUrlList.add(task.getResult().getStorage().getDownloadUrl().toString());
                        if(downloadUrlList.size() == jobPhotoList.size())
                            addDetailsIntoDatabase();
                    }
                    else
                    {
                        String message=task.getException().getMessage();
                        Toast.makeText(RaiseAlarmActivity.this,"Error Occurred" + message,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);

                    jobPhotoList.add(selectedImage);
                    SpannableString underlinedString = new SpannableString(String.valueOf(jobPhotoList.size()) +" items selected.");
                    underlinedString.setSpan(new UnderlineSpan(), 0, underlinedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    jobPhotosButton.setText(underlinedString);


//                    ImageView imageView = (ImageView) findViewById(R.id.ImageView);
//                    ContentResolver cr = getContentResolver();
//                    Bitmap bitmap;
//                    try {
//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);
//
//                        imageView.setImageBitmap(bitmap);
//                        Toast.makeText(this, selectedImage.toString(),
//                                Toast.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
//                                .show();
//                        Log.e("Camera", e.toString());
//                    }
                }
        }
    }

    private void addDetailsIntoDatabase() {

        String jobID = databaseReference.child("alarms").push().getKey();
        Alarm alarm = new Alarm(jobTitle, jobDeadline, jobLocationLat, jobLocationLon, jobDetails, downloadUrlList, uid, jobID);

        databaseReference.child("alarms").child(jobID).setValue(alarm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //1 -- active
                databaseReference.child("alarmHistory").child(uid).child("uploadedAlarms").child(jobID).setValue(jobTitle);


                //find people
                databaseReference.child("users").orderByChild("latitude").endAt(alarmRange).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue(User.class).getLongitude() <= alarmRange)
                        {
                            databaseReference.child("alarmHistory").child(snapshot.getKey()).child("receivedAlarms").child(jobID).setValue(jobTitle).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RaiseAlarmActivity.this, "Alarm raised!", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    startActivity(new Intent(RaiseAlarmActivity.this, HomeActivity.class));
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.VISIBLE);

                Toast.makeText(RaiseAlarmActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void open_camera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        // intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
    }
}