package com.mark.weathermark.callback;

import com.mark.weathermark.model.weather.CityWeather;

public interface CityClickCallback {
    void onCityClick(final CityWeather cityWeather, final int position);
}
