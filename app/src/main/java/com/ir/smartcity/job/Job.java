package com.ir.smartcity.job;

import java.util.ArrayList;

public class Job {

    private String jobName;
    private String jobDeadline;
    private String jobLocation;
    private String jobDetails;
    private String jobPayment;
    private ArrayList<String> jobPhotoList;
    private String hirerID;
    private String uid;
    //private String jobDivider;

    public Job()
    {}

    public Job(String jobName, String jobDeadline, String jobLocation, String jobDetails, String jobPayment)
    {
        this.jobName = jobName;
        this.jobDeadline = jobDeadline;
        this.jobLocation = jobLocation;
        this.jobDetails = jobDetails;
        this.jobPayment = jobPayment;
        //this.jobDivider = "----------------------------------------------------------------------------------------";
    }

    public Job(String jobName, String jobDeadline, String jobLocation, String jobDetails, String jobPayment, ArrayList<String> jobPhotoList, String hirerID)
    {
        this.jobName = jobName;
        this.jobDeadline = jobDeadline;
        this.jobLocation = jobLocation;
        this.jobDetails = jobDetails;
        this.jobPayment = jobPayment;
        this.jobPhotoList = new ArrayList<String>(jobPhotoList);
        //Collections.copy(this.jobPhotoList, jobPhotoList);
        this.hirerID = hirerID;
        //this.jobDivider = "----------------------------------------------------------------------------------------";
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDeadline() {
        return jobDeadline;
    }

    public void setJobDeadline(String jobDeadline) {
        this.jobDeadline = jobDeadline;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobDetails() {
        return jobDetails;
    }

    public void setJobDetails(String jobDetails) {
        this.jobDetails = jobDetails;
    }

    public String getJobPayment() {
        return jobPayment;
    }

    public void setJobPayment(String jobPayment) {
        this.jobPayment = jobPayment;
    }

    public ArrayList<String> getJobPhotoList() {
        return jobPhotoList;
    }

    public void setJobPhotoList(ArrayList<String> jobPhotoList) {
        this.jobPhotoList = jobPhotoList;
    }

    public String getHirerID() {
        return hirerID;
    }

    public void setHirerID(String hirerID) {
        this.hirerID = hirerID;
    }

    //    public String getJobDivider() {
//        return jobDivider;
//    }
//
//    public void setJobDivider(String jobDivider) {
//        this.jobDivider = jobDivider;
//    }
}
