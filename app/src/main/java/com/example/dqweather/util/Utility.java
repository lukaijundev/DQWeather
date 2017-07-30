package com.example.dqweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.dqweather.db.City;
import com.example.dqweather.db.County;
import com.example.dqweather.db.Province;
import com.example.dqweather.gson.Weather;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/24.
 */

public class Utility {
    private static final String TAG = "Utility";

//    解析和处理服务器返回省级数据
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allprovinces=new JSONArray(response);
                for(int i=0;i<allprovinces.length();i++){
                    JSONObject provinceObject=allprovinces.getJSONObject(i);
                    Province province=new Province();

                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


//    解析和处理服务器返回到市级数据
    public static boolean handleCityReponse(String reponse,int provinceId){
        if(!TextUtils.isEmpty(reponse)){
            try {
                JSONArray allcities=new JSONArray(reponse);
                Log.d(TAG, "handleCityReponse: "+allcities);

                for(int i=0;i<allcities.length();i++){
                    JSONObject cityObject=allcities.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

//    解析和处理服务器返回的县级数据
    public static boolean handleCountryReponse(String reponse,int cityId){
        if(!TextUtils.isEmpty(reponse)){
            try {
                JSONArray allCounties=new JSONArray(reponse);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setCityId(cityId);
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();

                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static Weather handleWeatherResponse(String response){

        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;

    }


}
