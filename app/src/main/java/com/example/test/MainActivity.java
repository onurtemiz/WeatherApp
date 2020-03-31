package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    result += (char) data;
                    data = reader.read();

                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    private ListView listView;
    private String result = "";
    private String apiKey = "7e12a59f52fec190683a19f2ff4a8a87";
    private String cityName = "";
    private String url = "https://api.openweathermap.org/data/2.5/weather?q=" + this.cityName + "&appid=" + this.apiKey;
    private EditText cityNameView;
    private TextWatcher tt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityNameView = (EditText) findViewById(R.id.cityName);

        tt = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cityName = cityNameView.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        cityNameView.addTextChangedListener(tt);



    }

    public void getWeather(View view){
        try {
            DownloadTask task = new DownloadTask();

            this.result = task.execute(this.url).get();

            JSONObject jsonObject =  new JSONObject(this.result);

            JSONObject weatherInfo = jsonObject.getJSONArray("weather").getJSONObject(0);
            String weather = weatherInfo.getString("main");

            JSONObject tempInfo = jsonObject.getJSONObject("main");
            String temp = tempInfo.getString("temp");

            TextView showData = (TextView) findViewById(R.id.showData);
            showData.setText("City: " + cityName.substring(0, 1).toUpperCase() + cityName.substring(1) + "\n" + "Weather: " + weather + "\n Temp: " + ((int)(Double.parseDouble(temp)-273.15)));
            showData.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
