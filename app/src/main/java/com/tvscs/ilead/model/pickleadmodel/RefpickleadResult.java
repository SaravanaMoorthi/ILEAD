package com.tvscs.ilead.model.pickleadmodel;

import java.io.Serializable;

public class RefpickleadResult implements Serializable {
    private String created_by;

    private String first_name;

    private String Product;

    private String status;

    private String phone_mobile1;

    private String created_on;

    private String state;

    private String taluk;

    private String lead_id;

    private String city;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String Product) {
        this.Product = Product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone_mobile1() {
        return phone_mobile1;
    }

    public void setPhone_mobile1(String phone_mobile1) {
        this.phone_mobile1 = phone_mobile1;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "ClassPojo [created_by = " + created_by + ", first_name = " + first_name + ", Product = " + Product + ", status = " + status + ", phone_mobile1 = " + phone_mobile1 + ", created_on = " + created_on + ", state = " + state + ", taluk = " + taluk + ", lead_id = " + lead_id + ", city = " + city + "]";
    }
}

