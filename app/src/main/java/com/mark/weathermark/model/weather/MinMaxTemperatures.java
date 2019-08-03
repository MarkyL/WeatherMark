package com.mark.weathermark.model.weather;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

public class MinMaxTemperatures {

    @SerializedName("temp_min")
    public Double tempMin;
    @SerializedName("temp_max")
    public Double tempMax;

    public MinMaxTemperatures() {
    }

    public MinMaxTemperatures(Double tempMin, Double tempMax) {
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    //region getters/setters

    public Double getTempMin() {
        return tempMin;
    }

    @SuppressLint("DefaultLocale")
    public String getTempMinStr() {
        return String.format("%.0f", tempMin);
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    @SuppressLint("DefaultLocale")
    public String getTempMaxStr() {
        return String.format("%.0f", tempMax);
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    //endregion
}
