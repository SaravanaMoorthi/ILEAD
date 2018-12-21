package com.tvscs.ilead.model.dashboard;

import java.io.Serializable;

public class DashboardModel implements Serializable {

    private String REJECTED_STATUS;

    private String PRELOGIN_STATUS;

    private String OPEN_STATUS;

    private String LOGREJ_STATUS;

    private String DISBURSED_STATUS;

    private String LOGIN_STATUS;

    private String LOST_STATUS;

    private String DEFERRED_STATUS;

    private String SANTION_STATUS;

    private String LEADS;

    private String NEW_STATUS;

    private String DROPPED_STATUS;

    private String user_id;

    private String lead_id;
    private String mobile;

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getREJECTED_STATUS() {
        return REJECTED_STATUS;
    }

    public void setREJECTED_STATUS(String REJECTED_STATUS) {
        this.REJECTED_STATUS = REJECTED_STATUS;
    }

    public String getPRELOGIN_STATUS() {
        return PRELOGIN_STATUS;
    }

    public void setPRELOGIN_STATUS(String PRELOGIN_STATUS) {
        this.PRELOGIN_STATUS = PRELOGIN_STATUS;
    }

    public String getOPEN_STATUS() {
        return OPEN_STATUS;
    }

    public void setOPEN_STATUS(String OPEN_STATUS) {
        this.OPEN_STATUS = OPEN_STATUS;
    }

    public String getLOGREJ_STATUS() {
        return LOGREJ_STATUS;
    }

    public void setLOGREJ_STATUS(String LOGREJ_STATUS) {
        this.LOGREJ_STATUS = LOGREJ_STATUS;
    }

    public String getDISBURSED_STATUS() {
        return DISBURSED_STATUS;
    }

    public void setDISBURSED_STATUS(String DISBURSED_STATUS) {
        this.DISBURSED_STATUS = DISBURSED_STATUS;
    }

    public String getLOGIN_STATUS() {
        return LOGIN_STATUS;
    }

    public void setLOGIN_STATUS(String LOGIN_STATUS) {
        this.LOGIN_STATUS = LOGIN_STATUS;
    }

    public String getLOST_STATUS() {
        return LOST_STATUS;
    }

    public void setLOST_STATUS(String LOST_STATUS) {
        this.LOST_STATUS = LOST_STATUS;
    }

    public String getDEFERRED_STATUS() {
        return DEFERRED_STATUS;
    }

    public void setDEFERRED_STATUS(String DEFERRED_STATUS) {
        this.DEFERRED_STATUS = DEFERRED_STATUS;
    }

    public String getSANTION_STATUS() {
        return SANTION_STATUS;
    }

    public void setSANTION_STATUS(String SANTION_STATUS) {
        this.SANTION_STATUS = SANTION_STATUS;
    }

    public String getLEADS() {
        return LEADS;
    }

    public void setLEADS(String LEADS) {
        this.LEADS = LEADS;
    }

    public String getNEW_STATUS() {
        return NEW_STATUS;
    }

    public void setNEW_STATUS(String NEW_STATUS) {
        this.NEW_STATUS = NEW_STATUS;
    }

    public String getDROPPED_STATUS() {
        return DROPPED_STATUS;
    }

    public void setDROPPED_STATUS(String DROPPED_STATUS) {
        this.DROPPED_STATUS = DROPPED_STATUS;
    }

    @Override
    public String toString() {
        return "ClassPojo [REJECTED_STATUS = " + REJECTED_STATUS + ", PRELOGIN_STATUS = " + PRELOGIN_STATUS + ", OPEN_STATUS = " + OPEN_STATUS + ", LOGREJ_STATUS = " + LOGREJ_STATUS + ", DISBURSED_STATUS = " + DISBURSED_STATUS + ", LOGIN_STATUS = " + LOGIN_STATUS + ", LOST_STATUS = " + LOST_STATUS + ", DEFERRED_STATUS = " + DEFERRED_STATUS + ", SANTION_STATUS = " + SANTION_STATUS + ", LEADS = " + LEADS + ", NEW_STATUS = " + NEW_STATUS + ", DROPPED_STATUS = " + DROPPED_STATUS + "]";
    }
}
