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
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setRecipe(spaRecipe);
            ingredientsFragment.setIngredients(spaRecipe.ingredients);
            ingredientsFragment.setClickable(clickable);
            return ingredientsFragment;
        }
        else if (position == 1) {
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setRecipe(spaRecipe);
            stepsFragment.setSteps(spaRecipe.steps);
            stepsFragment.setClickable(clickable);
            return stepsFragment;
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