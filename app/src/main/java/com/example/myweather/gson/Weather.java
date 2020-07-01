package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public Basic basic;
    public Air_now_city air_now_city;
    public Now now;
    public Update update;
    public String status;
    @SerializedName("lifestyle")
    public List<Lifestyle> suggestionList;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
    public Basic getBasic() {
        return basic;
    }
    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Air_now_city getAir_now_city() {
        return air_now_city;
    }
    public void setAir_now_city(Air_now_city air_now_city) {
        this.air_now_city = air_now_city;
    }

    public Now getNow() {
        return now;
    }
    public void setNow(Now now) {
        this.now = now;
    }

    public Update getUpdate() {
        return update;
    }
    public void setUpdate(Update update) {
        this.update = update;
    }

    public List<Lifestyle> getSuggestionList() {
        return suggestionList;
    }
    public void setSuggestionList(List<Lifestyle> suggestionList) {
        this.suggestionList = suggestionList;
    }

    public List<Forecast> getForecastList() {
        return forecastList;
    }
    public void setForecastList(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }


}
