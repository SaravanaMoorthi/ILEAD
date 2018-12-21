package com.tvscs.ilead.model.homechartmodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RefConeResult implements Serializable {

    private ArrayList<Lstuserdtls>  lstuserdtls;


//    @SerializedName("RefConenewResult")
    private ArrayList<RefConenewResult>  lstconedtls;



    private ArrayList<RefConenewResult>  RefConenewResult;

    public ArrayList<com.tvscs.ilead.model.homechartmodel.RefConenewResult> getRefConenewResult() {
        return RefConenewResult;
    }

    public void setRefConenewResult(ArrayList<com.tvscs.ilead.model.homechartmodel.RefConenewResult> refConenewResult) {
        RefConenewResult = refConenewResult;
    }

    public ArrayList<Lstuserdtls> getLstuserdtls() {
        return lstuserdtls;
    }

    public void setLstuserdtls(ArrayList<Lstuserdtls> lstuserdtls) {
        this.lstuserdtls = lstuserdtls;
    }

    public ArrayList<RefConenewResult> getLstconedtls() {
        return lstconedtls;
    }

    public void setLstconedtls(ArrayList<RefConenewResult> lstconedtls) {
        this.lstconedtls = lstconedtls;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lstuserdtls = " + lstuserdtls + ", RefConenewResult = " + lstconedtls + "]";
    }
}

