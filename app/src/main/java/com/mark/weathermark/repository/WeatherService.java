package com.mark.weathermark.repository;

import com.mark.weathermark.model.GroupForecastResponse;
import com.mark.weathermark.model.forecast.CityForecast;
import com.mark.weathermark.model.weather.CityWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    // example : api.openweathermap.org/data/2.5/weather?id=2172797
    @GET("weather?units=metric&")
    Call<CityWeather> getWeatherByCityID(@Query("APPID") String appID, @Query("id") String cityID);

    // Call for several city IDs
    // example: http://api.openweathermap.org/data/2.5/group?id=524901,703448,2643743&units=metric
    @GET("group?units=metric&")
    Call<GroupForecastResponse> getWeatherByCitiesIDs(@Query("APPID") String appID, @Query("id") String citiesIDs);

    // example : api.openweathermap.org/data/2.5/weather?q={city name}
    @GET("weather?units=metric&")
    Call<CityWeather> getWeatherByCityName(@Query("APPID") String appID, @Query("q") String cityName);

    @GET("forecast?units=metric&")
    Call<CityForecast> getForecastByCityID(@Query("APPID") String appID, @Query("id") String cityID);
}
