

package com.ir.smartcity.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import java.text.DateFormat;
import android.text.format.*;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ir.smartcity.R;
import com.ir.smartcity.home.HomeActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAJobActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, NumberPicker.OnValueChangeListener {

    private static final int CHOOSE_LOCATION = 2;
    private Button addButton;
    private TextInputEditText jobTitleEditText, jobDetailsEditText;
    private ProgressBar progressBar;
    private ImageButton jobLocationButton;
    private TextView jobPaymentButton, jobDeadlineButton, jobPhotosButton;
    private String jobPayment = new String(), jobDeadline, jobCategory, jobTitle, jobDetails, uid;
    private Double jobLocationLat, jobLocationLon;
    private ArrayList<Uri> jobPhotoList = new ArrayList<Uri>();
    private ArrayList<String> downloadUrlList = new ArrayList<String>();
    private int day = 0, month = 0, year = 0, hour = -1, minute = -1;
    private static final int CHOOSE_FILE = 1;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private NumberPicker np;
    private ProgressDialog loadingBar;
    private DatePickerDialog datePickerDialog;
    private TextInputLayout categories;
    private AutoCompleteTextView category_details;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_job);

        jobTitleEditText = findViewById(R.id.job_title);
        jobDetailsEditText = findViewById(R.id.job_details);
        progressBar = findViewById(R.id.addJobPagePb);
        addButton = findViewById(R.id.submitbutton);
        jobLocationButton = findViewById(R.id.job_location);
        jobPaymentButton = findViewById(R.id.job_payment);
        jobDeadlineButton = findViewById(R.id.job_deadline);
        jobPhotosButton = findViewById(R.id.job_photos);
        loadingBar = new ProgressDialog(this);
        categories=(TextInputLayout)findViewById(R.id.categories);
        category_details=(AutoCompleteTextView)findViewById(R.id.categories_details);
        arrayList=new ArrayList<>();

        arrayList.add("Computer/IT");
        arrayList.add("Cooking");
        arrayList.add("Daily Help");
        arrayList.add("Driving");
        arrayList.add("Electrician");
        arrayList.add("Errand");
        arrayList.add("Manual Work");
        arrayList.add("Mechanic");
        arrayList.add("Plumbing");
        arrayList.add("Teaching");

        arrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,arrayList);
        category_details.setAdapter(arrayAdapter);
        category_details.setThreshold(1);


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = ((FirebaseUser) user).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(AddAJobActivity.this,AddAJobActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        jobPaymentButton.addTextChangedListener(new TextWatcher() {

            boolean edited = false; //to avoid recursion
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("0"))
                {
                    jobPaymentButton.setTextColor(Color.GRAY);
                    jobPaymentButton.setError(null);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean titleValidate = false, locationValidate = false, paymentValidate = false, categoryValidate = false;

                jobTitle = jobTitleEditText.getText().toString().trim();
                jobDetails = jobDetailsEditText.getText().toString().trim();
                jobCategory = category_details.getText().toString().trim();

                if(jobTitle.isEmpty())
                {
                    jobTitleEditText.setError("Fill this detail.");
                    jobTitleEditText.requestFocus();
                }
                else
                    titleValidate = true;

                if(jobCategory.isEmpty())
                {
                    category_details.setError("Fill this detail");
                    category_details.requestFocus();
                }
                else
                    categoryValidate  =true;

                if(jobPayment.equals(""))
                {
                    jobPaymentButton.setTextColor(Color.RED);
                    jobPaymentButton.setError("Fill this detail");
                }
                else
                    paymentValidate = true;

                if(jobLocationLat==null || jobLocationLon == null)
                {
                    jobLocationButton.setBackgroundColor(Color.RED);
                    //jobLocationButton.setImageDrawable(getResources().getDrawable(R.drawable.location_red));
                }
                else
                    locationValidate = true;

//                Date date;
//                if(year != 0 && month != 0 && day != 0 && hour != -1 && minute != -1)
//                {
//
//                }

                if(titleValidate && locationValidate && paymentValidate && categoryValidate) {
                    if(!jobPhotoList.isEmpty())
                        uploadPhotos();
                    else
                        addDetailsIntoDatabase();
                }
            }
        });
    }

    private void uploadPhotos() {

        loadingBar.setTitle("Adding new job..");
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

            String jobPicName = "Job Images/" + uid + "/" + saveCurrentDate + saveCurrentTime + i;

            storageReference.child(jobPicName).putFile(jobPhotoList.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        //downloadUrlList.add(task.getResult().getStorage().getDownloadUrl().toString());
                        downloadUrlList.add(jobPicName);
                        if(downloadUrlList.size() == jobPhotoList.size())
                            addDetailsIntoDatabase();
                    }
                    else
                    {
                        String message=task.getException().getMessage();
                        Toast.makeText(AddAJobActivity.this,"Error Occurred" + message,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void select_deadline(View view) {

        datePickerDialog.show();
    }

    public void select_payment(View view) {

        final Dialog dialog = new Dialog(AddAJobActivity.this);

        dialog.setContentView(R.layout.payment_dialog);
        dialog.setTitle("How much are you willing to pay the helper?");

        Button b1 = (Button) dialog.findViewById(R.id.button1);
        Button b2 = (Button) dialog.findViewById(R.id.button2);
        np = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
        np.setMaxValue(99999);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                jobPayment = String.valueOf(np.getValue());

                SpannableString underlinedString = new SpannableString(jobPayment);
                underlinedString.setSpan(new UnderlineSpan(), 0, underlinedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                jobPaymentButton.setText(underlinedString + "  ");

                dialog.dismiss();
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void select_photos(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        // intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CHOOSE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if(requestCode == CHOOSE_LOCATION && resultCode == RESULT_OK)
        {
            jobLocationLat = resultData.getExtras().getDouble("latitude");
            jobLocationLon = resultData.getExtras().getDouble("longitude");
        }

        if (requestCode == CHOOSE_FILE && resultCode == RESULT_OK) {
            // The result data contains a URI for the document or directory that the user selected.
            if (resultData != null) {
                if (resultData.getClipData() != null)
                {
                    int i;
                    for(i = 0; i < resultData.getClipData().getItemCount(); i++)
                    {
                        Uri uri = resultData.getClipData().getItemAt(i).getUri();
                        jobPhotoList.add(uri);
                    }
                }
                else {
                    jobPhotoList.add(resultData.getData());
                }
                SpannableString underlinedString = new SpannableString(String.valueOf(jobPhotoList.size()) +" items selected.");
                underlinedString.setSpan(new UnderlineSpan(), 0, underlinedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                jobPhotosButton.setText(underlinedString);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.day = dayOfMonth;
        this.month = month;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddAJobActivity.this, AddAJobActivity.this, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;

        //SpannableString underlinedString = new SpannableString(day+"-"+month+"-"+year+" "+hour+":"+minute);
        Date date = new Date();
        date.setDate(day);
        date.setMonth(month);
        date.setYear(year);
        date.setHours(hour);
        date.setMinutes(minute);

        SimpleDateFormat currentDate=new SimpleDateFormat("E, MMM dd HH:mm");
//        String saveCurrentDate= currentDate.format(date.getTime());
//
//        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        jobDeadline = currentDate.format(date.getTime());

        //jobDeadline = saveCurrentTime;

//        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
//        jobDeadline = date.toString();

//        currentDate = new SimpleDateFormat("HH:mm");
//        jobDeadline currentDate.format(date.getTime());

        SpannableString underlinedString = new SpannableString(jobDeadline);
        underlinedString.setSpan(new UnderlineSpan(), 0, underlinedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        jobDeadlineButton.setText(underlinedString);
    }



    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal)
    {
        jobPayment = String.valueOf(picker.getValue());
        //jobPayment = String.valueOf(newVal);

        //to underline the string
        SpannableString underlinedString = new SpannableString(jobPayment);
        underlinedString.setSpan(new UnderlineSpan(), 0, underlinedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        jobPaymentButton.setText(underlinedString + "  ");
    }

    private void addDetailsIntoDatabase() {

        String jobID = databaseReference.child("jobs").push().getKey();
        Job job = new Job(jobTitle, jobDeadline, jobLocationLat, jobLocationLon, jobDetails, jobPayment, jobCategory, downloadUrlList, uid, jobID);

        databaseReference.child("jobs").child(jobID).setValue(job).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //1 -- active
                databaseReference.child("jobHistory").child(uid).child("uploadedHires").child(jobID).setValue(jobTitle);
                Toast.makeText(AddAJobActivity.this, "Job added successfully!", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                startActivity(new Intent(AddAJobActivity.this, HomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.VISIBLE);

                Toast.makeText(AddAJobActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getlocation(View view)
    {
        Intent intent = new Intent(AddAJobActivity.this, AddJobMapsActivity.class);
        startActivityForResult(intent, CHOOSE_LOCATION);
    }
}