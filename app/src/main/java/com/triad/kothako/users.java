package com.triad.kothako;

public class users {
    String pfp, gmail, username, pass, userid, last_message, status;

    public users() {
        // Default constructor required for calls to DataSnapshot.getValue(users.class)
    }

    public users(String id, String name, String mail, String pass, String image_URI, String status){
        this.userid = id;
        this.username = name;
        this.gmail = mail;
        this.pass = pass;
        this.pfp = image_URI;
        this.status = status;
    }


    public String getPfp() { return pfp; }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) { this.gmail = gmail; }

    public String getUsername() { return username; }

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

    public String getLast_message() {return last_message; }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
