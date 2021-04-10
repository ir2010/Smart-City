package com.ir.smartcity.home;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.ir.smartcity.R;
import com.ir.smartcity.community.CommunityActivity;
import com.ir.smartcity.community.RaiseAlarmActivity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SliderView sliderView;
    private ArrayList<Integer> images = new ArrayList<Integer>(Arrays.asList(R.drawable.pic, R.drawable.pic1, R.drawable.pic2, R.drawable.pic4, R.drawable.pic5, R.drawable.pic6, R.drawable.pic7));
    private SliderAdp sliderAdp;
    private FirebaseUser user;
    private RecyclerView jobListView;
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

        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        newJob = findViewById(R.id.fab_newjob);
        newAlarm = findViewById(R.id.fab_alarm);
        newService = findViewById(R.id.fab_newservice);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
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
        uid = user.getUid();

        //getUserDetailsFromDatabase();

        newJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddAJobActivity.class));
            }
        });

        newAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RaiseAlarmActivity.class));
            }
        });

        populateRecyclerView();

        //deleteDeadJobs();
    }

    private void populateRecyclerView() {

        jobListView= (RecyclerView)findViewById(R.id.homerecycler);
        jobListView.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager=new LinearLayoutManager(this);
        LinearLayoutManager.setReverseLayout(true);
        LinearLayoutManager.setStackFromEnd(true);
        jobListView.setLayoutManager(LinearLayoutManager);

        initFirebaseRecyclerView();
    }

    private void initFirebaseRecyclerView() {
        FirebaseRecyclerOptions<Job> options = new FirebaseRecyclerOptions.Builder<Job>().setQuery(databaseReference.child("jobs"), Job.class).build();
        FirebaseRecyclerAdapter<Job, HomeActivity.JobViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Job, HomeActivity.JobViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final HomeActivity.JobViewHolder jobViewHolder, int i, @NonNull final Job job) {
                jobViewHolder.jobName.setText(job.getJobName());
                jobViewHolder.jobDetails.setText(job.getJobDetails());
                jobViewHolder.jobDeadline.setText(job.getJobDeadline());
                jobViewHolder.jobPayment.setText(job.getJobPayment());

                jobViewHolder.jobItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, JobDetailsActivity.class);

                        Gson gson = new Gson();
                        String jobJson = gson.toJson(job);

                        intent.putExtra("job", jobJson);
                        intent.putExtra("jobID", getRef(i).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public HomeActivity.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joblist_item, parent, false);

                return new HomeActivity.JobViewHolder(view);
            }
        };
        jobListView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder
    {
        TextView jobName, jobDeadline, jobDetails, jobPayment;
        ImageView jobLocation;
        RelativeLayout jobItem;

        public JobViewHolder(@NonNull View view)
        {
            super(view);

            jobName=view.findViewById(R.id.job_name);
            jobDeadline=view.findViewById(R.id.job_deadline);
            jobDetails=view.findViewById(R.id.job_details);
            jobPayment=view.findViewById(R.id.job_payment);
            jobLocation=view.findViewById(R.id.job_location);
            jobItem=view.findViewById(R.id.job_item);
        }
    }

    private void deleteDeadJobs() {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calFordTime.getTime());
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