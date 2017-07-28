package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.MyForksAdapter;
import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/11/17.
 */

public class MyForksTimelineFragment extends RecipesListFragment implements MyForksAdapter.ForkAdapterListener {

    //private FirebaseAnalytics mFirebaseAnalytics;

    // Instance variables
    static MyForksAdapter forksAdapter;
    public static ArrayList<Recipe> forks;
    static RecyclerView rvForks;
    public SwipeRefreshLayout swipeContainer;
    public ArrayList<String> forksTitles;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_my_forks, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //forksAdapter.clear();
                populateTimeline();
            }
        });

// Configure the refeshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // find the RecyclerView
        rvForks = (RecyclerView) v.findViewById(R.id.rvForks);
        // init the arraylist (data source)
        forks = new ArrayList<>();
        // construct the adapter from this data source
        forksAdapter = new MyForksAdapter(forks, this);
        // RecyclerView setup (layout manger, user adapter)
        rvForks.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvForks.setAdapter(forksAdapter);

        //init title list
        forksTitles = new ArrayList<>();

        //create database reference
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        //create listener. this one adds all recipes with forked=true
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                    if (newRecipe.forked) {
                        appendRecipe(newRecipe);
                        //keep track of recipes already added
                        forksTitles.add(0, newRecipe.title);
                    }
                    newRecipe.mediaurl = "android.resource://com.example.pandrews.shakebake/" + R.raw.dog;
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


        //new reference
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        //this listener looks for new recipes added by checking list of titles in populateTimeline so it's called on refresh
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                    newRecipe.mediaurl = "android.resource://com.example.pandrews.shakebake/" + R.raw.dog;
                    //checks here if recipe is already being shown & checks forks
                    if (!forksTitles.contains(newRecipe.title) & newRecipe.forked) {
                        appendRecipe(newRecipe);
                        forksTitles.add(0, newRecipe.title);
                    }
                    if (forksTitles.contains(newRecipe.title) & !newRecipe.forked) {
                        removeRecipe(newRecipe);
                        forksTitles.remove(newRecipe.title);
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

    @Override
    public void onItemSelected(View view, int position) {
    }

    public static void appendRecipe(Recipe recipe) {
        // add a tweet
        forks.add(0, recipe);
        // inserted at position 0
        forksAdapter.notifyItemInserted(0);
        rvForks.scrollToPosition(0);
    }

    public void removeRecipe(Recipe recipe) {
        Integer removedRecipe = forksTitles.indexOf(recipe.title);
        // remove a recipe
        forks.remove(recipe);
        // inserted at position 0
        forksAdapter.notifyItemRemoved(removedRecipe);
        rvForks.scrollToPosition(0);
    }

}
