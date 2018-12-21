package com.tvscs.ilead.model.pickleadmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class PickLeadIDModel implements Serializable {

    private String user_id, lead_id;
    private ArrayList<RefpickleadbyIDResult> RefpickleadbyIDResult;

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

    public ArrayList<com.tvscs.ilead.model.pickleadmodel.RefpickleadbyIDResult> getRefpickleadbyIDResult() {
        return RefpickleadbyIDResult;
    }

    public void setRefpickleadbyIDResult(ArrayList<com.tvscs.ilead.model.pickleadmodel.RefpickleadbyIDResult> refpickleadbyIDResult) {
        RefpickleadbyIDResult = refpickleadbyIDResult;
    }

    @Override
    public String toString() {
        return "ClassPojo [RefpickleadbyIDResult = " + RefpickleadbyIDResult + "]";
    }
}
