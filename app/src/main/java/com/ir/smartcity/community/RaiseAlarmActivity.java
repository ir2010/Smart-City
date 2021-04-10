package com.ir.smartcity.community;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ir.smartcity.R;

public class RaiseAlarmActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Double dlat,dlongt;
    //these lat & long from firebase
    private Double lat, longt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_alarm);
    }

    public void getlocation(View view)
    {
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
                            dlat=location.getLatitude();
                            dlongt=location.getLongitude();
                            Toast.makeText(RaiseAlarmActivity.this,"Success",Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION },44);
            }
        }
        float results[]=new float[19];
        Location.distanceBetween(lat,longt,dlat,dlongt,results);
    }
}