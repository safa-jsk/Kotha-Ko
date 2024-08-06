package com.example.kothako;

public class user {
    String pfp, gmail, username, pass, userid, lastnessage, status;


    public user(String id, String name, String mail, String pass, String image_URI, String status){
        this.userid = id;
        this.username = name;
        this.gmail = mail;
        this.pass = pass;
        this.pfp = image_URI;
        this.status = status;
    }


    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastnessage() {
        return lastnessage;
    }

    public void setLastnessage(String lastnessage) {
        this.lastnessage = lastnessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
