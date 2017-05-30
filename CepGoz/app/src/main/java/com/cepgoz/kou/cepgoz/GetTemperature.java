package com.cepgoz.kou.cepgoz;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by faruk on 12.6.2016.
 */
public class GetTemperature {

    private Handler handler;
    private Context ctx;
    String cityField;
    String updatedField;
    String detailsField;
    String currentTemperatureField;
    String cityName;
    public GetTemperature(Context context,String _cityName){
        handler = new Handler();
        cityName=_cityName;
        ctx=context;
        updateWeatherData(_cityName);
    }
    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = FetchWeather.getJSON(ctx, city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(ctx,
                                    ctx.getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            cityField=json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country");

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsField=
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa";

            currentTemperatureField=
                    String.format("%.2f", main.getDouble("temp"))+ " ℃";

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField="Last update: " + updatedOn;

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    public String getCityField(){
        return cityField;
    }

    public String getDetailsField() {
        return detailsField;
    }

    public String getCurrentTemperatureField() {
        updateWeatherData(cityName);
        return currentTemperatureField;
    }

    public String getUpdatedField() {
        return updatedField;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
