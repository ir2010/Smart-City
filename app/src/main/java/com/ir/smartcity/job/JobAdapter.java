package com.ir.smartcity.job;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ir.smartcity.R;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder>{

    private List<Job> jobList;

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joblist_item, parent, false);
        return new JobAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.ViewHolder holder, int position) {

        String name = jobList.get(position).getJobName();
        String deadline = jobList.get(position).getJobDeadline();
       // String location = jobList.get(position).getJobLocation();
        String details = jobList.get(position).getJobDetails();
        String payment = jobList.get(position).getJobPayment();
        //String line = jobList.get(position).getJobDivider();

        holder.setData(name, deadline, details, payment);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView jobName;
        private TextView jobDeadline;
        //private TextView jobLocation;
        private TextView jobDetails;
        private TextView jobPayment;
        //private TextView divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jobName=itemView.findViewById(R.id.job_name);
            jobDeadline=itemView.findViewById(R.id.job_deadline);
            //jobLocation=itemView.findViewById(R.id.job_location);
            jobDetails=itemView.findViewById(R.id.job_details);
            jobPayment=itemView.findViewById(R.id.job_payment);
            //divider=itemView.findViewById(R.id.job_divider);
        }

        public void setData(String name, String deadline, String details, String payment) {
            jobName.setText(name);
            jobDeadline.setText(deadline);
            jobDetails.setText(details);
            //jobLocation.setText(location);
            jobPayment.setText(payment);
            //divider.setText(line);
        }
    }
}
