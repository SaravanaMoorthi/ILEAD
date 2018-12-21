package com.tvscs.ilead.model.pickleadmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class PickLeadsModel implements Serializable {

    //Get LeadsGivenActivity
    private String user_id, lead_id, mobile;

    private String Ecount;
    private String Scount;
    private ArrayList<RefpickleadResult> RefpickleadResult;

    public String getEcount() {
        return Ecount;
    }

    public void setEcount(String ecount) {
        Ecount = ecount;
    }

    public String getScount() {
        return Scount;
    }

    public void setScount(String scount) {
        Scount = scount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ArrayList<RefpickleadResult> getRefpickleadResult() {
        return RefpickleadResult;
    }

    public void setRefpickleadResult(ArrayList<RefpickleadResult> refpickleadResult) {
        RefpickleadResult = refpickleadResult;
    }

    @Override
    public String toString() {
        return "ClassPojo [RefpickleadResult = " + RefpickleadResult + "]";
    }
}

