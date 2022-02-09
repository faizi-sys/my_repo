package com.example.iiui_project.Model;

public class NotificationFromUser {
    private  String DriveTitle = " " ;
    private  String UserId = "" ;
    private  String DriverId = "" ;
    private  String DrivePic = "" ;
    private String DriverLat = "" ;
    private String DriverLong = "" ;
    private  String Time = "" ;
    private  String Address = " " ;
    private  String Pushid = " " ;
    private  String Reckey = "";



    public NotificationFromUser() {
    }

    public NotificationFromUser(String userId, String driverId, String drivePic, String driverLat, String driverLong, String time, String address , String pushid , String reckey
     , String DriveTitle1) {
        UserId = userId;
        DriverId = driverId;
        DrivePic = drivePic;
        DriverLat = driverLat;
        DriverLong = driverLong;
        Time = time;
        Address = address;
        Pushid = pushid ;
 Reckey    = reckey ;
        DriveTitle = DriveTitle1 ;
}

    public String getDriveTitle() {
        return DriveTitle;
    }

    public void setDriveTitle(String driveTitle) {
        DriveTitle = driveTitle;
    }

    public String getPushid() {
        return Pushid;
    }

    public void setPushid(String pushid) {
        Pushid = pushid;
    }

    public String getUserId() {
        return UserId;
    }

    public String getDriverId() {
        return DriverId;
    }

    public String getDrivePic() {
        return DrivePic;
    }

    public String getDriverLat() {
        return DriverLat;
    }

    public String getDriverLong() {
        return DriverLong;
    }

    public String getTime() {
        return Time;
    }

    public String getAddress() {
        return Address;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public void setDrivePic(String drivePic) {
        DrivePic = drivePic;
    }

    public void setDriverLat(String driverLat) {
        DriverLat = driverLat;
    }

    public void setDriverLong(String driverLong) {
        DriverLong = driverLong;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getReckey() {
        return Reckey;
    }

    public void setReckey(String reckey) {
        Reckey = reckey;
    }
}
