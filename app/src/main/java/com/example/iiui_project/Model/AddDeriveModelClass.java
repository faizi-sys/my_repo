package com.example.iiui_project.Model;

public class AddDeriveModelClass {
private  String Driverkey  = "" ;
private  String DrivePushId  = "" ;

private  String Title  = "" ;
private  String Time  = "" ;
private  String Date = "" ;
private  String Address  = "" ;
private  String Lattitude  = "" ;
private  String Lattitude2  = "" ;
private  String Longitude  = "" ;
private  String Longitude2  = "" ;
private  String imageurl  = "" ;


    public AddDeriveModelClass() {
    }

    public AddDeriveModelClass(String driverkey, String drivePushId, String title, String time, String date, String address, String lattitude, String longitude, String imageurl , String lattitude2 , String longitude2) {
        Driverkey = driverkey;
        DrivePushId = drivePushId;
        Title = title;
        Time = time;
        Date = date;
        Address = address;
        Lattitude = lattitude;
        Longitude = longitude;
        this.imageurl = imageurl;
        Lattitude2 = lattitude2 ;
        Longitude2 = longitude2 ;
    }

    public String getLattitude2() {
        return Lattitude2;
    }

    public void setLattitude2(String lattitude2) {
        Lattitude2 = lattitude2;
    }

    public void setLongitude2(String longitude2) {
        Longitude2 = longitude2;
    }

    public String getLongitude2() {
        return Longitude2;
    }

    public String getDriverkey() {
        return Driverkey;
    }

    public String getDrivePushId() {
        return DrivePushId;
    }

    public String getTitle() {
        return Title;
    }

    public String getTime() {
        return Time;
    }

    public String getDate() {
        return Date;
    }

    public String getAddress() {
        return Address;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setDriverkey(String driverkey) {
        Driverkey = driverkey;
    }

    public void setDrivePushId(String drivePushId) {
        DrivePushId = drivePushId;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
