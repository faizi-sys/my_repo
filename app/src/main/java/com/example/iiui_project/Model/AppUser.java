package com.example.iiui_project.Model;

import java.util.Locale;

public class AppUser {
    private String Name = "";
    private String Email = "";
    private  String Password = "";
    private String Cnic = "";
    private  String Phone = "";
    private String Age= "";

    private String Lattitude = "";
    private String Longitude = "";
    private String picUri  = "";

    private String status = "";
    private String usertype = "";
    private  String cartype = "";


    private  String Price = "";
    private String Pushid ="";
    private String carcolor ="";
    private String Location ="";
    private String CarNumber ="";


    public AppUser() {

    }

    public AppUser(String name, String email, String password, String cnic, String phone, String age, String lattitude, String longitude, String picUri, String status, String usertype, String cartype, String price, String pushid, String carcolor , String location , String carNumber) {
        Name = name;
        Email = email;
        Password = password;
        Cnic = cnic;
        Phone = phone;
        Age = age;
        Lattitude = lattitude;
        Longitude = longitude;
        this.picUri = picUri;
        this.status = status;
        this.usertype = usertype;
        this.cartype = cartype;
        Price = price;
        Pushid = pushid;
        this.carcolor = carcolor;
        Location = location;
        CarNumber = carNumber ;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getCnic() {
        return Cnic;
    }

    public String getPhone() {
        return Phone;
    }

    public String getAge() {
        return Age;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getPicUri() {
        return picUri;
    }

    public String getStatus() {
        return status;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getCartype() {
        return cartype;
    }

    public String getPrice() {
        return Price;
    }

    public String getPushid() {
        return Pushid;
    }

    public String getCarcolor() {
        return carcolor;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setCnic(String cnic) {
        Cnic = cnic;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setAge(String age) {
        Age = age;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setPushid(String pushid) {
        Pushid = pushid;
    }

    public void setCarcolor(String carcolor) {
        this.carcolor = carcolor;
    }
}
