package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Lifestyle {
    public String type;
    public String txt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
