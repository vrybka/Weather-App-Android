package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    UserCityDB userCityDB = new UserCityDB(this);

    private ListView listv;
    private ArrayList<String> listOfCity;
    private customizedAdapter adapter;
    private String username;

    public static final String INTENT_THEME_NAME = "theme";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        boolean darkThemeChecked = getIntent().getBooleanExtra(INTENT_THEME_NAME, false);
        switch (item.getItemId()) {
            case R.id.action_logout:
                intent = new Intent(this, LoginActivity.class);
                intent.putExtra(INTENT_THEME_NAME, darkThemeChecked);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean darkThemeChecked = getIntent().getBooleanExtra(INTENT_THEME_NAME, false);
        String label = getIntent().getStringExtra("userName");

        setTitle(label);

        if (darkThemeChecked) {
            setTheme(R.style.Theme_darkTheme);
        }
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("userName");
        listv = findViewById(R.id.list);
        listOfCity = userCityDB.getCityList(username);
        adapter = new customizedAdapter(listOfCity, this, getIntent());

        // Initializing the UI components
        // The list of locations should be customized per user (change the implementation so that
        // buttons are added to layout programmatically
        listv.setAdapter(adapter);

        Button buttonNew = findViewById(R.id.buttonAddLocation);
        Button buttonRemove = findViewById(R.id.buttonRemoveLocation);

        buttonNew.setOnClickListener(this);
        buttonRemove.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        boolean darkThemeChecked = getIntent().getBooleanExtra(INTENT_THEME_NAME, false);
        String label = getIntent().getStringExtra("userName");

        switch (view.getId()) {
            case R.id.buttonAddLocation:
                // Implement this action to add a new location to the list of locations
                AddCityDialog addCityDialog = new AddCityDialog(this, username, adapter);
                addCityDialog.show();
                intent = new Intent(this, RecyclerViewAdapter.class);
                intent.putExtra(INTENT_THEME_NAME, darkThemeChecked);
                intent.putExtra("userName", label);
                break;

            case R.id.buttonRemoveLocation:
                RemoveCityDialog removeCityDialog = new RemoveCityDialog(this, username, adapter);
                removeCityDialog.show();
                intent = new Intent(this, RecyclerViewAdapter.class);
                intent.putExtra(INTENT_THEME_NAME, darkThemeChecked);
                intent.putExtra("userName", label);
                break;
        }
    }
}
