package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pandrews.shakebake.FriendsActivity;
import com.example.pandrews.shakebake.MainActivity;
import com.example.pandrews.shakebake.models.User;

/**
 * Created by pandrews on 7/13/17.
 */

public class FollowingFragment extends FriendsListFragment {

    User profile = MainActivity.profile;
    User user = FriendsActivity.user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void populateTimeline() {

        friendAdapter.clear();

        for (int index = 0; index < user.following.size(); index++) {
            users.add(user.following.get(index));
            friendAdapter.notifyItemInserted(users.size() - 1);
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
