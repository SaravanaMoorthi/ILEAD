package com.tvscs.ilead.model.homechartmodel;

import java.io.Serializable;

public class Lstuserdtls  implements Serializable {
    private String Userid;

    private String UserName;

    public String getUserid ()
    {
        return Userid;
    }

    public void setUserid (String Userid)
    {
        this.Userid = Userid;
    }

    public String getUserName ()
    {
        return UserName;
    }

    public void setUserName (String UserName)
    {
        this.UserName = UserName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Userid = "+Userid+", UserName = "+UserName+"]";
    }
}

