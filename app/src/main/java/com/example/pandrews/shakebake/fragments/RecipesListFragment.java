package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pandrews.shakebake.RecipeAdapter;
import com.example.pandrews.shakebake.models.Recipe;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/10/17.
 */

public class RecipesListFragment extends Fragment implements RecipeAdapter.RecipeAdapterListener {
    //    // Instance variables
    static RecipeAdapter recipeAdapter;
    static RecipesPagerAdapter recipesPagerAdapter;
    public static ArrayList<Recipe> recipes;
    static RecyclerView rvRecipes;
//    public Integer value;

    public SwipeRefreshLayout swipeContainer;

    public interface RecipeSelectedListener {
        // handle recipe selection
        public void onRecipeSelected(Recipe recipe);
    }


    // inflation happens inside onCreateView


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onItemSelected(View view, int position) {
        Recipe recipe = recipes.get(position);

        Toast.makeText(getContext(), recipe.title, Toast.LENGTH_SHORT).show();

    }

    public void populateTimeline() {
        return;
    }

    public void displayRecipes() {}

    public void onNewRecipeAvailable(Recipe recipe){
        recipes.add(0, recipe);
        recipeAdapter.notifyItemInserted(0);
        rvRecipes.scrollToPosition(0);
    }

}
