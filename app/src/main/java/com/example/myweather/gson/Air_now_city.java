package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Air_now_city {
    public String aqi;
    public String pm25;

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
}
