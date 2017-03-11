package com.example.android.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by inbkumar01 on 11/27/2016.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;

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
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(),mForecastAdapter);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location= preferences.getString(getString(R.string.prefLocationKey),getString(R.string.prefLocationDefault));
        weatherTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         mForecastAdapter =
                 new ArrayAdapter<String>(
                         getActivity(), // The current context (this activity)
                         R.layout.list_item_forecast, // The name of the layout ID.
                         R.id.list_item_forecast_textview, // The ID of the textview to populate.
                         new ArrayList<String>());

         View rootView = inflater.inflate(R.layout.fragment_main, container, false);

         // Get a reference to the ListView, and attach this adapter to it.
         ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
         listView.setAdapter(mForecastAdapter);
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                 String forcast = mForecastAdapter.getItem(postion);
                 Intent intent = new Intent(getActivity(),DetailActivity.class)
                                .putExtra(Intent.EXTRA_TEXT,forcast);
                 startActivity(intent);

             }
         });

         return rootView;
    }


}
