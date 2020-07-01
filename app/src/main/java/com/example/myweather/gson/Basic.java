package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    //@SerializeName用于建立JSON与java字段之间的映射关系
    @SerializedName("location")
    public String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    @SerializedName("cid")
    public String weatherId;

}
