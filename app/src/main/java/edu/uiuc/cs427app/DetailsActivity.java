package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String INTENT_THEME_NAME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean darkThemeChecked = getIntent().getBooleanExtra(INTENT_THEME_NAME, false);
        String label = getIntent().getStringExtra("userName");

        setTitle(label);
        if (darkThemeChecked) {
            setTheme(R.style.Theme_darkTheme);
        }
        setContentView(R.layout.activity_details);

        // Process the Intent payload that has opened this Activity and show the information accordingly
        String cityName = getIntent().getStringExtra("city").toString();
        String welcome = "Welcome to the "+cityName;
        String cityWeatherInfo = "Detailed information about the weather of "+cityName;

        // Initializing the GUI elements
        TextView welcomeMessage = findViewById(R.id.welcomeText);
        TextView cityInfoMessage = findViewById(R.id.cityInfo);

        welcomeMessage.setText(welcome);
        cityInfoMessage.setText(cityWeatherInfo);
        // Get the weather information from a Service that connects to a weather server and show the results

        Button buttonMap = findViewById(R.id.mapButton);
        buttonMap.setOnClickListener(this);

        Button buttonWeather = findViewById(R.id.weatherButton);
        buttonWeather.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //If weatherButton was selected, it goes to weather_activity, which shows the weather of the selected city.
        //If mapButton was selected, it goes to map activity, which shows the map of the selected city.
        Intent intent;
        String cityName = getIntent().getStringExtra("city");

        switch (view.getId()) {
            case R.id.weatherButton:
                intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("city", cityName);
                startActivity(intent);
                break;

            case R.id.mapButton:
                break;
        }
    }
}

