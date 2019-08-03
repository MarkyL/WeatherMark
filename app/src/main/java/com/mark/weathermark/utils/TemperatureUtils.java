package com.mark.weathermark.utils;

import com.mark.weathermark.enums.UnitsType;

public class TemperatureUtils {

    public static double convertTemperatureToGivenUnit(final double temperature, UnitsType givenUnitsType) {
        return givenUnitsType == UnitsType.METRIC ?
             convertFahrenheitToCelsius(temperature) : convertCelsiusToFahrenheit(temperature);
    }

    public static double convertCelsiusToFahrenheit(final double celsiusTemperature) {
        return (celsiusTemperature * (9/5f)) + 32;
    }

    public static double convertFahrenheitToCelsius(final double fahrenheitTemperature) {
        return (fahrenheitTemperature - 32) * (5/9f);
    }
}
