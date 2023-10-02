package edu.uiuc.cs427app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;

import java.util.ArrayList;


public class RemoveCityDialog {
    final private String TAG = AddCityDialog.class.getSimpleName();
    private String cityName = "";
    private boolean success = false;
    private boolean exist = false;
    AlertDialog.Builder builder;

    /**
     * method to pop up dialog for user to remove the new add city
     */
    public RemoveCityDialog(Activity context, String username, customizedAdapter adapter) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Remove the city");

        UserCityDB userCityDB = new UserCityDB(context);
        View v = context.getLayoutInflater().inflate(R.layout.edittext, null);

        builder.setView(v)
            // button to cancel
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            })
            // button to remove the city
            .setPositiveButton("Remove City", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // remove location
                    // 1. get user id
                    EditText cityInput = (EditText) v.findViewById(R.id.editText);

                    cityName = cityInput.getText().toString();
                    Log.d(TAG, "userInput: " + cityName + " userName: " + username);

                    // 2. remove the input into the list of corresponding user
                    exist = userCityDB.isExist(username,cityName);
                    if(!exist){
                        Toast.makeText( context, cityName+" not exist", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        success = userCityDB.removeUserCity(username, cityName);
                        if(success){
                            Toast.makeText( context, cityName+" has been removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    // refresh the current list page
                    adapter.refresh(new ArrayList<>(userCityDB.getCityList(username)));
                }
            });
    }

    public void show() {
        builder.show();
    }

}