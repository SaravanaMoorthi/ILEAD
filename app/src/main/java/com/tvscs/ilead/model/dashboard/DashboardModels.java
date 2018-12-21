package com.tvscs.ilead.model.dashboard;

import java.io.Serializable;
import java.util.ArrayList;

public class DashboardModels implements Serializable {
    private String user_id;

    private ArrayList<Ref_Dashboard_NewResult> Ref_Dashboard_NewResult;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<Ref_Dashboard_NewResult> getRef_Dashboard_NewResult() {
        return Ref_Dashboard_NewResult;
    }

    public void setRef_Dashboard_NewResult(ArrayList<Ref_Dashboard_NewResult> ref_Dashboard_NewResult) {
        Ref_Dashboard_NewResult = ref_Dashboard_NewResult;
    }

    @Override
    public String toString() {
        return "ClassPojo [Ref_Dashboard_NewResult = " + Ref_Dashboard_NewResult + "]";
    }
}
