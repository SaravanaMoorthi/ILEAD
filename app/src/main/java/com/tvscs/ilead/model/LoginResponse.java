package com.tvscs.ilead.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("userid")
    public String userid;
    @SerializedName("password")
    public String password;
    @SerializedName("imei_number")
    public String imei_number;
    @SerializedName("ipaddress")
    public String ipaddress;
    @SerializedName("outErrorCode")
    public String outErrorCode;
    @SerializedName("outErrorMsg")
    public String outErrorMsg;

    @SerializedName("email")
    public String email;

    public String user_id;

/*
    @SerializedName("AppName1")
    public String AppName1;
    @SerializedName("Version")
    public String Version;
*/

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* public String getAppName1() {
         return AppName1;
     }

     public void setAppName1(String appName1) {
         AppName1 = appName1;
     }

     public String getVersion() {
         return Version;
     }

     public void setVersion(String version) {
         Version = version;
     }
 */
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImei_number() {
        return imei_number;
    }

    public void setImei_number(String imei_number) {
        this.imei_number = imei_number;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getOutErrorCode() {
        return outErrorCode;
    }

    public void setOutErrorCode(String outErrorCode) {
        this.outErrorCode = outErrorCode;
    }

    public String getOutErrorMsg() {
        return outErrorMsg;
    }

    public void setOutErrorMsg(String outErrorMsg) {
        this.outErrorMsg = outErrorMsg;
    }
}
