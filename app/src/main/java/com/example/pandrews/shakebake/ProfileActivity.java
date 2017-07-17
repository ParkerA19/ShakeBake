package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.fragments.UserTimelineFragment;
import com.example.pandrews.shakebake.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private final int REQUEST_CODE = 25;

    // Instance Variables
    User user;
    Context context;
    User profile;

    // set up view for butterknife
    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvForks) TextView tvForks;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // bind with butterknife
        ButterKnife.bind(this);


        // set the context
        context = getApplicationContext();

        // set the profile user
        profile = MainActivity.profile;

        // set the user and username
        String screenname;
        user = Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));

        screenname = user.username;

        // create the user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenname);
        // display the user timeline fragment inside the container (dynamically)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flContainer, userTimelineFragment);

        // commit the transaction
        ft.commit();

        // set the navigation view
        setNavigationView();

        // populate the user headline
        populateUserHeadline(user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    public void setNavigationView() {
        // set the toolbar at the top
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // draw the navigation item
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // set up the navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // access the header view to set the text according to the user details
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.tvName);
        TextView username = (TextView) header.findViewById(R.id.tvUsername);
        ImageView Image = (ImageView) header.findViewById(R.id.ivProfileImage);

        // set text
        name.setText(profile.name);
        username.setText(profile.username);

        // set profile image
        Glide.with(getApplicationContext())
                .load(profile.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 25, 0))
                .into(Image);

        // setup onClick for the profile view
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make and intent
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                // add the user
                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(profile));
                // start activity
                startActivity(intent);
            }
        });
    }

    public void populateUserHeadline(User user) {
        tvName.setText(user.name);
        tvUsername.setText(user.username);
        tvForks.setText(user.forkCount + " Forks");
        tvFollowers.setText(user.followersCount + " Followers");
        tvFollowing.setText(user.followingCount + " Following");

        if (user.profileImageUrl != null) {
            Glide.with(this)
                    .load(user.profileImageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                    .into(ivProfileImage);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.nav_activity_add_recipe:
                onCreateRecipeView(item);
                return true;
            default:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }


    public void onCreateRecipeView(MenuItem item) {
        Intent i = new Intent(this, AddRecipeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onFollowing(View v) {
        // make a new intent
        Intent intent = new Intent(context, FriendsActivity.class);
        // start activity
        startActivity(intent);
    }
    
    public void onFollowers(View v) {
        // make a new intent
        Intent intent = new Intent(context, FriendsActivity.class);
        // start activity
        startActivity(intent);
    }
}
