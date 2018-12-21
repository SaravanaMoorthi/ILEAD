package com.tvscs.ilead.model.homechartmodel.homesubchartmodel;

import com.tvscs.ilead.model.homechartmodel.RefConenewResult;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeSubChartModel  implements Serializable{
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private ArrayList<RefConenewResult> RefConenewResult;

    public ArrayList<RefConenewResult> getRefConenewResult() {
        return RefConenewResult;
    }

    public void setRefConenewResult(ArrayList<RefConenewResult> refConenewResult) {
        RefConenewResult = refConenewResult;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [RefConenewResult = "+RefConenewResult+"]";
    }
}
