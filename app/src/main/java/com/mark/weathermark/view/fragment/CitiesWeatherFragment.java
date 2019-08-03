package com.mark.weathermark.view.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mark.weathermark.R;
import com.mark.weathermark.callback.CityClickCallback;
import com.mark.weathermark.databinding.FragmentCitiesWeatherBinding;
import com.mark.weathermark.model.weather.CityWeather;
import com.mark.weathermark.view.activity.MainActivity;
import com.mark.weathermark.view.adapter.CitiesAdapter;
import com.mark.weathermark.viewmodel.MainViewModel;

import java.util.List;

public class CitiesWeatherFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "CitiesWeatherFragment";

    private FragmentCitiesWeatherBinding mBinding;
    private MainViewModel mViewModel;

    private CitiesAdapter mAdapter;

    public CitiesWeatherFragment() {
        // Required empty public constructor
    }

    public static CitiesWeatherFragment newInstance() {
        return new CitiesWeatherFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setTitle(R.string.app_name);
                activity.invalidateOptionsMenu();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cities_menu, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit() called with: query = [" + query + "]");
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchView.onActionViewCollapsed();
                onCitySearchRequest(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    // Can/should check if I already have city's weather in adapter before making a network call.
    // UX decision - do i clear or append the found city? not for this time frame.
    private void onCitySearchRequest(final String cityName) {
        mViewModel.getCityWeather(cityName).observe(CitiesWeatherFragment.this, new Observer<CityWeather>() {
            @Override
            public void onChanged(CityWeather cityWeather) {
                Log.d(TAG, "onChanged() called with: cityWeather = [" + cityWeather + "]");
                if (cityWeather == null) {
                    Toast.makeText(getContext(), "Error fetching " + cityName + "", Toast.LENGTH_LONG).show();
                } else {
                    mAdapter.clear();
                    mAdapter.add(cityWeather);
                }
            }
        });
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
        Log.d(TAG, "onCreateView: ");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cities_weather, container, false);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mAdapter = new CitiesAdapter(getContext(), mCityClickCallback);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        RecyclerView mRecyclerView = mBinding.citiesRecycler;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mBinding.swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mBinding.setViewModel(mViewModel);
        fetchCitiesFromRemote();
    }

    private void fetchCitiesFromRemote() {
        mViewModel.getMainCitiesWeather().observe(this, new Observer<List<CityWeather>>() {
            @Override
            public void onChanged(List<CityWeather> weathers) {
                mBinding.progressBar.setVisibility(View.GONE);
                if (weathers != null && weathers.size() > 0) {
                    Log.d(TAG, "onChanged: ");
                    mAdapter.addAll(weathers);
                } else {
                    Log.e(TAG, "onChanged: ", new Throwable());
                }
            }
        });
    }

    private final CityClickCallback mCityClickCallback = new CityClickCallback() {
        @Override
        public void onCityClick(CityWeather cityWeather, int position) {
            Log.d(TAG, "onCityClick() called with: cityWeather name = " +
                    "[" + cityWeather.getName() + "], position = [" + position + "]");

            FragmentActivity activity = getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).getFragmentNavigationManager().replaceFragment(
                        CityFragment.newInstance(cityWeather.getId(), cityWeather.getName()));
            }
        }
    };

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh() called");
        mAdapter.clear();
        fetchCitiesFromRemote();
        mBinding.swipeRefresh.setRefreshing(false);
    }
}
