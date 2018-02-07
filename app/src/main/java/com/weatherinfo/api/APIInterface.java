package com.weatherinfo.api;

import com.weatherinfo.models.WeatherInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/data/2.5/weather?")
    Call<WeatherInfo> getCurrentWeather(@Query("q") String city, @Query("units") String units,
                                        @Query("appid") String apiKey);
}
