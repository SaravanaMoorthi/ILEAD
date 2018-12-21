package com.tvscs.ilead.model;

public class FollowupPojo {

    private String Lead_id, RowNo, TM_User_ID, action_code, created_On, created_by,
            leadCategory, nxt_act_code, nxt_act_date, nxt_act_time, result_code, status;

    public FollowupPojo(String Lead_id, String RowNo, String TM_User_ID, String action_code,
                        String created_On, String created_by, String leadCategory,
                        String nxt_act_code, String nxt_act_date, String nxt_act_time,
                        String result_code, String status) {
        this.Lead_id = Lead_id;
        this.RowNo = RowNo;
        this.TM_User_ID = TM_User_ID;
        this.action_code = action_code;
        this.created_On = created_On;
        this.created_by = created_by;
        this.leadCategory = leadCategory;
        this.nxt_act_code = nxt_act_code;
        this.nxt_act_date = nxt_act_date;
        this.nxt_act_time = nxt_act_time;
        this.result_code = result_code;
        this.status = status;
    }

    public String getLead_id() {
        return Lead_id;
    }

    public void setLead_id(String lead_id) {
        Lead_id = lead_id;
    }

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
    }

    public String getTM_User_ID() {
        return TM_User_ID;
    }

    public void setTM_User_ID(String TM_User_ID) {
        this.TM_User_ID = TM_User_ID;
    }

    public String getAction_code() {
        return action_code;
    }

    public void setAction_code(String action_code) {
        this.action_code = action_code;
    }

    public String getCreated_On() {
        return created_On;
    }

    public void setCreated_On(String created_On) {
        this.created_On = created_On;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLeadCategory() {
        return leadCategory;
    }

    public void setLeadCategory(String leadCategory) {
        this.leadCategory = leadCategory;
    }

    public String getNxt_act_code() {
        return nxt_act_code;
    }

    public void setNxt_act_code(String nxt_act_code) {
        this.nxt_act_code = nxt_act_code;
    }

    public String getNxt_act_date() {
        return nxt_act_date;
    }

    public void setNxt_act_date(String nxt_act_date) {
        this.nxt_act_date = nxt_act_date;
    }

    public String getNxt_act_time() {
        return nxt_act_time;
    }

    public void setNxt_act_time(String nxt_act_time) {
        this.nxt_act_time = nxt_act_time;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}