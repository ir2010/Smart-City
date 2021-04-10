package com.ir.smartcity.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.ir.smartcity.job.Job;
import com.ir.smartcity.user.User;

public class Notification {
    private String Notihead;
    private String Notidesc;
    private String Notitime;
    private int Notiimage;
    private String type;
    private Job job;
    private Context context;
    private User applicant;
    private DataSnapshot dataSnapshot;

    public Notification(String Notihead, String Notidesc, String Notitime, int Notiimage, String type, Job job, Context context) {
        this.Notihead = Notihead;
        this.Notidesc = Notidesc;
        this.Notitime = Notitime;
        this.Notiimage = Notiimage;
        this.type=type;
        this.job = job;
        this.context = context;
    }

    public Notification(String Notihead, String Notidesc, String Notitime, int Notiimage, String type, Job job, Context context, DataSnapshot dataSnapshot) {
        this.Notihead = Notihead;
        this.Notidesc = Notidesc;
        this.Notitime = Notitime;
        this.Notiimage = Notiimage;
        this.type=type;
        this.job = job;
        this.context = context;
        //this.applicant = applicant;
        this.dataSnapshot = dataSnapshot;
    }

    public String getNotiHeading() {
        return Notihead;
    }

    public void setNotihead(String Notihead) {
        this.Notihead = Notihead;
    }

    public String getNotiDesc() {
        return Notidesc;
    }

    public void setNotidesc(String Notidesc) {
        this.Notidesc = Notidesc;
    }

    public String getNotiTime() {
        return Notitime;
    }

    public void setNotitime(String Notitime) {
        this.Notitime = Notitime;
    }

    public int getNotiImage() {
        return Notiimage;
    }

    public void setNotiimage(ImageView Notiimage) {
        this.Notitime = Notitime;
    }

    public String getNotihead() {
        return Notihead;
    }

    public String getNotidesc() {
        return Notidesc;
    }

    public String getNotitime() {
        return Notitime;
    }

    public int getNotiimage() {
        return Notiimage;
    }

    public void setNotiimage(int notiimage) {
        Notiimage = notiimage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }
}