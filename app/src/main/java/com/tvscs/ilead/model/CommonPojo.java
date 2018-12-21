package com.tvscs.ilead.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonPojo {

    @SerializedName("Pincode")
    public String Pincode;
    //Lead Update
    String year, Make, Model;
    //Get ActionTime
    String key_cod, action_id, nxt_act_code;
    //Lead Followup
    String logged_user_id, prospect_no, prospec_no, branch_code, disbursedamount, logindate, disbusdate, agreement_no;
    String userid;
    private String KEY_ID, KEY_CODE, KEY_DESCRIPTION;
    private String statecode, CityCode, Towncode, DIRECT;
    //Get LeadsGiven
    private String user_id, lead_id, mobile;
    private LeadFollowupPojo objLms;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Town")
    @Expose
    private String town;
    private String Product;
    private QuickLeadResponse LMSobj;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDIRECT() {
        return DIRECT;
    }

    public void setDIRECT(String DIRECT) {
        this.DIRECT = DIRECT;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public LeadFollowupPojo getObjLms() {
        return objLms;
    }

    public void setObjLms(LeadFollowupPojo objLms) {
        this.objLms = objLms;
    }

    public String getKey_cod() {
        return key_cod;
    }

    public void setKey_cod(String key_cod) {
        this.key_cod = key_cod;
    }

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

    public String getNxt_act_code() {
        return nxt_act_code;
    }

    public void setNxt_act_code(String nxt_act_code) {
        this.nxt_act_code = nxt_act_code;
    }

    public String getLogged_user_id() {
        return logged_user_id;
    }

    public void setLogged_user_id(String logged_user_id) {
        this.logged_user_id = logged_user_id;
    }

    public String getProspect_no() {
        return prospect_no;
    }

    public void setProspect_no(String prospect_no) {
        this.prospect_no = prospect_no;
    }

    public String getProspec_no() {
        return prospec_no;
    }

    public void setProspec_no(String prospec_no) {
        this.prospec_no = prospec_no;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getDisbursedamount() {
        return disbursedamount;
    }

    public void setDisbursedamount(String disbursedamount) {
        this.disbursedamount = disbursedamount;
    }

    public String getLogindate() {
        return logindate;
    }

    public void setLogindate(String logindate) {
        this.logindate = logindate;
    }

    public String getDisbusdate() {
        return disbusdate;
    }

    public void setDisbusdate(String disbusdate) {
        this.disbusdate = disbusdate;
    }

    public String getAgreement_no() {
        return agreement_no;
    }

    public void setAgreement_no(String agreement_no) {
        this.agreement_no = agreement_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getTowncode() {
        return Towncode;
    }

    public void setTowncode(String towncode) {
        Towncode = towncode;
    }

    public String getKEY_ID() {
        return KEY_ID;
    }

    public void setKEY_ID(String KEY_ID) {
        this.KEY_ID = KEY_ID;
    }

    public String getKEY_CODE() {
        return KEY_CODE;
    }

    public void setKEY_CODE(String KEY_CODE) {
        this.KEY_CODE = KEY_CODE;
    }

    public String getKEY_DESCRIPTION() {
        return KEY_DESCRIPTION;
    }

    public void setKEY_DESCRIPTION(String KEY_DESCRIPTION) {
        this.KEY_DESCRIPTION = KEY_DESCRIPTION;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    @Override
    public String toString() {
        return this.KEY_DESCRIPTION; // Value to be displayed in the Spinner
    }

    public QuickLeadResponse getLMSobj() {
        return LMSobj;
    }

    public void setLMSobj(QuickLeadResponse LMSobj) {
        this.LMSobj = LMSobj;
    }
}
