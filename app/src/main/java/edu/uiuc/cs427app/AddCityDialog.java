package edu.uiuc.cs427app;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddCityDialog extends AppCompatActivity {
    final private String TAG = AddCityDialog.class.getSimpleName();
    private String cityName = "";
    private boolean success = false;
    private boolean exist = false;
    AlertDialog.Builder builder;

    public AddCityDialog(Activity context, String username, customizedAdapter adapter) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Add the city");
        View v = context.getLayoutInflater().inflate(R.layout.edittext, null);

        UserCityDB userCityDB = new UserCityDB(context);
        builder.setView(v)
            // Add action buttons
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            })
            .setPositiveButton("Add City", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // add location
                    // 1. get user id
                    EditText cityInput = (EditText) v.findViewById(R.id.editText);
                    cityName = cityInput.getText().toString();
                    Log.d(TAG, "userInput: " + cityName + " userName: " + username);

                    // 2. add the input into the list of corresponding user
                    exist = userCityDB.isExist(username, cityName);
                    if(exist){
                        Toast.makeText( context, cityName+" exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        success = userCityDB.insertUserCity(username, cityName);
                        if(success){
                            Toast.makeText( context, cityName+" has been added", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // 3. refresh the list
                    adapter.refresh(new ArrayList<>(userCityDB.getCityList(username)));
                }
            });
    }

    public void show() {
        builder.show();
    }
}