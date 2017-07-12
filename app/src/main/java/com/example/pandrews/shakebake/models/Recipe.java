package com.example.pandrews.shakebake.models;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/10/17.
 */

public class Recipe {

    public String title;
    public String description;
    public User user;
    public String mediaurl;
    public int forkCount;
    public String keywords;
    public ArrayList<String> stepList;
    public ArrayList<String> supplyList;
    public String targetUri;

    public Recipe(String t, String d, User u, String m, int fc) {
        this.title = t;
        this.description = d;
        this.user = u;
        this.mediaurl = m;
        this.forkCount = fc;
    }

    //get stuff from bundle
    public static Recipe fromBundle(Bundle bundle) {
        Recipe recipe = new Recipe();

        recipe.supplyList= bundle.getStringArrayList("supplyList");
        recipe.stepList = bundle.getStringArrayList("stepsList");
        recipe.title = bundle.getString("title");
        recipe.description = bundle.getString("description");
        recipe.keywords = bundle.getString("keywords");
        recipe.targetUri = bundle.getString("targetUri");
        return recipe;
    }

    public Recipe() {
        this.title = "grapes";
        this.description = "fruit";
        this.forkCount = 20;
    }

}
