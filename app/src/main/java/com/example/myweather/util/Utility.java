package com.example.myweather.util;

import android.text.TextUtils;

import com.example.myweather.db.City;
import com.example.myweather.db.County;
import com.example.myweather.db.Province;
import com.example.myweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    //解析和处理服务器返回的省级数据
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                //解析数据
                JSONObject jsonObject=new JSONObject(response);
                JSONArray allProvinces=jsonObject.getJSONArray("provincelist");
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    //新建实体表存放服务器返回来的数据
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("province"));
                    province.setProvinceCode(provinceObject.getInt("pid"));
                    //存储到数据库中
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析和处理服务器返回的市级数据
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                //解析数据
                JSONObject jsonObject=new JSONObject(response);
                JSONArray allCities=jsonObject.getJSONArray("citylist");
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    //新建实体表存放服务器返回来的数据
                    City city=new City();
                    city.setCityName(cityObject.getString("city"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    //存储到数据库中
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析和处理服务器返回的县级数据
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                //解析数据
                JSONObject jsonObject=new JSONObject(response);
                JSONArray allCounties=jsonObject.getJSONArray("countylist");
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    //新建实体表存放服务器返回来的数据
                    County county=new County();
                    county.setCountyName(countyObject.getString("city"));
                    county.setWeatherId(countyObject.getString("id"));
                    county.setCountyNameen(countyObject.getString("en"));
                    county.setCityId(cityId);
                    //存储到数据库中
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    //将返回的JSON数据解析成Weather实体类
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
            //return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //有缓存的转换，重写上述方法
    public static Weather handleWeatherResponse(String response,int i){
        try {
            JSONObject jsonObject=new JSONObject(response);
            String weatherContent=jsonObject.toString();
            return new Gson().fromJson(weatherContent,Weather.class);
            //return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
