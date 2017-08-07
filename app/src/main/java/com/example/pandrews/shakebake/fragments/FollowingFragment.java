package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.pandrews.shakebake.MainActivity;
import com.example.pandrews.shakebake.models.User;

/**
 * Created by pandrews on 7/13/17.
 */

public class FollowingFragment extends FriendsListFragment {

    User profile = MainActivity.profile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    @Override
    public void populateTimeline() {
        User u = new User();
        User u1 = new User("Parker", "pandrews", "https://static.pexels.com/photos/404843/pexels-photo-404843.jpeg" , 10, 20, 300);
        User u2 = new User("Andrea", "agarcia", "https://static.pexels.com/photos/163114/mario-luigi-figures-funny-163114.jpeg", 15, 30, 450);
        User u3 = new User("Jennifer", "jshin", "https://static.pexels.com/photos/437886/pexels-photo-437886.jpeg", 20, 40, 700);
        User u4 = new User();


        friendAdapter.clear();

        for (int index = 0; index < profile.following.size(); index ++) {
            users.add(profile.following.get(index));
            friendAdapter.notifyItemInserted(users.size() -1);
        }

//        users.add(u);
//        friendAdapter.notifyItemInserted(users.size() -1);
//
//        users.add(u1);
//        friendAdapter.notifyItemInserted(users.size() -1);
//
//        users.add(u2);
//        friendAdapter.notifyItemInserted(users.size() -1);
//
//        users.add(u3);
//        friendAdapter.notifyItemInserted(users.size() -1);
//
//        users.add(u4);
//        friendAdapter.notifyItemInserted(users.size() -1);

        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onStart() {
        populateTimeline();
        super.onStart();
    }
}
