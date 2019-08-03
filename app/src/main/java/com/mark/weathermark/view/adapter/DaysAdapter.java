package com.mark.weathermark.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.mark.weathermark.R;
import com.mark.weathermark.databinding.DayAdapterItemBinding;
import com.mark.weathermark.enums.UnitsType;
import com.mark.weathermark.model.weather.CityWeather;
import com.mark.weathermark.model.weather.MinMaxTemperatures;
import com.mark.weathermark.utils.DateFactory;
import com.mark.weathermark.utils.TemperatureUtils;
import com.mark.weathermark.view.adapter.viewholder.BaseViewHolder;

public class DaysAdapter extends BaseAdapter<CityWeather, BaseViewHolder> {

    private static final String TAG = "DaysAdapter";

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    public DaysAdapter(Context context) {
        super(context);
    }

    // default displaying unit type
    private UnitsType mCurrentUnitsType = UnitsType.METRIC;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                DayAdapterItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.day_adapter_item, parent, false);
                return new DayViewHolder(binding);

            default:
                //TODO: put here loading or footer, not item.
                binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.day_adapter_item, parent, false);
                return new DayViewHolder(binding);
        }
    }

    @Override
    public long getItemId(int position) {
        return mDataSet.get(position).getTime(); // forecast has no id, time will be unique in this data set.
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

    //region day view holder

    public class DayViewHolder extends BaseViewHolder {

        final DayAdapterItemBinding binding;

        public DayViewHolder(DayAdapterItemBinding binding) {
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
            binding.setPosition(position);
            binding.executePendingBindings();

            CityWeather cityForecast = mDataSet.get(position);

            binding.dayTv.setText(DateFactory.getDateFromMillis(cityForecast.getTime()));
            binding.forecastDescription.setText(cityForecast.getWeather().get(0).getDescription());

            setMinMaxTemperatures(cityForecast);
            setCityWeatherIcon(cityForecast);
        }

        private void setMinMaxTemperatures(CityWeather cityWeather) {
            Resources resources = DaysAdapter.this.mContext.getResources();

            String temperaturesText = String.format(resources.getString(mCurrentUnitsType == UnitsType.METRIC ?
                            R.string.celsius_formatter : R.string.fahrenheit_formatter),
                    cityWeather.getMinMaxTemperatures().getTempMinStr(), cityWeather.getMinMaxTemperatures().getTempMaxStr());

            binding.temperatureTv.setText(temperaturesText);
        }

        private void setCityWeatherIcon(CityWeather cityWeather) {
            if (!TextUtils.isEmpty(cityWeather.getWeather().get(0).getIcon())) {
                Glide.with(mContext)
                        .load(getIconPath(cityWeather.getWeather().get(0).getIcon()))
                        .fitCenter()
                        .into(binding.weatherIcon);
            } else {
                Log.e(TAG, "setCityWeatherIcon: ", new Throwable());
            }
        }

        private String getIconPath(String icon) {
            return "http://openweathermap.org/img/wn/" + icon + "@2x.png";
        }
    }

    //endregion
}
