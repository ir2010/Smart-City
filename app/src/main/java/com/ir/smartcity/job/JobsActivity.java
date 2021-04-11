package com.ir.smartcity.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.ir.smartcity.R;
import com.ir.smartcity.home.BottomNavigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class JobsActivity extends AppCompatActivity {

    private RecyclerView jobListView;
    private List<Job> jobList;
    private JobAdapter adapter;
    private Button buttonSort, buttonFilter;
    private BottomNavigation bottomNavigation = new BottomNavigation();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        buttonSort = (Button) findViewById(R.id.sortbutton);
        buttonFilter = (Button) findViewById(R.id.filterbutton);
        bottomNavigation.implement(findViewById(R.id.bottom_nav), JobsActivity.this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        jobListView= (RecyclerView)findViewById(R.id.jobs_list);
        jobListView.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager=new LinearLayoutManager(this);
        LinearLayoutManager.setReverseLayout(true);
        LinearLayoutManager.setStackFromEnd(true);
        jobListView.setLayoutManager(LinearLayoutManager);

        initFirebaseRecyclerView();

        buttonSort.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(JobsActivity.this, buttonSort);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.sort_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(JobsActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                //showing popup menu
                startActivity(new Intent(JobsActivity.this, FilterActivity.class));
            }
        });
    }

    private void initFirebaseRecyclerView() {

        FirebaseRecyclerOptions<Job> options = new FirebaseRecyclerOptions.Builder<Job>().setQuery(databaseReference.child("jobs"), Job.class).build();
        FirebaseRecyclerAdapter<Job, JobViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Job, JobViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final JobViewHolder jobViewHolder, int i, @NonNull final Job job) {
                jobViewHolder.jobName.setText(job.getJobName());
                jobViewHolder.jobDetails.setText(job.getJobDetails());
                jobViewHolder.jobDeadline.setText(job.getJobDeadline());
                jobViewHolder.jobPayment.setText(job.getJobPayment());

                jobViewHolder.jobItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(JobsActivity.this, JobDetailsActivity.class);

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
            public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joblist_item, parent, false);

                return new JobViewHolder(view);
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
}