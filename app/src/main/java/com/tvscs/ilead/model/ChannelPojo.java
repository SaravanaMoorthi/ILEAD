package com.tvscs.ilead.model;

public class ChannelPojo {

    private String channelId;
    private String channelName;

    public ChannelPojo(String id, String name) {
        this.channelId = id;
        this.channelName = name;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return channelName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChannelPojo) {
            ChannelPojo c = (ChannelPojo) obj;
            if (c.getChannelName().equals(channelName) && c.getChannelId() == channelId)
                return true;
        }

        return false;
    }

}
