package com.example.pandrews.shakebake.models;

import org.parceler.Parcel;

import java.util.ArrayList;

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
    public int forkCount;
    public ArrayList<User> followers;
    public ArrayList<User> following;

    public User(String n, String un, String piu, int fs, int fg, int fc) {
        this.name = n;
        this.username = un;
        this.profileImageUrl = piu;
        this.followersCount = fs;
        this.followingCount = fg;
        this.forkCount = fc;
    }

    public User() {
        this.name = "Mark Zuckerberg";
        this.username = "quack_like_a_zuck";
        this.profileImageUrl = "https://harvardgazette.files.wordpress.com/2017/03/mark-zuckerberg-headshot-11.jpg";
        this.followersCount = 100;
        this.followingCount = 85;
        this.forkCount = 220;
        this.followers = null;
        this.following = null;
    }

    public void setFollowers(ArrayList<User> users) {
        this.followers = users;
    }

    public void setFollowing(ArrayList<User> users) {
        this.following = users;
    }

    public boolean equals(User person) {
        if (this.name.equals(person.name) && this.username.equals(person.username))
            return true;

        return false;
    }
}


