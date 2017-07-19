package com.example.pandrews.shakebake.models;

import android.os.Bundle;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/10/17.
 */

@Parcel
public class Recipe {

    public String title;
    public String description;
    public User user;
    public String mediaurl;
    public Integer forkCount;
    public ArrayList<String> keywords = new ArrayList<>();
    public ArrayList<String> steps = new ArrayList<>();
    public String targetUri;
    public ArrayList<String> ingredients = new ArrayList<>();
    public boolean forked;

    public Recipe(String t, String d, User u, String m, int fc,boolean b, ArrayList<String> i, ArrayList<String> s, ArrayList<String> keys) {
        this.title = t;
        this.description = d;
        this.user = u;
        this.mediaurl = m;
        this.forkCount = fc;
        this.ingredients = i;
        this.steps = s;
        this.forked = b;
        this.keywords = keys;

    }

    //get stuff from bundle
    public static Recipe fromBundle(Bundle bundle) {
        Recipe recipe = new Recipe();

        recipe.ingredients= bundle.getStringArrayList("supplyList");
        recipe.steps = bundle.getStringArrayList("stepList");
        recipe.title = bundle.getString("title");
        recipe.description = bundle.getString("description");
        recipe.keywords = bundle.getStringArrayList("keywords");
        if (bundle.getString("targetUri") != null) {
            recipe.targetUri = bundle.getString("targetUri");
        }
        recipe.forked = bundle.getBoolean("forked");
        if (bundle.getString("forkCount") != null) {
            recipe.forkCount = Integer.parseInt(bundle.getString("forkCount"));
        } else {
            recipe.forkCount = 0;
        }
        recipe.user = new User();
        if (bundle.getString("mediaurl") != null) {
            recipe.mediaurl = bundle.getString("mediaurl");
        } else {
            recipe.mediaurl = null;
        }
        return recipe;
    }


    public Recipe() {
        this.title = "Grapes";
        this.description = "green or red juicy things";
        this.user = new User();
        this.mediaurl = "https://pbs.twimg.com/media/Bv6uxxaCcAEjWHD.jpg";
        this.forkCount = 10;
        this.ingredients.add(0, "... grapes");
        this.steps.add(0, ".....its a grape just eat it");
        this.forked = false;
        this.keywords.add(0, "juicy");
        this.keywords.add(1, "green");
        this.keywords.add(2, "yummy");
    }
}
