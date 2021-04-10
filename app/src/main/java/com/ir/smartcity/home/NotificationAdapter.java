package com.ir.smartcity.home;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.ir.smartcity.R;
import com.ir.smartcity.chat.ChatActivity;
import com.ir.smartcity.job.Job;
import com.ir.smartcity.user.User;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
{
    private List<Notification> notificationList;
    private DatabaseReference databaseReference;
    private String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String heading = notificationList.get(position).getNotiHeading();
        String description = notificationList.get(position).getNotiDesc();
        String time = notificationList.get(position).getNotiTime();
        int image= notificationList.get(position).getNotiImage();
        String type = notificationList.get(position).getType();
        Job jobRelatedToNotification = notificationList.get(position).getJob();
        Context context = notificationList.get(position).getContext();
        User applicant = notificationList.get(position).getApplicant();

        holder.setData(heading, description, time, image, type, jobRelatedToNotification, context, applicant);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView notihead;
        private TextView notidesc;
        //private TextView jobLocation;
        private TextView notitime;
        private ImageView notiimage;
        private RelativeLayout notiItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notihead=itemView.findViewById(R.id.noti_head);
            notidesc=itemView.findViewById(R.id.noti_desc);
            notitime=itemView.findViewById(R.id.noti_time);
            notiimage=itemView.findViewById(R.id.noti_image);
            notiItem=itemView.findViewById(R.id.notification_item);
        }

        public void setData(String heading, String description, String time, int image, String type, Job job, Context context, User applicant) {
            notihead.setText(heading);
            notidesc.setText(description);
            notitime.setText(time);
            notiimage.setImageResource(image);
            notiItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type == "accept")           //request accepted
                    {
                        Intent intent = new Intent(context, ChatActivity.class);
                        Gson gson = new Gson();
                        String jobJson = gson.toJson(job);

                        intent.putExtra("job", jobJson);
                        context.startActivity(intent);
                    }

                    if(type == "request")
                    {
                        //TODO: open helper profile

                        //accepting request
                        databaseReference.child("jobs").child(job.getJobID()).child("helpers").push().child(applicant.getUid());
                        databaseReference.child("jobs").child(job.getJobID()).child("applications").child(applicant.getUid()).removeValue();
                        databaseReference.child("jobHistory").child(applicant.getUid()).child("sentHelps").child(job.getJobID()).removeValue();
                        databaseReference.child("jobHistory").child(applicant.getUid()).child("activeHelps").child(job.getJobID()).setValue(job.getJobName());
                        databaseReference.child("jobHistory").child(currentUserID).child("activeHires").child(job.getJobID()).setValue(job.getJobName());

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Do you need more helpers for this job?");

                        alertDialog.setIcon(R.drawable.jobs);

                        alertDialog.setPositiveButton("Yes, keep the job active.",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.setNegativeButton("No, remove the job now.",
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                databaseReference.child("jobs").child(job.getJobID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        databaseReference.child("jobHistory").child(currentUserID).child("uploadedHires").child(job.getJobID()).removeValue();
                                        Toast.makeText(context, "Job removed.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();

                        //rejecting request

//                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                        alertDialog.setTitle("Are you sure you want to delete this request?");
//                        alertDialog.setMessage("You won't be able to undo this.");
//
//                        alertDialog.setIcon(R.drawable.jobs);
//
//                        alertDialog.setPositiveButton("Yes, delete",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        databaseReference.child("jobs").child(job.getJobID()).child("applications").child(applicant.getUid()).removeValue();
//                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                        alertDialog.setNegativeButton("No, keep.",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                        dialog.dismiss();
//                                    }
//                                });
//                        alertDialog.show();

                    }
                }
            });
        }
    }
}