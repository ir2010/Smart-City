package com.ir.smartcity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ir.smartcity.home.HomeActivity;
import com.ir.smartcity.job.AddJobMapsActivity;
import com.ir.smartcity.register.LoginActivity;
import com.ir.smartcity.register.PreferenceActivity;
import com.ir.smartcity.register.RegisterActivity;

public class SplashScreen extends AppCompatActivity {
    Handler handle;
    ProgressBar progressBar;
    public static FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        progressBar = findViewById(R.id.pbar);
        progressBar.getProgress();
        handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(user != null)
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                else
                    startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
                finish();
            }
        },4000);
    }
}
