package com.ir.smartcity.job;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ir.smartcity.R;

import java.io.IOException;
import java.util.List;

public class AddJobMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener{

    private GoogleMap mMap;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder=new Geocoder(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
        // Add a marker in Sydney and move the camera
        try {
            List<Address> addresses = geocoder.getFromLocationName("India", 1);
            if(addresses.size()>0) {
                Address address = addresses.get(0);

                LatLng india = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude()))
                        .title(address.getLocality()).draggable(true);
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(india, 16));
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



    }



    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d("Location","onMarkerDragStart: ");

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d("Location","onMarkerDrag: ");

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d("Location","onMarkerDragEnd: ");
        LatLng latLng=marker.getPosition();
        try {

            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addresses.size()>0)
            {
                Address address=addresses.get(0);
                String streetAddress= address.getAddressLine(0);
                marker.setTitle(streetAddress);
                Toast.makeText(AddJobMapsActivity.this,"Location Saved!",Toast.LENGTH_LONG).show();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng));

    }
}