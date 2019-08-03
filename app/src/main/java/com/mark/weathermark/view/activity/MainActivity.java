package com.mark.weathermark.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mark.weathermark.R;
import com.mark.weathermark.callback.IBackPressListener;
import com.mark.weathermark.utils.FragmentNavigationManager;
import com.mark.weathermark.view.fragment.CitiesWeatherFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FragmentNavigationManager mFragmentNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentNavigationManager = new FragmentNavigationManager(getSupportFragmentManager(), getContainerID());
        mFragmentNavigationManager.addFragment(CitiesWeatherFragment.newInstance());
    }

    public int getContainerID() {
        return R.id.fragments_container;
    }

    public FragmentNavigationManager getFragmentNavigationManager() {
        return mFragmentNavigationManager;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragments_container);
            if (!(fragment instanceof IBackPressListener) || !((IBackPressListener) fragment).onBackPressed()) {
                onBackPressed();
                return true;
            }
        }
        return false;
    }
}
