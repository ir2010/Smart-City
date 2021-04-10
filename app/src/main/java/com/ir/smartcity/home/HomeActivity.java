package com.ir.smartcity.home;

import android.content.Intent;
import android.os.Bundle;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ir.smartcity.R;
import com.ir.smartcity.community.CommunityActivity;
import com.ir.smartcity.job.*;

import com.ir.smartcity.register.LoginActivity;
import com.ir.smartcity.user.User;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SliderView sliderView;
    private int images[]= {R.drawable.pic, R.drawable.pic1, R.drawable.pic2,
            R.drawable.pic4, R.drawable.pic5, R.drawable.pic6, R.drawable.pic7};
    private SliderAdp sliderAdp;
    private FirebaseUser user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private FloatingActionButton newJob, newAlarm, newService;
    public static User userDetails;
    private String uid;
    private BottomNavigation bottomNavigation = new BottomNavigation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        newJob = findViewById(R.id.fab_newjob);
        newAlarm = findViewById(R.id.fab_alarm);
        newService = findViewById(R.id.fab_newservice);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigation.implement(findViewById(R.id.bottom_nav), HomeActivity.this);

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // uid = user.getUid();

        //   getUserDetailsFromDatabase();

        newJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddAJobActivity.class));
            }
        });
    }


    private void getUserDetailsFromDatabase() {

        databaseReference.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                userDetails = task.getResult().getValue(User.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Toast.makeText(this, "selected", Toast.LENGTH_SHORT).show();

        if(id == R.id.notification)
        {
            Toast.makeText(this, "not", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.jobs)
        {

        }
        else if(id == R.id.community)
        {

        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        if(id == R.id.nav_login){
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }

        //drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}