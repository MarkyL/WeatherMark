package com.mark.weathermark.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mark.weathermark.model.GroupForecastResponse;
import com.mark.weathermark.model.weather.CityWeather;
import com.mark.weathermark.repository.NetworkClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mark.weathermark.BuildConfig.WEATHER_API_KEY;

public class MainViewModel extends ViewModel {
    // extend AndroidViewModel if need Application Context

    private static final String TAG = "MainViewModel";

    @Nullable
    private MutableLiveData<List<CityWeather>> mMainCitiesWeather;

    public MutableLiveData<List<CityWeather>> getMainCitiesWeather() {
        mMainCitiesWeather = new MutableLiveData<>();

        Call<GroupForecastResponse> call =
                NetworkClient.weatherService.getWeatherByCitiesIDs(WEATHER_API_KEY, generatePredefinedMainCitiesIDsMock());
        call.enqueue(new Callback<GroupForecastResponse>() {
            @Override
            public void onResponse(Call<GroupForecastResponse> call, Response<GroupForecastResponse> response) {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                if (response.isSuccessful() && response.body() != null) {
                    mMainCitiesWeather.setValue(response.body().list);
                } else {
                    onFailure(call, new Throwable());
                }
            }

            @Override
            public void onFailure(Call<GroupForecastResponse> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                mMainCitiesWeather.setValue(null);
            }
        });

        return mMainCitiesWeather;
    }

    public MutableLiveData<CityWeather> getCityWeather(final String cityName) {
        final MutableLiveData<CityWeather> cityWeather = new MutableLiveData<>();

        final Call<CityWeather> call = NetworkClient.weatherService.getWeatherByCityName(WEATHER_API_KEY, cityName);
        call.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cityWeather.postValue(response.body());
                } else {
                    onFailure(call, new Throwable());

                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                cityWeather.postValue(null);
            }
        });

        return cityWeather;
    }

    public String generatePredefinedMainCitiesIDsMock() {
        List<String> idsList = new ArrayList<>();
        idsList.add("5506956"); // Las Vegas
        idsList.add("3701484"); // San Francisco
        idsList.add("5128638"); // New York
        idsList.add("5809844"); // Seattle
        idsList.add("4542692"); // Miami

        return TextUtils.join(",", idsList);
    }
}
