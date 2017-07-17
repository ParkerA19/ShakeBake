package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

/**
 * Created by pandrews on 7/10/17.
 */

public class HomeTimelineFragment extends RecipesListFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void populateTimeline() {
        User u1 = new User("Parker", "pandrews", "https://static.pexels.com/photos/404843/pexels-photo-404843.jpeg" , 10, 20, 300);
        User u2 = new User("Andrea", "agarcia", "https://static.pexels.com/photos/163114/mario-luigi-figures-funny-163114.jpeg", 15, 30, 450);
        User u3 = new User("Jennifer", "jshin", "https://static.pexels.com/photos/437886/pexels-photo-437886.jpeg", 20, 40, 700);

        Recipe r1 = new Recipe("Cereal", "Cinammon Toast Crunch", u1, "https://pbs.twimg.com/media/Bv6uxxaCcAEjWHD.jpg", 200, false, "milk and stuff", "Pour milk \n eat Cereal \n repeat until full");
        Recipe r2 = new Recipe("Mangos", "round juicy fruit", u2, "https://static.pexels.com/photos/462402/pexels-photo-462402.jpeg", 300, true, "fruit and juice", "bite \n bite again \n finish \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooovv\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooovvvvvv\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo");
        Recipe r3 = new Recipe("Sushi", "Dead Fish", u3, "https://static.pexels.com/photos/42224/accord-acoustic-art-background-42224.jpeg", 220, true, "fish, rice, seaweed, wasabi", "cover seaweed in rice \n wrap fish with seaweed (which should now be covered in rice.. if this is not the case then you missed the only step so far and should probably try making something else) \n apply wasbi \n eat");
        Recipe r4 = new Recipe();


     //   recipeAdapter.clear();

        recipes.add(r1);
        recipeAdapter.notifyItemInserted(recipes.size() -1);

        recipes.add(r2);
        recipeAdapter.notifyItemInserted(recipes.size() -1);

        recipes.add(r3);
        recipeAdapter.notifyItemInserted(recipes.size() -1);

        recipes.add(r4);
        recipeAdapter.notifyItemInserted(recipes.size() -1);

        swipeContainer.setRefreshing(false);

    }


    public void appendRecipe(Recipe recipe) {
        // add a tweet
        recipes.add(0, recipe);
        // inserted at position 0
        recipeAdapter.notifyItemInserted(0);
        // do work
        rvRecipes.scrollToPosition(0);
    }

    @Override
    public void onStart() {
        populateTimeline();
        super.onStart();
    }
}
