package com.ir.smartcity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.ir.smartcity.community.RaiseAlarmActivity;
import com.ir.smartcity.job.Job;
import com.ir.smartcity.job.JobAdapter;

import java.util.ArrayList;

public class StoreRoom {

//    public void getlocation(View view)
//    {
//        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
//        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
//        {
//            if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
//            {
//                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if(location !=null)
//                        {
//                            dlat=location.getLatitude();
//                            dlongt=location.getLongitude();
//                            Toast.makeText(RaiseAlarmActivity.this,"Success",Toast.LENGTH_SHORT);
//                        }
//                    }
//                });
//            }
//            else
//            {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION },44);
//            }
//        }
//        float results[]=new float[19];
//        Location.distanceBetween(lat,longt,dlat,dlongt,results);
//    }



//    private void initdata() {
//
//        jobList = new ArrayList<>();
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//        jobList.add(new Job("Teach my Std. 5 kid", "Mar 23 22:30", "Shastrinagar, Munger", "The job is to teach my child who is in Std. 5 for his exams. Duration will be 2 weeks. Subjects - English, Hindi, Science", "900"));
//    }
//
//    private void initRecyclerView()
//    {
//        jobListView=findViewById(R.id.jobs_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        jobListView.setLayoutManager(layoutManager);
//        adapter=new JobAdapter(jobList);
//        jobListView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }
}
