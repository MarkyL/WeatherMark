package com.mark.weathermark.model.weather;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("id")
    public Integer id;
    @SerializedName("description")
    public String description;
    @SerializedName("icon")
    public String icon;

    public Weather() {
    }

    public Weather(Integer id, String description, String icon) {
        this.id = id;
        this.description = description;
        this.icon = icon;
    }

    //region getters/setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    //endregion
}
