package com.tvscs.ilead.model;

public class NotificationPojo {

    public String lead_no, status, first_name, dateTime, Next_Action;

    public NotificationPojo(String lead_no, String status, String first_name, String Next_Action) {
        this.lead_no = lead_no;
        this.status = status;
        this.first_name = first_name;
        this.Next_Action = Next_Action;
    }

    public String getNext_Action() {
        return Next_Action;
    }

    public void setNext_Action(String next_Action) {
        Next_Action = next_Action;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLead_no() {
        return lead_no;
    }

    public void setLead_no(String lead_no) {
        this.lead_no = lead_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
}
