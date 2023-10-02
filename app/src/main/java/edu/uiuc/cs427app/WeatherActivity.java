package edu.uiuc.cs427app;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Activity class for weather
 */
public class WeatherActivity extends AppCompatActivity {
    public static final String INTENT_THEME_NAME = "theme";
    private LatLng latlng;

    private String timeZoneId;
    String currentTime;
    String currentDate;
    String cityName;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    DecimalFormat fm = new DecimalFormat("#.##");
    private final String appid = "f9a014f618783cea4b3640f8dccb8e39";
    TextView result;

    /**
     * create the detailed weather page for city
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean darkThemeChecked = getIntent().getBooleanExtra(INTENT_THEME_NAME, false);
        String label = getIntent().getStringExtra("userName");

        setTitle(label);
        if (darkThemeChecked) {
            setTheme(R.style.Theme_darkTheme);
        }

        // show back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.weather_activity);

        // Process the Intent payload that has opened this Activity and show the information accordingly
        cityName = getIntent().getStringExtra("city").toString();

        getLocationFromAddress(cityName);
        setTimeZone();
        getWeather();

        // Initializing the GUI elements
        result = findViewById(R.id.weatherinfo);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * helper function to get latitude and longitude from a city name
     */
    public void getLocationFromAddress(String city) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(city, 5);
            if (address == null) {
                return;
            }
            Address location = address.get(0);
            this.latlng = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTimeZone() {
        String url = "https://maps.googleapis.com/maps/api/timezone/json?location=" + latlng.latitude + "%2C" + latlng.longitude + "&timestamp=1331161200&key=AIzaSyBmKr2rUPWzoCUQ8hR53aCYI3dIr2pLaLw";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject obj = new JSONObject(response);
                            timeZoneId = obj.getString("timeZoneId");
                            TimeZone tz = TimeZone.getTimeZone(timeZoneId);

                            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
                            df.setTimeZone(tz);
                            df = DateFormat.getDateInstance(DateFormat.FULL);
                            df.setTimeZone(tz);
                            String dateString = df.format(new Date());
                            df = DateFormat.getTimeInstance(DateFormat.FULL);
                            df.setTimeZone(tz);
                            String timeString = df.format(new Date());
                            currentTime = "Time: " + timeString;
                            currentDate = "Date: " + dateString;
                            TextView TimeMessage = findViewById(R.id.currentTimeInfo);
                            TextView DateMessage = findViewById(R.id.currentDateInfo);
                            TimeMessage.setText(currentTime);
                            DateMessage.setText(currentDate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    public void getWeather() {
        String url2 = url + "?q=" + cityName + "&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    JSONObject jresponse = new JSONObject(response);
                    JSONArray weatherm = jresponse.getJSONArray("weather");
                    JSONObject weather1 = weatherm.getJSONObject(0);
                    String weather = weather1.getString("description");
                    JSONObject main = jresponse.getJSONObject("main");
                    double temp = main.getDouble("temp") - 273.15;
                    int humi = main.getInt("humidity");
                    JSONObject windm = jresponse.getJSONObject("wind");
                    String windspeed = windm.getString("speed");
                    String winddeg = windm.getString("deg");

                    output += "City: "+ cityName
                            + "\nTemperature: " + fm.format(temp) + "°C"
                            + "\n Weather: " + weather
                            + "\n Humidity: " + humi + "%"
                            + "\n Wind Speed: " + windspeed + " meters per second (m/s)"
                            + "\n Wind Degree: " + winddeg;
                    result.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
