package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.InstructionsAdapter;
import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.models.Recipe;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/18/17.
 */

public class IngredientsFragment extends Fragment {

    // Instance varbiables
    ArrayList<String> ingredients;
    Recipe recipe;
    int fragmentPosition = 0;


    static InstructionsAdapter instructionsAdapter;
    static InstructionsPagerAdapter instructionsPagerAdapter;
    public static ArrayList<String> Steps;
    static RecyclerView rvSteps;

    // constructor
    public IngredientsFragment(ArrayList<String> Ingredients, Recipe Recipe){
        ingredients = Ingredients;
        recipe = Recipe;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        View v = inflater.inflate(R.layout.fragments_steps_list, container, false);

        // find the RecyclerView
        rvSteps = (RecyclerView) v.findViewById(R.id.rvStep);
        // init the arraylist (data source)
        Steps = new ArrayList<>();
        // construct the adapter from this data source
        instructionsAdapter = new InstructionsAdapter(Steps, recipe, fragmentPosition);
        // RecyclerView setup (layout manger, user adapter)
        rvSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvSteps.setAdapter(instructionsAdapter);

        return v;
    }

    public void populateTimeline() {
   //      instructionsAdapter.clear();

        for (int i = 0; i < ingredients.size(); i++) {
            Steps.add(ingredients.get(i));
            instructionsAdapter.notifyItemInserted(Steps.size() -1);
        }
    }

    @Override
    public void onStart() {
        populateTimeline();
        super.onStart();
    }
}