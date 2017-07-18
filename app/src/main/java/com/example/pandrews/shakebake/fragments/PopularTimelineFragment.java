package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.PopularAdapter;
import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pandrews on 7/10/17.
 */

public class PopularTimelineFragment extends RecipesListFragment implements PopularAdapter.PopularAdapterListener {

    static ArrayList<String> r1iList = new ArrayList<>(Arrays.asList("wow"));
    static ArrayList<String> r1sList = new ArrayList<>(Arrays.asList("wow", "wow"));

    static User u1 = new User("Kevin", "kwong", null, 10, 20, 123);
    static User u2 = new User("Jim", "jim", null, 15, 30, 50);
    static User u3 = new User("Greg", "greg", null, 20, 40, 456);
    static User u4 = new User("Allison", "allison" ,null, 25, 50, 743);



    static Recipe r1 = new Recipe("Peaches", "good fruit", u1, null, 200,true, r1iList, r1sList);
    static Recipe r2 = new Recipe("Pasta", "with pesto and alfredo sauce", u2, null, 300, false, r1iList, r1sList);
    static Recipe r3 = new Recipe("Shrimp", "mmmmmmm", u3, null, 220, true, r1iList, r1sList);
    static Recipe r4 = new Recipe("Bananas", "yellow fruit", u4, null, 400, false, r1iList, r1sList);

    //instance variables
    static PopularAdapter popularAdapter;
    public static ArrayList<Recipe> popular = new ArrayList<>(Arrays.asList(r1, r2, r3, r4));
    static RecyclerView rvPopular;
    public SwipeRefreshLayout swipeContainer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_popular, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
           //     popularAdapter.clear();
                populateTimeline();
            }
        }); //eventually change so that onrefresh does not call populate timeline but instead calls another method that gets more recipes (is line needed since first call is made in onStart-- TODO

        // Configure the refeshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // find the RecyclerView
        rvPopular = (RecyclerView) v.findViewById(R.id.rvPopular);
        // init the arraylist (data source)
        //popular = new ArrayList<>();
        // construct the adapter from this data source
        popularAdapter = new PopularAdapter(popular, this);
        // RecyclerView setup (layout manger, user adapter)
        rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvPopular.setAdapter(popularAdapter);

        return v;
    }

    public void populateTimeline() {
        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onStart() {
        //populateTimeline();
        super.onStart();
    }

}






