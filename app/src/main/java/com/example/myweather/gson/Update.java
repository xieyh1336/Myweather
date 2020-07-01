package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Update {
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @SerializedName("loc")
    public String updateTime;
}
