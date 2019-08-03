package com.mark.weathermark.view.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mark.weathermark.R;
import com.mark.weathermark.callback.IBackPressListener;
import com.mark.weathermark.databinding.FragmentCityBinding;
import com.mark.weathermark.model.forecast.CityForecast;
import com.mark.weathermark.model.weather.CityWeather;
import com.mark.weathermark.view.adapter.DaysAdapter;
import com.mark.weathermark.viewmodel.CityViewModel;

import java.util.ArrayList;
import java.util.List;

public class CityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IBackPressListener {

    private static final String TAG = "CityFragment";

    private static final String ARG_CITY_ID = "city_id";
    private static final String ARG_CITY_Name = "city_name";

    private FragmentCityBinding mBinding;
    private CityViewModel mViewModel;

    private DaysAdapter mAdapter;

    private int mCityID;
    private String mCityName;

    public CityFragment() {
        // Required empty public constructor
    }

    public static CityFragment newInstance(final int cityID, final String cityName) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITY_ID, cityID);
        args.putString(ARG_CITY_Name, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCityID = args.getInt(ARG_CITY_ID);
            mCityName = args.getString(ARG_CITY_Name);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mCityName);
                activity.invalidateOptionsMenu();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cities_menu, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchMenuItem.setVisible(false); // no search in this fragment.

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle) {
            mAdapter.toggleTemperatureUnits();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_city, container, false);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mAdapter = new DaysAdapter(getContext());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CityViewModel.class);
        mBinding.setViewModel(mViewModel);
        initUI();
        fetchCityForecast();
    }

    private void initUI() {
        RecyclerView mRecyclerView = mBinding.daysRecycler;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mBinding.swipeRefresh.setOnRefreshListener(this);
    }

    private void fetchCityForecast() {
        mViewModel.getCityForecast(String.valueOf(mCityID)).observe(CityFragment.this,
                new Observer<CityForecast>() {
                    @Override
                    public void onChanged(CityForecast cityForecast) {
                        mBinding.progressBar.setVisibility(View.GONE);
                        if (cityForecast != null) {
                            //TODO: find in API documentation how to filter out for 1 forecast per day.
                            //current implementation is not elegant..
                            List<CityWeather> filteredList = new ArrayList<>();
                            List<CityWeather> forecastsList = cityForecast.getForecastsList();
                            for (int i = 0; i < forecastsList.size(); i+= 8) {
                                filteredList.add(forecastsList.get(i));
                            }
                            mAdapter.addAll(filteredList);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh() called");
        mAdapter.clear();
        fetchCityForecast();
        mBinding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
