package com.mark.weathermark.model.forecast;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;

    public City() {
    }

    public City(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    //region getters/setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //endregion
}
