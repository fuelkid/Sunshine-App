package com.example.android.sunshine.app;


import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.sunshine.app.data.WeatherContract;


/**
 * Created by inbkumar01 on 11/27/2016.
 */
public class ForecastFragment extends Fragment {

    private ForecastAdapter mForecastAdapter;

    public ForecastFragment() {
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // add this line in order for this fragment to handle menu events
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String locationSetting = Utility.getPreferredLocation(getActivity());
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE+ " ASC";

        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting,System.currentTimeMillis());
        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri, null,null,null,sortOrder);

        mForecastAdapter = new ForecastAdapter(getActivity(),cur,0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

         return rootView;
    }


}
