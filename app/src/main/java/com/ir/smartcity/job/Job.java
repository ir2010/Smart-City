package com.ir.smartcity.job;

import java.util.ArrayList;
import java.util.HashMap;

public class Job {

    private String jobName;
    private String jobDeadline;
    //private String jobLocation;
    private Double jobLocationLat, jobLocationLon;
    private String jobCategory;
    private String jobDetails;
    private String jobPayment;
    private ArrayList<String> jobPhotoList;
    private String hirerID;
    private String jobID;
    private HashMap<String, String> applications;
    //private String jobDivider;

    public Job()
    {}

//    public Job(String jobName, String jobDeadline, String jobLocation, String jobDetails, String jobPayment)
//    {
//        this.jobName = jobName;
//        this.jobDeadline = jobDeadline;
//        this.jobLocation = jobLocation;
//        this.jobDetails = jobDetails;
//        this.jobPayment = jobPayment;
//        //this.jobDivider = "----------------------------------------------------------------------------------------";
//    }

    public Job(String jobName, String jobDeadline, Double jobLocationLat, Double jobLocationLon, String jobDetails, String jobPayment) {
        this.jobName = jobName;
        this.jobDeadline = jobDeadline;
        this.jobLocationLat = jobLocationLat;
        this.jobLocationLon = jobLocationLon;
        this.jobDetails = jobDetails;
        this.jobPayment = jobPayment;
    }

    public Job(String jobName, String jobDeadline, Double jobLocationLat, Double jobLocationLon, String jobDetails, String jobPayment, String jobCategory, ArrayList<String> jobPhotoList, String hirerID, String jobID) {
        this.jobName = jobName;
        this.jobDeadline = jobDeadline;
        this.jobLocationLat = jobLocationLat;
        this.jobLocationLon = jobLocationLon;
        this.jobCategory = jobCategory;
        this.jobDetails = jobDetails;
        this.jobPayment = jobPayment;
        this.jobPhotoList = jobPhotoList;
        this.hirerID = hirerID;
        this.jobID = jobID;
    }

//    public Job(String jobName, String jobDeadline, String jobLocation, String jobDetails, String jobPayment, ArrayList<String> jobPhotoList, String hirerID, String jobId)
//    {
//        this.jobName = jobName;
//        this.jobDeadline = jobDeadline;
//        this.jobLocation = jobLocation;
//        this.jobDetails = jobDetails;
//        this.jobPayment = jobPayment;
//        this.jobPhotoList = new ArrayList<String>(jobPhotoList);
//        //Collections.copy(this.jobPhotoList, jobPhotoList);
//        this.hirerID = hirerID;
//        this.jobID = jobId;
//        //this.jobDivider = "----------------------------------------------------------------------------------------";
//    }

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

    public Double getJobLocationLat() {
        return jobLocationLat;
    }

    public void setJobLocationLat(Double jobLocationLat) {
        this.jobLocationLat = jobLocationLat;
    }

    public Double getJobLocationLon() {
        return jobLocationLon;
    }

    public void setJobLocationLon(Double jobLocationLon) {
        this.jobLocationLon = jobLocationLon;
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

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
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

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public HashMap<String, String> getApplications() {
        return applications;
    }

    public void setApplications(HashMap<String, String> applications) {
        this.applications = applications;
    }

    //    public String getJobDivider() {
//        return jobDivider;
//    }
//
//    public void setJobDivider(String jobDivider) {
//        this.jobDivider = jobDivider;
//    }
}
