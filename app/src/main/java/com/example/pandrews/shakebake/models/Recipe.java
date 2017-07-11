package com.example.pandrews.shakebake.models;

/**
 * Created by pandrews on 7/10/17.
 */

public class Recipe {

    public String title;
    public String description;
    public User user;
    public String mediaurl;
    public int forkCount;

    public Recipe(String t, String d, User u, String m, int fc) {
        this.title = t;
        this.description = d;
        this.user = u;
        this.mediaurl = m;
        this.forkCount = fc;
    }


}
