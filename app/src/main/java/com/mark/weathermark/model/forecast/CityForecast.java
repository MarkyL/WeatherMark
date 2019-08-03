package com.mark.weathermark.model.forecast;

import com.google.gson.annotations.SerializedName;
import com.mark.weathermark.model.weather.CityWeather;

import java.util.List;

public class CityForecast {
    @SerializedName("city")
    public City city;
    @SerializedName("list")
    public List<CityWeather> forecastsList = null;

    public CityForecast() {
    }

    public CityForecast(City city, List<CityWeather> forecasts) {
        this.city = city;
        this.forecastsList = forecasts;
    }

    //region getters/setters

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<CityWeather> getForecastsList() {
        return forecastsList;
    }

    public void setForecastsList(List<CityWeather> forecastsList) {
        this.forecastsList = forecastsList;
    }

    //endregion
}
