package com.example.pandrews.shakebake.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.pandrews.shakebake.SmartFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pandrews on 7/10/17.
 */

public class RecipesPagerAdapter extends SmartFragmentStatePagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Popular", "My Forks"};
    private Context context;
    public List<RecipesListFragment> recipesList = new ArrayList<>();

    public RecipesPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        }
        else if (position == 1) {
            return new PopularTimelineFragment();
        }
        else if (position == 2) {
            return new MyForksTimelineFragment();
        }
        else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    // return the title of the tab
    public CharSequence getPageTitle(int position) {
        // generate title based on item position
        return tabTitles[position];
    }
}









//    HomeTimelineFragment homeTimeline = new HomeTimelineFragment();
//            recipesList.add(position, homeTimeline);
//                    return homeTimeline;