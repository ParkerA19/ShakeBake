package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.RecipeAdapter;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pandrews on 7/10/17.
 */

public class HomeTimelineFragment extends RecipesListFragment {

    public static ArrayList<String> r1iList = new ArrayList<>(Arrays.asList("milk", "stuff"));
    public static ArrayList<String> r1sList = new ArrayList<>(Arrays.asList("Pour milk", "eat cereal", "repeat until full"));

    public static ArrayList<String> r2iList = new ArrayList<>(Arrays.asList("fruit", "juice"));
    public static ArrayList<String> r2sList = new ArrayList<>(Arrays.asList("bite", "bite again", "finish"));

    public static ArrayList<String> r3iList = new ArrayList<>(Arrays.asList("fish", "rice", "seaweed", "wasabi"));
    public static ArrayList<String> r3sList = new ArrayList<>(Arrays.asList("cover seaweed in rice", "wrap fish with seaweed (which should now be covered in rice.. if this is not the case then you missed the only step so far and should probably try making something else)", "apply wasabi", "eat"));

    public static ArrayList<String> r4iList = new ArrayList<>(Arrays.asList("fruit", "juice"));
    public static ArrayList<String> r4sList = new ArrayList<>(Arrays.asList("bite", "bite again", "finish"));

    public static User u1 = new User("Parker", "pandrews", "https://static.pexels.com/photos/404843/pexels-photo-404843.jpeg" , 10, 20, 300);
    public static User u2 = new User("Andrea", "agarcia", "https://static.pexels.com/photos/163114/mario-luigi-figures-funny-163114.jpeg", 15, 30, 450);
    public static User u3 = new User("Jennifer", "jshin", "https://static.pexels.com/photos/437886/pexels-photo-437886.jpeg", 20, 40, 700);

    public static Recipe r1 = new Recipe("Cereal", "Cinammon Toast Crunch", u1, "https://pbs.twimg.com/media/Bv6uxxaCcAEjWHD.jpg", 200, false, r1iList, r1sList);
    public static Recipe r2 = new Recipe("Mangos", "round juicy fruit", u2, null, 300, true, r2iList, r2sList);
    public static Recipe r3 = new Recipe("Sushi", "Dead Fish", u3, null, 220, true, r3iList, r3sList);


    static RecipeAdapter recipeAdapter;
    public static ArrayList<Recipe> recipes = new ArrayList<>(Arrays.asList(r1, r2, r3));
    static RecyclerView rvRecipes;
    public SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        // construct the adapter from this data source
        recipeAdapter = new RecipeAdapter(recipes, this);
        // RecyclerView setup (layout manger, user adapter)
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvRecipes.setAdapter(recipeAdapter);

        return v;
    }

    public void populateTimeline() {
        Recipe r4 = new Recipe();

        recipes.add(r4);
        recipeAdapter.notifyItemInserted(recipes.size() -1);

        swipeContainer.setRefreshing(false);

    }


    public static void appendRecipe(Recipe recipe) {
        // add a tweet
        recipes.add(0, recipe);
        // inserted at position 0
        recipeAdapter.notifyItemInserted(0);
        rvRecipes.scrollToPosition(0);
    }

    @Override
    public void onStart() {
        //populateTimeline();
        super.onStart();
    }
}
