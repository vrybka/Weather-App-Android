package edu.uiuc.cs427app;

import static edu.uiuc.cs427app.MainActivity.INTENT_THEME_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LatLng latlng;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean darkThemeChecked = getIntent().getBooleanExtra(INTENT_THEME_NAME, false);

        if (darkThemeChecked) {
            setTheme(R.style.Theme_darkTheme);
        }
        // Set the layout file as the content view.
        setTitle(getIntent().getStringExtra("userName"));
        setContentView(R.layout.map_activity);

        // show back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getLocationFromAddress(getIntent().getStringExtra("city"));
        Log.d("LAT", this.latlng.latitude + " " + this.latlng.longitude);

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(this.latlng)
                .title(getIntent().getStringExtra("city") + ": (" + this.latlng.latitude + ", " + this.latlng.longitude + ")"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(this.latlng));
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
}
