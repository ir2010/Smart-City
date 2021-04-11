package com.ir.smartcity.community;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.ir.smartcity.R;
import com.ir.smartcity.home.BottomNavigation;
import com.ir.smartcity.home.HomeActivity;
import com.ir.smartcity.home.SliderAdp;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;

public class CommunityActivity extends AppCompatActivity {

    private SliderView sliderView;
    private ArrayList<Integer> images = new ArrayList<Integer>(Arrays.asList(R.drawable.pic, R.drawable.pic1, R.drawable.pic2, R.drawable.pic4, R.drawable.pic5, R.drawable.pic6, R.drawable.pic7));
    private SliderAdp sliderAdp;
    private BottomNavigation bottomNavigation = new BottomNavigation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        //bottomNavigation.implement(findViewById(R.id.bottom_nav), CommunityActivity.this);

        //begin of slide view
        sliderView = findViewById(R.id.slider_view);

        //Initialize Adapter
        sliderAdp= new SliderAdp(images);

        //Set Adapter
        sliderView.setSliderAdapter(sliderAdp);

        //Set indicator animation
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);

        //Set transformation Animation
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);

        //Start Auto Cycle
        sliderView.startAutoCycle();
    }
}