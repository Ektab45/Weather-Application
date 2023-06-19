package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView dateTv;
    private TextView cityTv;
    private TextView descTv;
    private TextView windTv;
    private TextView tempTv;
    private TextView humidTv;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTv = findViewById(R.id.dateTv);
        cityTv = findViewById(R.id.cityTv);
        descTv = findViewById(R.id.descTv);
        windTv = findViewById(R.id.windTv);
        tempTv = findViewById(R.id.tempTv);
        humidTv = findViewById(R.id.humidTv);

        imageView=findViewById(R.id.iconView);

        String CurrentForecast = "https://api.openweathermap.org/data/2.5/weather?q=Kathmandu&appid=86efc9be8662947df3dfa0f75fc53799";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                CurrentForecast,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonData = response;
                        Log.d("JSON DATA(Kathmandu):", jsonData);
                        parseWeather(jsonData);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(stringRequest);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        ArrayList<ForecastModel> tasks = new ArrayList<ForecastModel>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,tasks);
        recyclerView.setAdapter(adapter);
        String forecast = "https://api.openweathermap.org/data/2.5/forecast?q=kathmandu&appid=86efc9be8662947df3dfa0f75fc53799";

        StringRequest forecastStringRequest = new StringRequest(
                Request.Method.GET,
                forecast,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonData = response;
                        Log.d("JSON DATA(Forecast):", jsonData);

                        jsonArrayDecode(jsonData, adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Something is wrong", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(forecastStringRequest);

        EditText searchET = findViewById(R.id.searchView);

        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER )) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    String search = searchET.getText().toString();

                    String forecast = "https://api.openweathermap.org/data/2.5/forecast?q=" + search + "&appid=86efc9be8662947df3dfa0f75fc53799";
                    String CurrentForecast = "https://api.openweathermap.org/data/2.5/weather?q=" + search + "&appid=86efc9be8662947df3dfa0f75fc53799";

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                    StringRequest forecastStringRequest = new StringRequest(
                            Request.Method.GET,
                            forecast,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String jsonData = response;
                                    Log.d("JSON DATA (Forecast):", jsonData);

                                    jsonArrayDecode(jsonData, adapter);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this,"Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(forecastStringRequest);

                    StringRequest weatherStringRequest = new StringRequest(
                            Request.Method.GET,
                            CurrentForecast,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String jsonData = response;
                                    Log.d("JSON DATA (Weather):", jsonData);

                                    parseWeather(jsonData);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(weatherStringRequest);

                    return true;
                }
                return false;
            }
        });

    }

    private void jsonArrayDecode(String jsonData, RecyclerViewAdapter adapter) {
        ArrayList<ForecastModel> data = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray listArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject listItem = listArray.getJSONObject(i);

              
                String date = convertTimestampToDate(listItem.getLong("dt"));
                String time = convertTimestampToTime(listItem.getLong("dt"));

                JSONObject mainObject = listItem.getJSONObject("main");
                double temperatureInKelvin = mainObject.getDouble("temp");
                double temperatureInCelsius = temperatureInKelvin - 273.15;
                String temperature = String.format("%.2f", temperatureInCelsius) + " °C";

                JSONArray weatherArray = listItem.getJSONArray("weather");
                String iconCode = weatherArray.getJSONObject(0).getString("icon");
                String imageUrl= "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                data.add(new ForecastModel(date,time, imageUrl, temperature));}

            adapter.setPosts(data);

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private String convertTimestampToTime(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeFormat.format(date);
    }

    private String parseWeather(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject mainObject = jsonObject.getJSONObject("main");

            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);
            String weatherCondition = weatherObject.getString("main");

            String iconCode = weatherObject.getString("icon");
            String imageUrl = "https://openweathermap.org/img/wn/" + iconCode + ".png";

            String city = jsonObject.getString("name");


            long timestamp = jsonObject.getLong("dt");
            String date = convertTimestampToDate(timestamp);

            double temperatureInKelvin = mainObject.getDouble("temp");
            double temperatureInCelsius = temperatureInKelvin - 273.15;
            String temp = String.format("Temp \n"+" %.2f", temperatureInCelsius) + " °C";
            String humid = mainObject.getString("humidity");

            String description = weatherObject.getString("description");

            JSONObject windData = jsonObject.getJSONObject("wind");
            String wind = windData.getString("speed");


            windTv.setText("Wind \n"+wind + " m/s");
            cityTv.setText(city);
            dateTv.setText(date);
            tempTv.setText(temp);
            descTv.setText(description);
            humidTv.setText("Humidity \n"+humid+ "%");




            Picasso.get().load(imageUrl).into(imageView);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

    private String convertTimestampToDate(long timestamp) {
            Date date = new Date(timestamp * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
            return sdf.format(date);


    }


}

