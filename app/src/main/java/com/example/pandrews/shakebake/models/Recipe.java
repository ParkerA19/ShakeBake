package com.example.pandrews.shakebake.models;

import android.os.Bundle;

import java.util.ArrayList;
import org.parceler.Parcel;

/**
 * Created by pandrews on 7/10/17.
 */

@Parcel
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
    public String ingredients;
    public String steps;
    public boolean forked;

    public Recipe(String t, String d, User u, String m, int fc,boolean b, String i, String s) {
        this.title = t;
        this.description = d;
        this.user = u;
        this.mediaurl = m;
        this.forkCount = fc;
        this.ingredients = i;
        this.steps = s;
        this.forked = b;

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
        this.title = "Grapes";
        this.description = "green or red juicy things";
        this.user = new User();
        this.mediaurl = null;
        this.forkCount = 10;
        this.ingredients = "... grapes";
        this.steps = ".....its a grape just eat it";
        this.forked = false;
    }

}
