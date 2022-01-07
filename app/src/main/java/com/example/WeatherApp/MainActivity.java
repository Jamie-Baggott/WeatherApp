package com.example.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText city;
    Spinner weather, citySpinner;
    EditText temperature;
    EditText min;
    EditText max;
    Button saveBtn, clrBtn, returnBtn, homeBtn;
    Intent intent;
    String url = "https://www.metaweather.com/api/location/615702/";;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectWeather();

    }


    public void parseJsonData(String jsonString) {

        try {

            JSONObject object = new JSONObject(jsonString);

            JSONArray api = object.getJSONArray("consolidated_weather");


            for (int i = 0; i < 1; ++i) {


                JSONObject data = api.getJSONObject(0);

                int maxAPI = data.getInt("max_temp");
                int minAPI = data.getInt("min_temp");
                int tempAPI = data.getInt("the_temp");
                String weatherAPI = data.getString("weather_state_name");


                Spinner weatherOptions = findViewById(R.id.spinner1);
                String[] weatherType = new String[]{"Snow", "Sleet", "Hail", "Thunderstorm", "Heavy Rain", "Light Rain", "Showers", "Heavy Cloud", "Light Cloud", "Clear"};
                ArrayAdapter<String> weatherChoice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, weatherType);
                weatherOptions.setAdapter(weatherChoice);

                Spinner cityOptions = findViewById(R.id.spinner2);
                String[] cities = new String[]{ "Paris", "London", "Dublin", "Milan", "Rome", "Sofia", "Berlin", "Geneva", "Kiev", "Prague", "Edinburgh", "Moscow", "Bucharest"};
                ArrayAdapter<String> cityChoice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
                cityOptions.setAdapter(cityChoice);

                city = (EditText) findViewById(R.id.txtCity);
                citySpinner = cityOptions;
                weather = weatherOptions;
                temperature = (EditText) findViewById(R.id.txtTemp);
                min = (EditText) findViewById(R.id.txtMin);
                max = (EditText) findViewById(R.id.txtMax);
                saveBtn = (Button) findViewById(R.id.btnSave);
                clrBtn = (Button) findViewById(R.id.clearbutton);
                returnBtn = (Button) findViewById(R.id.retrunbutton);
                homeBtn = (Button) findViewById(R.id.homebutton);




                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cityName = city.getText().toString();
                        if (cityName.equals(""))
                            cityName = cityOptions.getSelectedItem().toString();
                        String currentTemp = tempAPI + "°";
                        //String currentTemp = temperature.getText().toString() + "°";

                        //String weatherStatus = weatherOptions.getSelectedItem().toString();
                        String weatherStatus = weatherAPI;

                        //String minTemp = min.getText().toString() + "°";
                        String minTemp = minAPI + "°";

                        //String maxTemp = max.getText().toString() + "°";
                        String maxTemp = maxAPI + "°";

                        DbHandler dbHandler = new DbHandler(MainActivity.this);
                        dbHandler.insertUserDetails(cityName, weatherStatus, currentTemp, minTemp, maxTemp);
                        intent = new Intent(MainActivity.this, DetailsActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Details Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                });


                clrBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DbHandler dbHandler = new DbHandler(MainActivity.this);
                        dbHandler.ResetDatabase();
                        dbHandler.insertUserDetails("London", "Snow", "4°", "-1°", "4°");
                        dbHandler.insertUserDetails("Dublin", "Rain", "3°", "1°", "4°");
                    }
                });

                returnBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(MainActivity.this, DetailsActivity.class);
                        startActivity(intent);
                    }
                });

                homeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(MainActivity.this, IntroActivity.class);
                        startActivity(intent);
                    }
                });

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void connectWeather(){

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

    }
}


//onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)