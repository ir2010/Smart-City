package com.ir.smartcity.community;

import java.util.ArrayList;
import java.util.HashMap;

public class Alarm {

    private String alarmName;
    private String alarmDeadline;
    //private String alarmLocation;
    private Double alarmLocationLat, alarmLocationLon;
    private String alarmDetails;
    private ArrayList<String> alarmPhotoList;
    private String hirerID;
    private String alarmID;
    private HashMap<String, String> applications;
    //private String alarmDivider;

    public Alarm()
    {}

//    public Alarm(String alarmName, String alarmDeadline, String alarmLocation, String alarmDetails, String alarmPayment)
//    {
//        this.alarmName = alarmName;
//        this.alarmDeadline = alarmDeadline;
//        this.alarmLocation = alarmLocation;
//        this.alarmDetails = alarmDetails;
//        this.alarmPayment = alarmPayment;
//        //this.alarmDivider = "----------------------------------------------------------------------------------------";
//    }

    public Alarm(String alarmName, String alarmDeadline, Double alarmLocationLat, Double alarmLocationLon, String alarmDetails) {
        this.alarmName = alarmName;
        this.alarmDeadline = alarmDeadline;
        this.alarmLocationLat = alarmLocationLat;
        this.alarmLocationLon = alarmLocationLon;
        this.alarmDetails = alarmDetails;
    }

    public Alarm(String alarmName, String alarmDeadline, Double alarmLocationLat, Double alarmLocationLon, String alarmDetails, ArrayList<String> alarmPhotoList, String hirerID, String alarmID) {
        this.alarmName = alarmName;
        this.alarmDeadline = alarmDeadline;
        this.alarmLocationLat = alarmLocationLat;
        this.alarmLocationLon = alarmLocationLon;
        this.alarmDetails = alarmDetails;
        this.alarmPhotoList = alarmPhotoList;
        this.hirerID = hirerID;
        this.alarmID = alarmID;
    }

//    public Alarm(String alarmName, String alarmDeadline, String alarmLocation, String alarmDetails, String alarmPayment, ArrayList<String> alarmPhotoList, String hirerID, String alarmId)
//    {
//        this.alarmName = alarmName;
//        this.alarmDeadline = alarmDeadline;
//        this.alarmLocation = alarmLocation;
//        this.alarmDetails = alarmDetails;
//        this.alarmPayment = alarmPayment;
//        this.alarmPhotoList = new ArrayList<String>(alarmPhotoList);
//        //Collections.copy(this.alarmPhotoList, alarmPhotoList);
//        this.hirerID = hirerID;
//        this.alarmID = alarmId;
//        //this.alarmDivider = "----------------------------------------------------------------------------------------";
//    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmDeadline() {
        return alarmDeadline;
    }

    public void setAlarmDeadline(String alarmDeadline) {
        this.alarmDeadline = alarmDeadline;
    }

    public Double getAlarmLocationLat() {
        return alarmLocationLat;
    }

    public void setAlarmLocationLat(Double alarmLocationLat) {
        this.alarmLocationLat = alarmLocationLat;
    }

    public Double getAlarmLocationLon() {
        return alarmLocationLon;
    }

    public void setAlarmLocationLon(Double alarmLocationLon) {
        this.alarmLocationLon = alarmLocationLon;
    }

    public String getAlarmDetails() {
        return alarmDetails;
    }

    public void setAlarmDetails(String alarmDetails) {
        this.alarmDetails = alarmDetails;
    }

    public ArrayList<String> getAlarmPhotoList() {
        return alarmPhotoList;
    }

    public void setAlarmPhotoList(ArrayList<String> alarmPhotoList) {
        this.alarmPhotoList = alarmPhotoList;
    }

    public String getHirerID() {
        return hirerID;
    }

    public void setHirerID(String hirerID) {
        this.hirerID = hirerID;
    }

    public String getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(String alarmID) {
        this.alarmID = alarmID;
    }

    public HashMap<String, String> getApplications() {
        return applications;
    }

    public void setApplications(HashMap<String, String> applications) {
        this.applications = applications;
    }

}
