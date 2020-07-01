package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public String date;
    public String cond_txt_n;
    public String tmp_max;
    public String tmp_min;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCond_txt_n() {
        return cond_txt_n;
    }

    public void setCond_txt_n(String cond_txt_n) {
        this.cond_txt_n = cond_txt_n;
    }

    public String getTmp_max() {
        return tmp_max;
    }

    public void setTmp_max(String tmp_max) {
        this.tmp_max = tmp_max;
    }

    public String getTmp_min() {
        return tmp_min;
    }

    public void setTmp_min(String tmp_min) {
        this.tmp_min = tmp_min;
    }
}
