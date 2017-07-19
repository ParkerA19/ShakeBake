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

/**
 * Created by pandrews on 7/12/17.
 */

public class UserTimelineFragment extends RecipesListFragment {

    public SwipeRefreshLayout swipeContainer;


    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        View v = inflater.inflate(R.layout.fragments_recipes_list, container, false);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
//        // setup refresh listener which triggers new data loading
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
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        populateTimeline();
        super.onStart();
    }

    public void populateTimeline() {


        ArrayList<String> r1iList = new ArrayList<>();
        ArrayList<String> r1sList = new ArrayList<>();

        ArrayList<String> r2iList = new ArrayList<>();
        ArrayList<String> r2sList = new ArrayList<>();

        ArrayList<String> r3iList = new ArrayList<>();
        ArrayList<String> r3sList = new ArrayList<>();

        ArrayList<String> r4iList = new ArrayList<>();
        ArrayList<String> r4sList = new ArrayList<>();

        r1iList.add("milk");
        r1iList.add("stuff");

        r1sList.add("Pour milk");
        r1sList.add("eat cereal");
        r1sList.add("repeat until full");

        r2iList.add("fruit");
        r2iList.add("juice");

        r2sList.add("bite");
        r2sList.add("bite again");
        r2sList.add("finish");
        r2sList.add("broooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");

        r3iList.add("fish");
        r3iList.add("rice");
        r3iList.add("seaweed");
        r3iList.add("wasabi");


        r3sList.add("cover seaweed in rice");
        r3sList.add("wrap fish with seaweed (which should now be covered in rice.. if this is not the case then you missed the only step so far and should probably try making something else)");
        r3sList.add("apply wasabi");
        r3sList.add("eat");

        r4iList.add("fruit");
        r4iList.add("juice");

        r4sList.add("bite");
        r4sList.add("bite again");
        r4sList.add("finish");

        User u1 = new User("Parker", "pandrews", null, 10, 20, 300);
        User u2 = new User("Andrea", "agarcia", null, 15, 30, 450);
        User u3 = new User("Jennifer", "jshin", null, 20, 40, 700);

        Recipe r1 = new Recipe("Cereal", "Cinammon Toast Crunch", u1, "https://pbs.twimg.com/media/Bv6uxxaCcAEjWHD.jpg", 200, false, r1iList, r1sList, r1sList);
        Recipe r2 = new Recipe("Mangos", "round juicy fruit", u2, "https://pbs.twimg.com/media/Bv6uxxaCcAEjWHD.jpg", 300, true, r2iList, r2sList, r2iList);
        Recipe r3 = new Recipe("Sushi", "Dead Fish", u3, null, 220, true, r3iList, r3sList, null);
        Recipe r4 = new Recipe();


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
