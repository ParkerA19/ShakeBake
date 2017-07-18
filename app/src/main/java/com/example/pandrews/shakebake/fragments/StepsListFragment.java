package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.InstructionsAdapter;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/18/17.
 */

public class StepsListFragment extends Fragment {
    //    // Instance variables
    static InstructionsAdapter instructionsAdapter;
    static InstructionsPagerAdapter instructionsPagerAdapter;
    public static ArrayList<String> Steps;
    static RecyclerView rvSteps;

//    public SwipeRefreshLayout swipeContainer;

    // inflation happens inside onCreateView

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // inflate the layout
//        View v = inflater.inflate(R.layout.fragments_steps_list, container, false);
//
////        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer3);
//////        // setup refresh listener which triggers new data loading
////        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
////            @Override
////            public void onRefresh() {
////                populateTimeline();
////            }
////        });
////
////        // Configure the refeshing colors
////        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
////                android.R.color.holo_green_light,
////                android.R.color.holo_orange_light,
////                android.R.color.holo_red_light);
//
//
//        // find the RecyclerView
//        rvSteps = (RecyclerView) v.findViewById(R.id.rvStep);
//        // init the arraylist (data source)
//        Steps = new ArrayList<>();
//        // init the recipe
//        Recipe = ;
//        // construct the adapter from this data source
//        instructionsAdapter = new InstructionsAdapter(Steps, Recipe);
//        // RecyclerView setup (layout manger, user adapter)
//        rvSteps.setLayoutManager(new LinearLayoutManager(getContext()));
//        // set the adapter
//        rvSteps.setAdapter(instructionsAdapter);
//
//        return v;
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void populateTimeline() {
        return;
    }


}
