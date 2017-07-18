package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.models.User;

import static com.example.pandrews.shakebake.fragments.RecipesListFragment.recipeAdapter;

/**
 * Created by pandrews on 7/13/17.
 */

public class FollowersFragment extends FriendsListFragment {

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
        User u4 = new User();


        recipeAdapter.clear();

        users.add(u1);
        recipeAdapter.notifyItemInserted(users.size() -1);

        users.add(u2);
        recipeAdapter.notifyItemInserted(users.size() -1);

        users.add(u3);
        recipeAdapter.notifyItemInserted(users.size() -1);

        users.add(u4);
        recipeAdapter.notifyItemInserted(users.size() -1);

        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onStart() {
        populateTimeline();
        super.onStart();
    }
}
