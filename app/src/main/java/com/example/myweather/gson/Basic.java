package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    //@SerializeName用于建立JSON与java字段之间的映射关系
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
