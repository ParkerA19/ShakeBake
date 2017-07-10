package com.example.jennifershin.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pandrews.shakebake.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginPressed (View v) {
        EditText etEmail = (EditText) findViewById(R.id.email);
        v.setEnabled(false);
        String itemText = etEmail.getText().toString();
        Toast.makeText(getApplicationContext(), itemText + " logged in", Toast.LENGTH_SHORT).show();
    }

    /*
    public void onCancel (View v) {
        EditText etEmail = (EditText) findViewById(R.id.email);
        etEmail.setText("");
        EditText etPassword = (EditText) findViewById(R.id.password);
        etPassword.setText("");
        Toast.makeText(getApplicationContext(), "Login canceled", Toast.LENGTH_SHORT).show();
    }
    */

}
