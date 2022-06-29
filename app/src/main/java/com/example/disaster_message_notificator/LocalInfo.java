package com.example.disaster_message_notificator;

public class LocalInfo {
    private String local_name;
    private String latitude;
    private String longitude;

    public LocalInfo() {
        super();
    }

    public LocalInfo(String local_name, String latitude, String longitude) {
        this.local_name = local_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocal_name() {
        return local_name;
    }

    public void setLocal_name(String local_name) {
        this.local_name = local_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
