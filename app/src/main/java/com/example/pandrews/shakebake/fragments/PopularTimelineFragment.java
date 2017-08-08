package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.PopularAdapter;
import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/10/17.
 */

public class PopularTimelineFragment extends RecipesListFragment implements PopularAdapter.PopularAdapterListener {

    //instance variables
    static PopularAdapter popularAdapter;
    public static ArrayList<Recipe> popular;
    static RecyclerView rvPopular;
    public SwipeRefreshLayout swipeContainer;
    public ArrayList<String> popularTitles;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_popular, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
           //     popularAdapter.clear();
                populateTimeline();
            }
        });

        // Configure the refeshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_light);


        // find the RecyclerView
        rvPopular = (RecyclerView) v.findViewById(R.id.rvPopular);
        // init the arraylist (data source)
        popular = new ArrayList<>();
        // construct the adapter from this data source
        popularAdapter = new PopularAdapter(popular, this);
        // RecyclerView setup (layout manger, user adapter)
        rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvPopular.setAdapter(popularAdapter);

        //init title list
        popularTitles = new ArrayList<>();


        //create database reference
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recipes");

        //create listener. this one adds all recipes currently in database w/fork count above 300
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);

                    if (newRecipe.stepVideo == null) {
                        newRecipe.mediaurl = "android.resource://com.example.pandrews.shakebake/" + R.raw.elephant;

                    }
                    //modify line below for min fork threshold
                    if (newRecipe.forkCount >= 300) {
                        appendRecipe(newRecipe);
                        //keep track of recipes already added
                        popularTitles.add(newRecipe.title);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return v;
    }

    public void populateTimeline() {

        popularAdapter.clear();
        popularTitles.clear();

        //new reference
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recipes");

        //this listener looks for new recipes added by checking list of titles. in populateTimeline so it's called on refresh
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);

                    if (newRecipe.stepVideo == null) {
                        newRecipe.mediaurl = "android.resource://com.example.pandrews.shakebake/" + R.raw.elephant;

                    }
                    //modify line below for min fork threshold.
                    //checks here if recipe is already being shown & checks forks
                    if (!popularTitles.contains(newRecipe.title) & newRecipe.forkCount >= 300) {
                        appendRecipe(newRecipe);
                        // keep track of recipes already added
                        popularTitles.add(newRecipe.title);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onStart() {
        //populateTimeline();
        super.onStart();
    }

    public static void appendRecipe(Recipe recipe) {
        // add a recipe
        popular.add(0, recipe);
        // inserted at position 0
        popularAdapter.notifyItemInserted(0);
        rvPopular.scrollToPosition(0);
    }

}






