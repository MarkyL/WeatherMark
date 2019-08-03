package com.mark.weathermark.model;

import com.google.gson.annotations.SerializedName;
import com.mark.weathermark.model.weather.CityWeather;

import java.util.List;

public class GroupForecastResponse {

    @SerializedName("list")
    public List<CityWeather> list = null;

    public GroupForecastResponse() {
    }

    public GroupForecastResponse(List<CityWeather> list) {
        this.list = list;
    }

    //region getters/setters

    public List<CityWeather> getList() {
        return list;
    }

    public void setList(List<CityWeather> list) {
        this.list = list;
    }

    //endregion
}
