package com.example.myweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myweather.gson.Air_now_city;
import com.example.myweather.gson.Forecast;
import com.example.myweather.gson.Lifestyle;
import com.example.myweather.gson.Weather;
import com.example.myweather.service.AutoUpdateService;
import com.example.myweather.util.HttpUtil;
import com.example.myweather.util.Utility;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Button navButton;

    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;//记录刷新时保存的id

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private LinearLayout lifestyleLayout;
    private TextView aqiText;
    private TextView pm25Text;

    private ImageView bingPicImg;
    //SharedPreferences.Editor editor;
    //SharedPreferences.Editor editor1;
    //SharedPreferences.Editor editor2;
    //SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
    //SharedPreferences.Editor editor1=getSharedPreferences("suggestionList",MODE_PRIVATE).edit();
    //SharedPreferences.Editor editor2=getSharedPreferences("forecastList",MODE_PRIVATE).edit();
    private Weather weather=new Weather();
    private Weather weather1x=new Weather();
    private Weather weather11;
    private Weather weather22;
    private Weather weather33;
    private Weather weather44;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化各控件
        weatherLayout =findViewById(R.id.weather_layout);
        titleCity=findViewById(R.id.title_city);
        titleUpdateTime=findViewById(R.id.title_update_time);
        degreeText=findViewById(R.id.degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        forecastLayout=findViewById(R.id.forecast_layout);
        aqiText=findViewById(R.id.aqi_text);
        pm25Text=findViewById(R.id.pm25_text);
        lifestyleLayout=findViewById(R.id.lifesytle_layout);
        bingPicImg=findViewById(R.id.bing_pic_img);
        drawerLayout=findViewById(R.id.drawer_layout);
        navButton=findViewById(R.id.nav_button);
        swipeRefresh=findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


        /*SharedPreferences prefs1=getSharedPreferences("suggestionList",Context.MODE_PRIVATE);
        String json1=prefs1.getString("suggsetionListJson",null);
        List<Lifestyle> lifestyles;
        Gson gson1=new Gson();
        lifestyles=gson1.fromJson(json1,new TypeToken<List<Lifestyle>>(){}.getType());

        SharedPreferences prefs2=getSharedPreferences("forecastListList",Context.MODE_PRIVATE);
        String json2=prefs2.getString("forecastListJson",null);
        List<Forecast> forecasts;
        Gson gson2=new Gson();
        forecasts=gson2.fromJson(json2,new TypeToken<List<Forecast>>(){}.getType());



        weather5.setStatus(prefs.getString("status",null));
        weather5.air_now_city.setPm25(prefs.getString("pm25",null));
        weather5.air_now_city.setAqi(prefs.getString("aqi",null));
        weather5.basic.setWeatherId(prefs.getString("weatherId",null));
        weather5.basic.setCityName(prefs.getString("cityName",null));
        weather5.update.setUpdateTime(prefs.getString("updateTime",null));
        weather5.now.setTemperature(prefs.getString("temperature",null));
        weather5.now.setMore((prefs.getString("more",null)));
        weather5.setSuggestionList(lifestyles);
        weather5.setForecastList(forecasts);*/
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存时直接解析天气数据
            Weather weather5= Utility.handleWeatherResponse(weatherString,1);
            mWeatherId=weather5.basic.weatherId;
            showWeatherInfo(weather5);
        }else{
            //无缓存时去服务器查询天气
            mWeatherId=getIntent().getStringExtra("weather_id");
            String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);

            requestWeather(weatherId);
        }

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        String bingPic=prefs.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
    }

    //根据天气id请求城市天气信息
    public void requestWeather(final String weatherId){
        mWeatherId=weatherId;
        //请求basic和aqi
        String basicUrl="https://free-api.heweather.net/s6/air/now?location="+weatherId+"&key=4e370c9c0f024db2934b9bae83930f17";
        HttpUtil.sendOkHttpRequest(basicUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText1=response.body().string();
                final Weather weather1=Utility.handleWeatherResponse(responseText1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather1!=null&&"ok".equals(weather1.status)){
                            //SharedPreferences.Editor editor1= PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //editor1.putString("weather",responseText1);
                            //editor1.apply();
                            //showWeatherInfo(weather1);
                            i=i+1;
                            chooseweather(weather1,null,null,null,i);

                        }else {


                            String x1="未知(免费版只提供部分热门城市)";
                            Air_now_city city=new Air_now_city();
                            city.setAqi(x1);
                            city.setPm25(x1);
                            weather1x.setAir_now_city(city);
                            i=i+1;
                            chooseweather(weather1x,null,null,null,i);
                        }
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败1",Toast.LENGTH_SHORT).show();
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        //请求now
        String nowUrl="https://free-api.heweather.net/s6/weather/now?location="+weatherId+"&key=4e370c9c0f024db2934b9bae83930f17";
        HttpUtil.sendOkHttpRequest(nowUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText2=response.body().string();
                final Weather weather2=Utility.handleWeatherResponse(responseText2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather2!=null&&"ok".equals(weather2.status)){
                            //SharedPreferences.Editor editor2=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //editor2.putString("weather",responseText2);
                            //editor2.apply();
                            //showWeatherInfo(weather2);
                            i=i+1;
                            chooseweather(null,weather2,null,null,i);
                        }else {

                            Toast.makeText(WeatherActivity.this,"获取天气信息失败2",Toast.LENGTH_SHORT).show();
                        }
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(WeatherActivity.this,"获取天气信息失败2",Toast.LENGTH_SHORT).show();
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        //请求lifestyle
        String lifestyleUrl="https://free-api.heweather.net/s6/weather/lifestyle?location="+weatherId+"&key=4e370c9c0f024db2934b9bae83930f17";
        HttpUtil.sendOkHttpRequest(lifestyleUrl, new Callback() {
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String responseText3=response.body().string();
                final Weather weather3=Utility.handleWeatherResponse(responseText3);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather3!=null&&"ok".equals(weather3.status)){
                            //SharedPreferences.Editor editor3=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //editor3.putString("weather",responseText3);
                            //editor3.apply();
                            //showWeatherInfo(weather3);
                            i=i+1;
                            chooseweather(null,null,weather3,null,i);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败3",Toast.LENGTH_SHORT).show();
                        }
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败3",Toast.LENGTH_SHORT).show();
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        //请求daily_forecast
        String daily_forecastUrl="https://free-api.heweather.net/s6/weather/forecast?location="+weatherId+"&key=4e370c9c0f024db2934b9bae83930f17";
        HttpUtil.sendOkHttpRequest(daily_forecastUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText4=response.body().string();
                final Weather weather4=Utility.handleWeatherResponse(responseText4);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather4!=null&&"ok".equals(weather4.status)){
                            //SharedPreferences.Editor editor2=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //editor2.putString("weather",responseText4);
                            //editor2.apply();
                            //showWeatherInfo(weather4);
                            i=i+1;
                            chooseweather(null,null,null,weather4,i);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败4",Toast.LENGTH_SHORT).show();
                        }
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败4",Toast.LENGTH_SHORT).show();
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });


        //if(v1==1&&v2==1&&v3==1&&v4==1){


        //}
        loadBingPic();
    }

    private void chooseweather(Weather weather1,Weather weather2,Weather weather3,Weather weather4,int i2){
        if(weather1!=null){
            weather11=weather1;
            weather.setAir_now_city(weather1.air_now_city);
            //weather.air_now_city.pm25=weather1.air_now_city.pm25;
            //weather.air_now_city.aqi=weather1.air_now_city.aqi;

            //weather.basic.weatherId=weather1.basic.weatherId;
            //weather.basic.cityName=weather1.basic.cityName;

            //weather.update.updateTime=weather1.update.updateTime;
            /*editor.putString("status",weather1.status);
            editor.putString("pm25",weather1.air_now_city.pm25);
            editor.putString("aqi",weather1.air_now_city.aqi);
            editor.putString("weatherId",weather1.basic.weatherId);
            editor.putString("cityName",weather1.basic.cityName);
            editor.putString("updateTime",weather1.update.updateTime);*/
        }
        if(weather2!=null){
            weather22=weather2;
            weather.setStatus(weather2.status);
            weather.setNow(weather2.now);
            weather.setBasic(weather2.basic);
            weather.setUpdate(weather2.update);
            //weather.now.temperature=weather2.now.temperature;
            //weather.now.more=weather2.now.more;
            //editor.putString("temperature",weather2.now.temperature);
            //editor.putString("more",weather2.now.more);
        }
        if(weather3!=null){
            weather33=weather3;
            weather.setSuggestionList(weather3.suggestionList);
            //weather.suggestionList=weather3.suggestionList;
            //Gson gson=new Gson();
            //String json1=gson.toJson(weather3.suggestionList);
            //Log.d(null,"saved json is"+json1);
            //editor1.putString("suggestiongList",json1);
        }
        if(weather4!=null){
            weather44=weather4;
            weather.setForecastList(weather4.forecastList);
            //Gson gson=new Gson();
            //String json2=gson.toJson(weather4.forecastList);
            //Log.d(null,"saved json is"+json2);
            //editor2.putString("forecastList",json2);
            //weather.forecastList=weather4.forecastList;
        }
        if(i>=4){
            Gson gson=new Gson();
            String jsonStr=gson.toJson(weather);
            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
            editor.putString("weather",jsonStr);
            editor.apply();
            //editor.apply();
            //editor1.apply();
            //editor2.apply();
            showWeatherInfo(weather);
            //showWeatherInfo(weather11,weather22,weather33,weather44);
            swipeRefresh.setRefreshing(false);
        }
        loadBingPic();
    }

    //处理并展示Weather实体类中的数据(重写)
    /*private void showWeatherInfo(Weather weather1,Weather weather2,Weather weather3,Weather weather4){
        if(weather1!=null&&"ok".equals(weather1.status)&&weather2!=null&&"ok".equals(weather2.status)&&weather3!=null&&"ok".equals(weather3.status)&&weather4!=null&&"ok".equals(weather4.status)){
            String cityName=weather1.basic.cityName;
            String updateTime=weather1.update.updateTime.split(" ")[1];
            String degree=weather2.now.temperature+"°C";
            String weatherInfo=weather2.now.more;
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);
            forecastLayout.removeAllViews();
            lifestyleLayout.removeAllViews();
            for (Forecast forecast:weather4.forecastList){
                View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
                TextView dateText=view.findViewById(R.id.date_text);
                TextView infoText=view.findViewById(R.id.info_text);
                TextView maxText=view.findViewById(R.id.max_text);
                TextView minText=view.findViewById(R.id.min_text);
                dateText.setText(forecast.date);
                infoText.setText(forecast.cond_txt_n);
                maxText.setText(forecast.tmp_max);
                minText.setText(forecast.tmp_min);
                forecastLayout.addView(view);
            }
            if(weather1.air_now_city!=null){
                aqiText.setText(weather1.air_now_city.aqi);
                pm25Text.setText(weather1.air_now_city.pm25);
            }
            for (Lifestyle suggestion:weather3.suggestionList){
                View view1= LayoutInflater.from(this).inflate(R.layout.suggestion_item,lifestyleLayout,false);
                TextView lifesytle=view1.findViewById(R.id.lifesytle_text);
                lifesytle.setText(suggestion.txt);
                lifestyleLayout.addView(view1);
            }
            weatherLayout.setVisibility(View.VISIBLE);
            //Intent intent=new Intent(this, AutoUpdateService.class);
            //startService(intent);
        }else{
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }
    }*/

    //处理并展示Weather实体类中的数据
    private void showWeatherInfo(Weather weather){
        if(weather!=null&&"ok".equals(weather.status)){
            String cityName=weather.basic.cityName;
            String updateTime=weather.update.updateTime.split(" ")[1];
            String degree=weather.now.temperature+"°C";
            String weatherInfo=weather.now.more;
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);
            forecastLayout.removeAllViews();
            lifestyleLayout.removeAllViews();
            for (Forecast forecast:weather.forecastList){
                View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
                TextView dateText=view.findViewById(R.id.date_text);
                TextView infoText=view.findViewById(R.id.info_text);
                TextView maxText=view.findViewById(R.id.max_text);
                TextView minText=view.findViewById(R.id.min_text);
                dateText.setText(forecast.date);
                infoText.setText(forecast.cond_txt_n);
                maxText.setText(forecast.tmp_max);
                minText.setText(forecast.tmp_min);
                forecastLayout.addView(view);
            }
            if(weather.air_now_city!=null){
                aqiText.setText(weather.air_now_city.aqi);
                pm25Text.setText(weather.air_now_city.pm25);
            }
            for (Lifestyle suggestion:weather.suggestionList){
                View view= LayoutInflater.from(this).inflate(R.layout.suggestion_item,lifestyleLayout,false);
                TextView lifesytle=view.findViewById(R.id.lifesytle_text);
                lifesytle.setText(suggestion.txt);
                lifestyleLayout.addView(view);
            }
            weatherLayout.setVisibility(View.VISIBLE);
            //Intent intent=new Intent(this, AutoUpdateService.class);
            //startService(intent);
        }else{
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }
        loadBingPic();
    }

    //加载必应每日一图
    private void loadBingPic(){
        String requsetBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requsetBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

}