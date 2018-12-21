package com.tvscs.ilead.model;

import java.util.ArrayList;

public class DealersPojo {
    String DealerName, Dealercode;
    private ArrayList<DealersPojo> RefDealerResult;

    public DealersPojo(String DealerName, String Dealercode) {
        this.DealerName = DealerName;
        this.Dealercode = Dealercode;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getDealercode() {
        return Dealercode;
    }

    public void setDealercode(String dealercode) {
        Dealercode = dealercode;
    }

    public ArrayList<DealersPojo> getRefDealerResult() {
        return RefDealerResult;
    }

    public void setRefDealerResult(ArrayList<DealersPojo> refDealerResult) {
        RefDealerResult = refDealerResult;
    }

    @Override
    public String toString() {
        return "DelaerClassPojo [RefDealerResult = " + RefDealerResult + "]";
    }
}
