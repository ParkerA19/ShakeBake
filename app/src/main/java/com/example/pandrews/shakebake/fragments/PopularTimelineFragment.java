package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

/**
 * Created by pandrews on 7/10/17.
 */

public class PopularTimelineFragment extends RecipesListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeline();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    @Override
    public void populateTimeline() {
        User u1 = new User("Kevin", "kwong", null, 10, 20);
        User u2 = new User("Jim", "jim", null, 15, 30);
        User u3 = new User("Greg", "greg", null, 20, 40);
        User u4 = new User("Allison", "allison" ,null, 25, 50);


        Recipe r1 = new Recipe("Peaches", "good fruit", u1, null, 200,true, "wow", "wow wow");
        Recipe r2 = new Recipe("Pasta", "with pesto alfredo sauce", u2, null, 300, false, "wow", "wow wow");
        Recipe r3 = new Recipe("Shrimp", "mmmmmmm", u3, null, 220, true, "wow", "wow wow");
        Recipe r4 = new Recipe("Bananas", "yellow fruit", u4, null, 400, false, "wow", "wow wow");

        if (recipeAdapter != null) {
            recipeAdapter.clear();

            recipes.add(r1);
            recipeAdapter.notifyItemInserted(recipes.size() - 1);

            recipes.add(r2);
            recipeAdapter.notifyItemInserted(recipes.size() - 1);

            recipes.add(r3);
            recipeAdapter.notifyItemInserted(recipes.size() - 1);

            recipes.add(r4);
            recipeAdapter.notifyItemInserted(recipes.size() - 1);


            swipeContainer.setRefreshing(false);
        }

    }


}
