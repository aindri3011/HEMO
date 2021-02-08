package com.aindri.hemo;

public class UserInformationModel {
String name;
String email;
String password;
String phone;
String city;
String gender;
String bloodgroup;
String agegroup;
//String image;


    public UserInformationModel() {

    }



    public UserInformationModel(String name, String email, String password, String phone, String city, String gender, String bloodgroup, String agegroup) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.agegroup = agegroup;



    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getAgegroup() {
        return agegroup;
    }

    public void setAgegroup(String agegroup) {
        this.agegroup = agegroup;
    }




}
