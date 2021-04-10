package com.ir.smartcity.community;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.ir.smartcity.R;
import com.ir.smartcity.job.Job;
import com.ir.smartcity.location.APiInterface;
import com.ir.smartcity.location.Result;
import com.ir.smartcity.location.Route;
import com.ir.smartcity.user.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlarmDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private APiInterface apiInterface;
    private List<LatLng> polylinelist;
    private PolylineOptions polylineOptions;
    private LatLng origion,dest;

    private Job job;
    private TextView jobTitle, jobPayment, jobDetails, jobDeadline, hirerName, hirerPhone;
    private User jobHirer;
    private Double jobLocationLat, jobLocationLon;
    private Button applyButton;
    private String hirerId, jobID, currentUserID;
    private DatabaseReference databaseReference;
    private ImageView hirerImage;
    private User userDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_details);
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl("https://maps.googleapis.com/")
                .build();
        apiInterface=retrofit.create(APiInterface.class);

        Gson gson = new Gson();
        job = gson.fromJson(getIntent().getStringExtra("job"), Job.class);
        jobID = getIntent().getStringExtra("jobID");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        jobTitle = findViewById(R.id.job_title);
        jobDetails = findViewById(R.id.job_details);
        //jobLocation = findViewById(R.id.job_location);
        applyButton = findViewById(R.id.button_apply);
        hirerName = findViewById(R.id.hirer_name);
        hirerPhone = findViewById(R.id.hirer_phone);
        hirerImage = findViewById(R.id.hirer_image);

        jobTitle.setText(job.getJobName());
        jobDetails.setText(job.getJobDetails());
        // jobLocation.setText(job.getJobLocation());
        hirerId = job.getHirerID();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyButton.setText("Notified. Please Rush Now.");
                applyButton.setEnabled(false);
            }
        });


//        databaseReference.child("users").child(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                userDetails = task.getResult().getValue(User.class);
//                applyButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        databaseReference.child("aa").child(jobID).child("applications").child(currentUserID).setValue(userDetails.getName());
//                        databaseReference.child("jobHistory").child(currentUserID).child("sentHelps").child(jobID).setValue(job.getJobName());
//                        applyButton.setText("Application Sent. Wait for approval.");
//                        applyButton.setEnabled(false);
//                    }
//                });
//            }
//        });

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
                Toast.makeText(AlarmDetailsActivity.this, "Couldn't access the person's details", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onMapReady(GoogleMap googleMap) {

        map=googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        origion=new LatLng(39.5340,85.3096);
        dest=new LatLng(25.4358,81.8463);
        map.addMarker(new MarkerOptions().position(origion).title("Destination"));
        map.moveCamera(CameraUpdateFactory.newLatLng(origion));
        getDirection("23.3441" + "," + "85.3096","25.4358"+","+"81.8463");

    }
    private void getDirection(String origin,String destination) {
        apiInterface.getDirection("driving", "less driving", origin, destination, getString(R.string.api_key)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Result result) {
                        polylinelist = new ArrayList<>();
                        List<Route> routeList = result.getRoutes();
                        for (Route route : routeList) {
                            String polyline = route.getOverviewPolyline().getPoints();
                            polylinelist.addAll(decodePoly(polyline));
                        }
                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(ContextCompat.getColor(getApplicationContext(), R.color.primaryTextColor));
                        polylineOptions.width(0);
                        polylineOptions.startCap(new ButtCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polylinelist);
                        map.addPolyline(polylineOptions);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(origion);
                        builder.include(dest);
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : result >> 1);
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}