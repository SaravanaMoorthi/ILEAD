package com.tvscs.ilead.model;

public class MainMenu {
    private String menuName;
    private int imageId;
    //Version Update
    private String AppName1, Version;

    public String getAppName1() {
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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
