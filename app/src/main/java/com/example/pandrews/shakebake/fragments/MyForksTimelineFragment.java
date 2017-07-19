package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.MyForksAdapter;
import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.pandrews.shakebake.fragments.HomeTimelineFragment.r2;

/**
 * Created by pandrews on 7/11/17.
 */

public class MyForksTimelineFragment extends RecipesListFragment implements MyForksAdapter.ForkAdapterListener {

    static User u1 = new User("Kevin", "kwong", null, 10, 20, 123);
    static User u2 = new User("Jim", "jim", null, 15, 30, 50);
    static User u3 = new User("Greg", "greg", null, 20, 40, 456);
    static User u4 = new User("Allison", "allison" ,null, 25, 50, 743);

    static ArrayList<String> r1iList = new ArrayList<>(Arrays.asList("wow"));
    static ArrayList<String> r1sList = new ArrayList<>(Arrays.asList("wow", "wow"));

    static Recipe r1 = new Recipe("Peaches", "good fruit", u1, null, 200,true, r1iList, r1sList, null);
    static Recipe r3 = new Recipe("Shrimp", "mmmmmmm", u3, null, 220, true, r1iList, r1sList, null);
    static Recipe r4 = new Recipe("Bananas", "yellow fruit", u4, null, 400, false, r1iList, r1sList, null);


    // Instance variables
    static MyForksAdapter forksAdapter;
    public static ArrayList<Recipe> forks = new ArrayList<>(Arrays.asList(r1, r2, r3, r4));
    static RecyclerView rvForks;
    public SwipeRefreshLayout swipeContainer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_my_forks, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                forksAdapter.clear();
                populateTimeline();
            }
        });

// Configure the refeshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // find the RecyclerView
        rvForks = (RecyclerView) v.findViewById(R.id.rvForks);
        // init the arraylist (data source)
        forks = new ArrayList<>();
        // construct the adapter from this data source
        forksAdapter = new MyForksAdapter(forks, this);
        // RecyclerView setup (layout manger, user adapter)
        rvForks.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvForks.setAdapter(forksAdapter);

        return v;
    }

    public void populateTimeline() {

        User u1 = new User("Kevin", "kwong", null, 10, 20, 123);
        User u2 = new User("Jim", "jim", null, 15, 30, 50);
        User u3 = new User("Greg", "greg", null, 20, 40, 456);
        User u4 = new User("Allison", "allison" ,null, 25, 50, 743);


        r1iList.add(0,"wow");
        r1sList.add(0, "wow wow");

        Recipe r1 = new Recipe("Peaches", "good fruit", u1, null, 200,true, r1iList, r1sList, r1iList);
        Recipe r2 = new Recipe("Pasta", "with pesto alfredo sauce", u2, null, 300, false, r1iList, r1sList, r1sList);
        Recipe r3 = new Recipe("Shrimp", "mmmmmmm", u3, null, 220, true, r1iList, r1sList, r1sList);
        Recipe r4 = new Recipe("Bananas", "yellow fruit", u4, null, 400, false, r1iList, r1sList, null);

        forksAdapter.clear();

        forks.add(r1);
        forksAdapter.notifyItemInserted(forks.size() - 1);

        forks.add(r2);
        forksAdapter.notifyItemInserted(forks.size() - 1);

        forks.add(r3);
        forksAdapter.notifyItemInserted(forks.size() - 1);

        forks.add(r4);
        forksAdapter.notifyItemInserted(forks.size() - 1);

        swipeContainer.setRefreshing(false);


    }

    @Override
    public void onStart() {
        populateTimeline();
        super.onStart();
    }

    @Override
    public void onItemSelected(View view, int position) {
    }
}
