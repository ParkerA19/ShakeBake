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
        populateTimeline();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void populateTimeline() {
        User u1 = new User("Parker", "pandrews", null, 10, 20);
        User u2 = new User("Andrea", "agarcia", null, 15, 30);
        User u3 = new User("Jennifer", "jshin", null, 20, 40);

        Recipe r1 = new Recipe("Cereal", "Cinammon Toast Crunch", u1, null, 200, "milk and stuff", "Pour milk \n eat Cereal \n repeat until full");
        Recipe r2 = new Recipe("Mangos", "round juicy fruit", u2, null, 300, "fruit and juice", "bite \n bite again \n finish \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo \n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooovv\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooovvvvvv\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo\n brooooooooooooooooooooooooooooooo");
        Recipe r3 = new Recipe("Sushi", "Dead Fish", u3, null, 220, "fish, rice, seaweed, wasabi", "cover seaweed in rice \n wrap fish with seaweed (which should now be covered in rice.. if this is not the case then you missed the only step so far and should probably try making something else) \n apply wasbi \n eat");
        Recipe r4 = new Recipe();


        if (recipeAdapter != null) {

            recipeAdapter.clear();

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
    }


    public void appendTweet(Recipe recipe) {
        // add a tweet
        recipes.add(0, recipe);
        // inserted at position 0
        recipeAdapter.notifyItemInserted(0);
        // do work
        rvRecipes.scrollToPosition(0);
    }


}
