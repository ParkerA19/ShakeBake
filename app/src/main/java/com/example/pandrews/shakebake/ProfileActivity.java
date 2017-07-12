package com.example.pandrews.shakebake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pandrews.shakebake.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    // Instance Variables
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String username;
        user = Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));




    }
}
