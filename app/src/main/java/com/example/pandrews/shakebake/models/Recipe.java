package com.example.pandrews.shakebake.models;

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
