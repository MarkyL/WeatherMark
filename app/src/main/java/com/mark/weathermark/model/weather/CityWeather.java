package com.mark.weathermark.model.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityWeather {

    @SerializedName(value = "weather", alternate = "list")
    public List<Weather> weather = null;
    @SerializedName("main")
    public MinMaxTemperatures minMaxTemperatures;
    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public int id;
    @SerializedName("dt")
    public Integer dt;

    public CityWeather() {
    }

    public CityWeather(List<Weather> weather, MinMaxTemperatures minMaxTemperatures, String name, int id, Integer dt) {
        this.weather = weather;
        this.minMaxTemperatures = minMaxTemperatures;
        this.name = name;
        this.id = id;
        this.dt = dt;
    }

    //region getters/setters

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public MinMaxTemperatures getMinMaxTemperatures() {
        return minMaxTemperatures;
    }

    public void setMinMaxTemperatures(MinMaxTemperatures minMaxTemperatures) {
        this.minMaxTemperatures = minMaxTemperatures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription(){
        return this.weather.get(0).getDescription();
    }

    public Integer getTime() {
        return dt;
    }

    public void setTime(Integer dt) {
        this.dt = dt;
    }

    //endregion

    @Override
    public String toString() {
        return "CityWeather{" +
                "weather=" + weather +
                ", minMaxTemperatures=" + minMaxTemperatures +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

