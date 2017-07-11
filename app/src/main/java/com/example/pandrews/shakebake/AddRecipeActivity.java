package com.example.pandrews.shakebake;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {
    Button bAddIngredient;
    Button bRecipeSteps;
    RecyclerView rvIngredients;
    RecyclerView rvSteps;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> supplyList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        bAddIngredient = (Button) findViewById(R.id.bAddIngredients);
        bRecipeSteps = (Button) findViewById(R.id.bRecipeSteps);
        rvIngredients = (RecyclerView) findViewById(R.id.rvIngredients);
        rvSteps = (RecyclerView) findViewById(R.id.rvSteps);

        layoutManager = new LinearLayoutManager(this);

        adapter = new AddRecipeAdapter(supplyList, this);
        rvIngredients.setAdapter(adapter);
        supplyList = new ArrayList<>();
    }

    public void onAddIngredient(View v) {
        Integer REQUEST_CODE = 20;
        Intent i = new Intent(this, AddActivity.class);
        i.putExtra("title", "Add Ingredients");
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onAddStep(View v) {
        Integer REQUEST_CODE = 21;
        Intent i = new Intent(this, AddActivity.class);
        i.putExtra("title", "Add Steps");
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            //add ingredients to list in add recipe activity
            supplyList = data.getExtras().getStringArrayList("supplyList");
            rvIngredients.setLayoutManager(new LinearLayoutManager(this));
            AddRecipeAdapter rAdapter = new AddRecipeAdapter(supplyList, this);
            rvIngredients.setAdapter(rAdapter);
        } else {
            supplyList = data.getExtras().getStringArrayList("supplyList");
            rvSteps.setLayoutManager(new LinearLayoutManager(this));
            AddRecipeAdapter rAdapter = new AddRecipeAdapter(supplyList, this);
            rvSteps.setAdapter(rAdapter);
        }
    }
 // figure out if intent for result can be replaced with a regular intent so it doesnt crash when you go back to add recipe -- TODO
}


