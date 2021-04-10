package com.ir.smartcity.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ir.smartcity.R;
import com.ir.smartcity.job.Job;
import com.ir.smartcity.user.User;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationActivity extends AppCompatActivity
{
    private RecyclerView NotiListView;
    private List<Notification> notificationList;
    private NotificationAdapter adapter;
    private DatabaseReference databaseReference;
    private String uid;
    private HashMap<Job, User> jobApplicants = new HashMap<Job, User>();
    private RelativeLayout noNotifications, yesNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        noNotifications = findViewById(R.id.no_noti);
        yesNotifications = findViewById(R.id.yesnotification);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        initdata();
    }

    private void initdata() {
        notificationList = new ArrayList<>();

        //new requests
        databaseReference.child("jobHistory").child(uid).child("uploadedHires").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot jobSnapshot, @Nullable String previousChildName)
            {

                //Toast.makeText(NotificationActivity.this, jobSnapshot.toString(), Toast.LENGTH_SHORT).show();
                databaseReference.child("jobs").child(jobSnapshot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Job job = task.getResult().getValue(Job.class);

                        //Toast.makeText(NotificationActivity.this, job.getJobID(), Toast.LENGTH_SHORT).show();
                        task.getResult().getRef().child("applications").addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot applySnapshot : snapshot.getChildren())
                                {
                                    Toast.makeText(NotificationActivity.this, applySnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                    notificationList.add(new Notification(
                                            "Your job "+jobSnapshot.getValue()+" has a new applicant - " + applySnapshot.getValue().toString()+".",
                                            "","", R.drawable.jobapp, "request", job,
                                            NotificationActivity.this, applySnapshot));
                                    initRecyclerView();
//                                        databaseReference.child("users").child(applySnapshot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                                                if(task.isSuccessful())
//                                                {
//                                                    User applicant = task.getResult().getValue(User.class);
//                                                    Toast.makeText(NotificationActivity.this, applicant.getUid(), Toast.LENGTH_SHORT).show();
//                                                    notificationList.add(new Notification(
//                                                            "Your job "+jobSnapshot.getValue()+" has a new applicant - "+applicant.getName()+".",
//                                                            "","", R.drawable.jobapp, "request", job, NotificationActivity.this, applicant));
//
//                                                    //Toast.makeText(NotificationActivity.this, job.getJobID(), Toast.LENGTH_SHORT).show();
//                                                    //if(notificationList.size() == jobSnapshot.getChildrenCount()*applySnapshot.getChildrenCount())
//                                                    initRecyclerView();
//                                                }
//                                            }
//                                        });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //request accepted
        databaseReference.child("jobHistory").child(uid).child("sentHelps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot jobSnapshot : snapshot.getChildren())
                {
                    databaseReference.child("jobs").child(jobSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dsnapshot) {
                            snapshot.getRef().child("helpers").child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                    {
                                        notificationList.add(new Notification("You are assigned the job - "+jobSnapshot.child("jobName").getValue()+".", "", "", R.drawable.jobacc, "accept", dsnapshot.getValue(Job.class), NotificationActivity.this));
                                        initRecyclerView();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void initdata()
//    {
//        notificationList = new ArrayList<>();
//        notificationList.add(new Notification("Jop application accepted",  "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "2 hours ago",R.drawable.jobacc));
//        notificationList.add(new Notification("New applicant for the job","The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "4 hours ago",R.drawable.jobapp));
//        notificationList.add(new Notification("Robbery Alert!!",  "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "5 hours ago",R.drawable.alarm));
//        notificationList.add(new Notification("Plantation drive invitation","The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "12 hours ago",R.drawable.comm));
//        notificationList.add(new Notification("Jop application accepted",  "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "2 hours ago",R.drawable.jobacc));
//        notificationList.add(new Notification("New applicant for the job","The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "4 hours ago",R.drawable.jobapp));
//        notificationList.add(new Notification("Robbery Alert!!",  "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "5 hours ago",R.drawable.alarm));
//        notificationList.add(new Notification("Plantation drive invitation","The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "12 hours ago",R.drawable.comm));
//    }

    private void initRecyclerView()
    {
        NotiListView=findViewById(R.id.noti_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        NotiListView.setLayoutManager(layoutManager);
        adapter=new NotificationAdapter(notificationList);
        NotiListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (notificationList.size() != 0)
        {
            noNotifications.setVisibility(View.GONE);
            yesNotifications.setVisibility(View.VISIBLE);
        }
        // Toast.makeText(NotificationActivity.this, notificationList.size(), Toast.LENGTH_SHORT).show();
    }
}