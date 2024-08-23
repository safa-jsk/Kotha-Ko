package com.triad.kothako;

import java.util.ArrayList;

public class groups {
    String pfp;
    String group_name;
    String group_id;
    ArrayList<String> members;

    public groups() {
    }

    public groups(String pfp, String group_name, String group_id, ArrayList<String> members) {
        this.pfp = pfp;
        this.group_name = group_name;
        this.group_id = group_id;
        this.members = members;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}
