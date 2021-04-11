package com.ir.smartcity.register;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ir.smartcity.R;
import com.ir.smartcity.home.HomeActivity;
import com.ir.smartcity.job.JobAdapter;

import java.util.ArrayList;

public class PreferenceActivity extends AppCompatActivity {

    private Button teaching, electrician, plumber, dailyHelp, driving, manualWork, IT, cooking, mechanic, errand;
    private ArrayList<String> preferences = new ArrayList<String>();
    private DatabaseReference databaseReference;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        teaching = findViewById(R.id.teach1);
        electrician = findViewById(R.id.electric2);
        plumber = findViewById(R.id.plubber3);
        dailyHelp = findViewById(R.id.dailyhelp4);
        driving = findViewById(R.id.drive5);
        manualWork = findViewById(R.id.manual6);
        IT = findViewById(R.id.comp7);
        cooking = findViewById(R.id.cook8);
        mechanic = findViewById(R.id.mechanic9);
        errand = findViewById(R.id.errand10);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    public void select(View view)
    {
        view.setEnabled(false);
        preferences.add((String) view.getTag());
        Toast.makeText(this, view.getTag()+"selected", Toast.LENGTH_SHORT).show();
    }

    public void next(View view)
    {
        databaseReference.child("users/"+uid+"/preferences").setValue(preferences).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PreferenceActivity.this, "Preferences added successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PreferenceActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    public void skipnow(View view)
    {
        startActivity(new Intent(PreferenceActivity.this, HomeActivity.class));
        finish();
    }

}
