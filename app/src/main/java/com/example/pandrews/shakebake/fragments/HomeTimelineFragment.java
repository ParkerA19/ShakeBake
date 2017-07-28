package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.RecipeAdapter;
import com.example.pandrews.shakebake.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/10/17.
 */

public class HomeTimelineFragment extends RecipesListFragment {

    private DatabaseReference mDatabase;

    static RecipeAdapter recipeAdapter;
//    public static ArrayList<Recipe> recipes = new ArrayList<>(Arrays.asList(r1, r2, r3));
    public static ArrayList<Recipe> recipes;
    static RecyclerView rvRecipes;
    public SwipeRefreshLayout swipeContainer;
    public ArrayList<String> recipeTitles;

    private StorageReference storageReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageReference = FirebaseStorage.getInstance().getReference("videos");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_recipes_list, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
     //           recipeAdapter.clear();
                populateTimeline();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // find the RecyclerView
        rvRecipes = (RecyclerView) v.findViewById(R.id.rvRecipe);
        // init the arraylist (data source)
        recipes = new ArrayList<>();
        // construct the adapter from this data source
        recipeAdapter = new RecipeAdapter(recipes, this);
        // RecyclerView setup (layout manger, user adapter)
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvRecipes.setAdapter(recipeAdapter);

        //init title list
        recipeTitles = new ArrayList<>();

        //create database reference
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        //create listener. this one adds all recipes currently in database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                    //LinkedHashMap<String, String> stepsVid = postSnapshot.getValue(Recipe.class).stepVideo;
                    if (newRecipe.stepVideo == null) {
                        newRecipe.mediaurl = "android.resource://com.example.pandrews.shakebake/" + R.raw.cat;

                        //newRecipe.mediaurl = "https://firebasestorage.googleapis.com/v0/b/shake-n-bake-5d01f.appspot.com/o/videos%2F251?alt=media&token=7918ec93-b700-456a-9e15-e12eefe7d4a1";
                    }
                    //newRecipe.mediaurl = "android.resource://com.example.pandrews.shakebake/" + R.raw.cat;
                    appendRecipe(newRecipe);
                    //keep track of recipes already added
                    recipeTitles.add(newRecipe.title);
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

        //this listener looks for new recipes added by checking list of titles. in populateTimeline so it's called on refresh
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                    //if stepVideo == null then set to cat video. else get values from stepVideo and loop through
                    if (newRecipe.stepVideo == null) {
                        newRecipe.mediaurl = "android.resource://com.example.pandrews.shakebake/" + R.raw.cat;
                    } else {
                        newRecipe.mediaurl = "https://firebasestorage.googleapis.com/v0/b/shake-n-bake-5d01f.appspot.com/o/videos%2F251?alt=media&token=7918ec93-b700-456a-9e15-e12eefe7d4a1";
                    }

                    //newRecipe.mediaurl = "android.resource://com.example.pandrews.shakebake/" + R.raw.cat;
                    //modify line below for min fork threshold.
                    //checks here if recipe is already being shown & checks forks
                    if (!recipeTitles.contains(newRecipe.title)) {
                        appendRecipe(newRecipe);
                        recipeTitles.add(newRecipe.title);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("htf", "Failed to read value.", databaseError.toException());
            }
        });

        swipeContainer.setRefreshing(false);

    }


    public static void appendRecipe(Recipe recipe) {
        // add a tweet
        recipes.add(0, recipe);
        // inserted at position 0
        recipeAdapter.notifyItemInserted(0);
        rvRecipes.scrollToPosition(0);
    }

}
