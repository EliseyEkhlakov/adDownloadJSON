package com.demo.downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadJSONTask task = new DownloadJSONTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q=London");
    }

    private static class DownloadJSONTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while(line != null){
                    result.append(line);
                    line = bufferedReader.readLine();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i("NAME_",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String name = jsonObject.getString("name");
                Log.i("NAME_",name);
                JSONObject main = jsonObject.getJSONObject("main");
                String temp = main.getString("temp");
                String pressure = main.getString("pressure");
                Log.i("NAME_", temp + " - " + pressure);
                //
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject weather = jsonArray.getJSONObject(0);
                String _main = weather.getString("main");
                String description = weather.getString("description");
                Log.i("NAME_", _main + "  " + description);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}