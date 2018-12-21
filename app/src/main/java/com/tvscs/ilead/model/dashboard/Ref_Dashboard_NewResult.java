package com.tvscs.ilead.model.dashboard;

import java.io.Serializable;

public class Ref_Dashboard_NewResult implements Serializable {
    private String Ctwo;

    private String Cfour;

    private String Product;

    private String Cone;

    private String Mobile;

    private String UserName;

    private String Czero;

    private String Cthree;

    private String Productdesc;

    public String getProductdesc() {
        return Productdesc;
    }

    public void setProductdesc(String productdesc) {
        Productdesc = productdesc;
    }

    public String getCtwo() {
        return Ctwo;
    }

    public void setCtwo(String Ctwo) {
        this.Ctwo = Ctwo;
    }

    public String getCfour() {
        return Cfour;
    }

    public void setCfour(String Cfour) {
        this.Cfour = Cfour;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String Product) {
        this.Product = Product;
    }

    public String getCone() {
        return Cone;
    }

    public void setCone(String Cone) {
        this.Cone = Cone;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getCzero() {
        return Czero;
    }

    public void setCzero(String Czero) {
        this.Czero = Czero;
    }

    public String getCthree() {
        return Cthree;
    }

    public void setCthree(String Cthree) {
        this.Cthree = Cthree;
    }

    @Override
    public String toString() {
        return "ClassPojo [Ctwo = " + Ctwo + ", Cfour = " + Cfour + ", Product = " + Product + ", Cone = " + Cone + ", Mobile = " + Mobile + ", UserName = " + UserName + ", Czero = " + Czero + ", Cthree = " + Cthree + "]";
    }
}

