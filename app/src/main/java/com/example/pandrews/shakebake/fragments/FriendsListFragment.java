package com.example.pandrews.shakebake.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pandrews.shakebake.FriendAdapter;
import com.example.pandrews.shakebake.R;
import com.example.pandrews.shakebake.models.User;

import java.util.ArrayList;

/**
 * Created by pandrews on 7/13/17.
 */

public class FriendsListFragment extends Fragment implements FriendAdapter.FriendAdapterListener {

    public interface FriendSelectedListener {
        // handle recipe selection
        public void onFriendSelected(User user);
    }

    // Instance variables
    static FriendAdapter friendAdapter;
    public ArrayList<User> users;  //make sure this variable can be static
    static RecyclerView rvFriends;

    public SwipeRefreshLayout swipeContainer;

    // inflation happens inside onCreateView


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        View v = inflater.inflate(R.layout.fragments_friends_list, container, false);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer2);
        // setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }
        });

        // Configure the refeshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // find the RecyclerView
        rvFriends = (RecyclerView) v.findViewById(R.id.rvFriend);
        // init the arraylist (data source)
        users = new ArrayList<>();
        // construct the adapter from this data source
        friendAdapter = new FriendAdapter(users, this);
        // RecyclerView setup (layout manger, user adapter)
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvFriends.setAdapter(friendAdapter);

        return v;
    }

    public void populateTimeline() {
        return;
    }

    @Override
    public void onItemSelected(View view, int position) {
        User user = users.get(position);

        Toast.makeText(getContext(), user.name, Toast.LENGTH_SHORT).show();
    }
}
