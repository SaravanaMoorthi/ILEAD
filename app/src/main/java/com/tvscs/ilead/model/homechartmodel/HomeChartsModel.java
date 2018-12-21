package com.tvscs.ilead.model.homechartmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeChartsModel implements Serializable
{
    private String user_id;
    private ArrayList<RefConeResult> RefConeResult;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<RefConeResult> getRefConeResult() {
        return RefConeResult;
    }

    public void setRefConeResult(ArrayList<RefConeResult> refConeResult) {
        RefConeResult = refConeResult;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [RefConeResult = "+RefConeResult+"]";
    }
}

