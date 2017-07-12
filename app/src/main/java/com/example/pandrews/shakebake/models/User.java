package com.example.pandrews.shakebake.models;

import org.parceler.Parcel;

/**
 * Created by pandrews on 7/10/17.
 */
@Parcel
public class User {

    public String name;
    public String username;
    public String profileImageUrl;
    public int followersCount;
    public int followingCount;

    public User(String n, String un, String piu, int fs, int fg) {
        this.name = n;
        this.username = un;
        this.profileImageUrl = piu;
        this.followersCount = fs;
        this.followingCount = fg;
    }

    public User() {
        this.name = "Zuck";
        this.username = "quack_like_a_zuck";
        this.profileImageUrl = null;
        this.followersCount = 100;
        this.followingCount = 85;
    }
}
