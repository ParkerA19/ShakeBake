package com.example.pandrews.shakebake.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.pandrews.shakebake.SmartFragmentStatePagerAdapter;
import com.example.pandrews.shakebake.models.Recipe;

/**
 * Created by pandrews on 7/18/17.
 */

public class InstructionsPagerAdapter extends SmartFragmentStatePagerAdapter {

    private String tabTitles[] = new String[] {"Ingredients", "Steps"};
    private Context context;
    public Recipe spaRecipe;
    public boolean clickable;

    public InstructionsPagerAdapter(FragmentManager fm, Context context, Recipe recipe, boolean click){
        super(fm);
        this.context = context;
        spaRecipe = recipe;
        clickable = click;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new IngredientsFragment(spaRecipe.ingredients, spaRecipe, clickable);
        }
        else if (position == 1) {
            return new StepsFragment(spaRecipe.steps, spaRecipe, clickable);
        }
        else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    // return the title of the tab
    public CharSequence getPageTitle(int position) {
        // generate title based on item position
        return tabTitles[position];
    }
}