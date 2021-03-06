package com.example.myweather.db;

import org.litepal.crud.DataSupport;
//存放县的数据表
public class County extends DataSupport {
    private int id;//每个实体类中应有的字段
    private String countyName;//县的名字
    private String countyNameen;

    public String getCountyNameen() {
        return countyNameen;
    }

    public void setCountyNameen(String countyNameen) {
        this.countyNameen = countyNameen;
    }

    private String weatherId;//县对应的天气id
    private int cityId;//当前县所属市的id值

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(String weatherId) {
        this.weatherId = "CN"+weatherId;
    }

    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
