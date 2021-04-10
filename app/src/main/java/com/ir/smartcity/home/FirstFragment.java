package com.ir.smartcity.home;

import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ir.smartcity.R;

import java.io.IOException;
import java.util.List;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}
    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d("Location","onMapLongClick: "+ latLng.toString());
        try {

            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addresses.size()>0)
            {
                Address address=addresses.get(0);
                String streetAddress= address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions().position(latLng).title(streetAddress).draggable(true));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng));

    }