package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.RecipeAdapter;
import com.example.pandrews.shakebake.models.Recipe;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/10/17.
 */

public class RecipesListFragment extends Fragment implements RecipeAdapter.RecipeAdapterListener {

    public interface RecipeSelectedListener {
        // handle recipe selection
        public void onRecipeSelected(Recipe recipe);
    }

    // Instance variables
    static RecipeAdapter recipeAdapter;
    public static ArrayList<Recipe> recipes;  //make sure this variable can be static
    static RecyclerView rvRecipes;

    public SwipeRefreshLayout swipeContainer;

    // inflation happens inside onCreateView


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        View v = inflater.inflate(R.layout.fragments_recipes_list, container, false);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }
        });

        // Configure the refeshing colors
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

        return v;
    }

    public void populateTimeline() {
        return;
    }

    @Override
    public void onItemSelected(View view, int position) {
        Recipe recipe = recipes.get(position);

        Toast.makeText(getContext(), recipe.title, Toast.LENGTH_SHORT).show();
    }

    public static void onNewRecipeAvailable(Recipe recipe){
        recipes.add(0, recipe);
        recipeAdapter.notifyItemInserted(0);
        rvRecipes.scrollToPosition(0);
    }
}
