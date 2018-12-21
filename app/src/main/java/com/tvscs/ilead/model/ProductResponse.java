package com.tvscs.ilead.model;

public class ProductResponse {

    private String szproductcode, szdesc;
    private boolean isSelected;

    public ProductResponse(String productCode, String productDesc) {
        this.szproductcode = productCode;
        this.szdesc = productDesc;
    }

    public String getSzproductcode() {
        return szproductcode;
    }

    public void setSzproductcode(String szproductcode) {
        this.szproductcode = szproductcode;
    }

    public String getSzdesc() {
        return szdesc;
    }

    public void setSzdesc(String szdesc) {
        this.szdesc = szdesc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
