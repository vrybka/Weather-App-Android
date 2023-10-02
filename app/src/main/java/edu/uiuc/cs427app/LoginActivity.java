package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TEAM_PREFIX = "Your weather-";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        Button signup = findViewById(R.id.signup);
        Button signin = findViewById(R.id.signin);

        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        DBHelper dbHelper = new DBHelper(this);
        //method for Sign in and Sign up
        switch (view.getId()) {
            case R.id.signin:
                login(dbHelper, username.getText().toString(), password.getText().toString());
                break;

            case R.id.signup:
                signup(dbHelper, username.getText().toString(), password.getText().toString());
                break;
        }
    }


    private void signup(DBHelper dbHelper, String userName, String passWord) {
        if (userName.isEmpty() || passWord.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUserNameTaken(userName)) {
            Toast.makeText(LoginActivity.this, "User name is taken", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.insertData(userName, passWord)) {
            Toast.makeText(LoginActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
        }
    }

    private void login(DBHelper dbHelper, String userName, String password) {
        if (!dbHelper.checkusernamepassword(userName, password)) {
            Toast.makeText(LoginActivity.this, "wrong username or password", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean darkThemeChecked = ((CheckBox) findViewById(R.id.darkTheme)).isChecked();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", darkThemeChecked);
        intent.putExtra("userName", TEAM_PREFIX + userName);
        startActivity(intent);
    }
}
