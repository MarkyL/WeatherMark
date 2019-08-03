package com.mark.weathermark.viewmodel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mark.weathermark.model.forecast.CityForecast;
import com.mark.weathermark.repository.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mark.weathermark.BuildConfig.WEATHER_API_KEY;

public class CityViewModel extends ViewModel {

    private static final String TAG = "CityViewModel";
    @Nullable
    private MutableLiveData<CityForecast> cityForecast;
    
    public MutableLiveData<CityForecast> getCityForecast(final String cityID) {
        cityForecast = new MutableLiveData<>();

        Call<CityForecast> call = NetworkClient.weatherService.getForecastByCityID(WEATHER_API_KEY, cityID);
        call.enqueue(new Callback<CityForecast>() {
            @Override
            public void onResponse(Call<CityForecast> call, Response<CityForecast> response) {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                if (response.isSuccessful() && response.body() != null) {
                    cityForecast.postValue(response.body());
                } else {
                    onFailure(call, new Throwable());
                }
            }

            @Override
            public void onFailure(Call<CityForecast> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

        return cityForecast;
    }
}
