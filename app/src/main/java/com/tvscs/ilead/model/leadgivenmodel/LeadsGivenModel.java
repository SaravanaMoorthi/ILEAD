package com.tvscs.ilead.model.leadgivenmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class LeadsGivenModel implements Serializable {

    //Get LeadsGivenActivity
    private String user_id, lead_id, mobile;
    private ArrayList<RefLeadsgivenResult> RefLeadsgivenResult;

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

    public ArrayList<RefLeadsgivenResult> getRefLeadsgivenResult() {
        return RefLeadsgivenResult;
    }

    public void setRefLeadsgivenResult(ArrayList<RefLeadsgivenResult> refLeadsgivenResult) {
        RefLeadsgivenResult = refLeadsgivenResult;
    }

    @Override
    public String toString() {
        return "ClassPojo [RefLeadsgivenResult = " + RefLeadsgivenResult + "]";
    }
}

