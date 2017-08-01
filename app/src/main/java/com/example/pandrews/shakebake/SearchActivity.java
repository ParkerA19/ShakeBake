package com.example.pandrews.shakebake;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;

import com.example.pandrews.shakebake.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements ResultAdapter.ResultAdapterListener {

    public ArrayList<Recipe> resultRecipes;
    public ResultAdapter resultAdapter;
    public RecyclerView rvResult;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 20;
    public String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        resultRecipes = new ArrayList<>();
        query = getIntent().getStringExtra("query");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        } else {

            //create database reference
            FirebaseDatabase database =  FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            //create listener. this one adds all recipes currently in database w/fork count above 300
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                        if (query.toLowerCase().equalsIgnoreCase(newRecipe.title.toLowerCase()) | newRecipe.keywords.contains(query)) {    //later add | newRecipe.keywords.contains(query) into logic to search keywords
                            resultRecipes.add(newRecipe);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });

            rvResult = (RecyclerView) findViewById(R.id.rvResult);
            resultAdapter = new ResultAdapter(resultRecipes, this);
            rvResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvResult.setAdapter(resultAdapter);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //create database reference
                    FirebaseDatabase database =  FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();

                    //create listener. this one adds all recipes currently in database w/fork count above 300
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                                if (query.toLowerCase().equalsIgnoreCase(newRecipe.title.toLowerCase()) | newRecipe.keywords.contains(query)) {
                                    resultRecipes.add(newRecipe);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Log.w(TAG, "Failed to read value.", databaseError.toException());
                        }
                    });


                    rvResult = (RecyclerView) findViewById(R.id.rvResult);
                    resultAdapter = new ResultAdapter(resultRecipes, this);
                    rvResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvResult.setAdapter(resultAdapter);

                } else {

                    // permission denied, so dont allow any recipes with a null mediaurl to go into recipe list. see if still a valid check after making database --TODO
                }
                return;
            }

            // maybe add other 'case' lines to check for other permissions app might request
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.miSearch).getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is where adapter will be filtered, still have to add
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //route back to same activity to handle searches -- TODO
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(View view, int position) {

    }
}
