package com.tvscs.ilead.model.pickleadmodel;

import java.io.Serializable;

public class RefpickleadbyIDResult implements Serializable {

    private String d_status;

    public String getD_status() {
        return d_status;
    }

    public void setD_status(String d_status) {
        this.d_status = d_status;
    }

    @Override
    public String toString() {
        return "ClassPojo [d_status = " + d_status + "]";
    }
}
