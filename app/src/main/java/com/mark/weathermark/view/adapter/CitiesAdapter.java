package com.mark.weathermark.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.mark.weathermark.R;
import com.mark.weathermark.callback.CityClickCallback;
import com.mark.weathermark.databinding.CityAdapterItemBinding;
import com.mark.weathermark.enums.UnitsType;
import com.mark.weathermark.model.weather.CityWeather;
import com.mark.weathermark.model.weather.MinMaxTemperatures;
import com.mark.weathermark.utils.TemperatureUtils;
import com.mark.weathermark.view.adapter.viewholder.BaseViewHolder;

public class CitiesAdapter extends BaseAdapter<CityWeather, BaseViewHolder> {

    private static final String TAG = "CitiesAdapter";

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    @Nullable
    private final CityClickCallback mCityCallback;

    public CitiesAdapter(Context context, @Nullable CityClickCallback callback) {
        super(context);
        mCityCallback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                CityAdapterItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.city_adapter_item, parent, false);
                binding.setCallback(mCityCallback);
                return new CityViewHolder(binding);

            default:
                //TODO: put here loading or footer, not item.
                binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.city_adapter_item, parent, false);
                binding.setCallback(mCityCallback);
                return new CityViewHolder(binding);
        }
    }

    @Override
    public long getItemId(int position) {
        return mDataSet.get(position).getId();
    }

    public void toggleTemperatureUnits() {
        toggleUnitsType();
        for (int i = 0; i < mDataSet.size(); i++) {
            CityWeather currentCity = mDataSet.get(i);
            MinMaxTemperatures minMaxTemperatures = currentCity.getMinMaxTemperatures();
            double newMinTemp = TemperatureUtils.convertTemperatureToGivenUnit(minMaxTemperatures.getTempMin(), mCurrentUnitsType);
            double newMaxTemp = TemperatureUtils.convertTemperatureToGivenUnit(minMaxTemperatures.getTempMax(), mCurrentUnitsType);
            MinMaxTemperatures newMinMax = new MinMaxTemperatures(newMinTemp, newMaxTemp);
            currentCity.setMinMaxTemperatures(newMinMax);
            updateDataItem(currentCity, i);
        }
    }

    /**
     * toggles the current displayed units type:
     * if it was metric (celsius), now it will be imperial (fahrenheit)
     * and vice versa.
     */
    private void toggleUnitsType() {
        mCurrentUnitsType =
                mCurrentUnitsType == UnitsType.METRIC ? UnitsType.IMPERIAL : UnitsType.METRIC;
    }

    //region city view holder

    public class CityViewHolder extends BaseViewHolder {

        final CityAdapterItemBinding binding;

        public CityViewHolder(CityAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        protected void clear() {
            binding.weatherIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.placeholder));
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            CityWeather cityWeather = mDataSet.get(position);

            binding.setCityWeather(cityWeather);
            binding.setPosition(position);
            binding.executePendingBindings();

            setMinMaxTemperatures(cityWeather);
            setCityWeatherIcon(cityWeather);
        }

        private void setMinMaxTemperatures(CityWeather cityWeather) {
            Resources resources = CitiesAdapter.this.mContext.getResources();
            MinMaxTemperatures minMaxTemperatures = cityWeather.getMinMaxTemperatures();

            String temperaturesText = String.format(resources.getString(mCurrentUnitsType == UnitsType.METRIC ?
                    R.string.celsius_formatter : R.string.fahrenheit_formatter), minMaxTemperatures.getTempMinStr(), minMaxTemperatures.getTempMaxStr());

            binding.temperatureTv.setText(temperaturesText);
        }

        private void setCityWeatherIcon(CityWeather cityWeather) {
            // image path example : http://openweathermap.org/img/wn/10d@2x.png
            if (!TextUtils.isEmpty(cityWeather.getWeather().get(0).getIcon())) {
                Glide.with(mContext)
                        .load(getIconPath(cityWeather.getWeather().get(0).getIcon()))
                        .fitCenter()
                        .into(binding.weatherIcon);
            } else {
                Log.d(TAG, "onBind: " + cityWeather.getName() + " has not icon.");
            }
        }
    }

    private String getIconPath(String icon) {
        return "http://openweathermap.org/img/wn/" + icon + "@2x.png";
    }

    //endregion
}
