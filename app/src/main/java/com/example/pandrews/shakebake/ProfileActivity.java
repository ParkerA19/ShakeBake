package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.pandrews.shakebake.fragments.UserTimelineFragment;
import com.example.pandrews.shakebake.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    // put back into activity_profile.xml
//    <android.support.design.widget.NavigationView
//        android:id="@+id/nav_view"
//        android:layout_width="wrap_content"
//        android:layout_height="match_parent"
//        android:layout_gravity="start"
//        android:fitsSystemWindows="true"
//        android:background="@drawable/background"
//        app:headerLayout="@layout/nav_header_side"
//        app:menu="@menu/activity_side_drawer" />


    private final int REQUEST_CODE = 25;

    // Instance Variables
    User user;
    Context context;
    User profile;

    // set up view for butterknife
    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvPosts) TextView tvPosts;
    @BindView(R.id.tvPostCount) TextView tvPostCount;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowersCount) TextView tvFollowersCount;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.tvFollowingCount) TextView tvFollowingCount;
    @BindView(R.id.flContainer) FrameLayout flContainer;
    @BindView(R.id.scroll) NestedScrollView scrollView2;
    @BindView(R.id.rlUserHeader) RelativeLayout rlUserHeader;
    @BindView(R.id.cvCard) CardView cvCard;
    @BindView(R.id.rootview) CoordinatorLayout rootview;
    @BindView(R.id.drawer_layout3) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;


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

        setRecipeTimeline(screenname);

        // set the navigation view
        setNavigationView();

        // populate the user headline
        populateUserHeadline(user);

        collapsingToolbarLayout.setTitleEnabled(false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    public void setRecipeTimeline(String screenname) {
        // create the user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenname);
        // display the user timeline fragment inside the container (dynamically)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flContainer, userTimelineFragment);

        // commit the transaction
        ft.commit();
    }

    public void setNavigationView() {
//        // set the toolbar at the top
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.tv_title);
//        toolbarTitle.setText("PROFILE");

        // set the toolbar at the top
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        toolbarTitle.setText("PROFILE");
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



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
        final ImageView Image = (ImageView) header.findViewById(R.id.ivProfileImage);

        // set text
        name.setText(profile.name);
        username.setText(profile.username);

        // set profile image
        Glide.with(getApplicationContext())
                .load(profile.profileImageUrl)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(Image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        Image.setImageDrawable(circularBitmapDrawable);
                    }
                });

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
                // set the animation
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                // close the navigation view
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    public void populateUserHeadline(User user) {
        tvName.setText(user.name);
        tvUsername.setText(user.username);
//        tvPosts.setText("Posts");
//        tvFollowers.setText("Followers");
//        tvFollowing.setText("Following");
        tvPostCount.setText(user.forkCount + "");
        tvFollowersCount.setText(user.followersCount + "");
        tvFollowingCount.setText(user.followingCount + "");

        if (user.profileImageUrl != null) {
            Glide.with(this)
                    .load(user.profileImageUrl)
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(ivProfileImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivProfileImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                // set the new animation
                overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
                // close the navigation view
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_activity_add_recipe:
                onCreateRecipeView(item);
                // close the navigation view
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_search:
                return true;
            case R.id.nav_find_friends:
                return true;
            case R.id.nav_help:
                return true;
            case R.id.nav_settings:
                return true;
            case R.id.nav_logout:
                // Pass in the click listener when displaying the Snackbar
                Snackbar.make(drawerLayout, R.string.snackbar_text, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.snackbar_action, myOnClickListener)
                        .setActionTextColor(getResources().getColor(R.color.appFontLogout))
                        .show(); // Don’t forget to show!

                return true;
            default:
                // close the navigation view
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }
    }


    /**
    on click listener for the snackbar
    when you click it will confirm the logout and bring user to the login activity
     */
    View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logout();
        }
    };

    /**
    starts the AddRecipeActivity for result
    called in onNavigationSelected
     */
    public void onCreateRecipeView(MenuItem item) {
        Intent i = new Intent(this, AddRecipeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    /**
    takes user back to the logout screen
    called in myOnClickListener
     */
    public void logout() {
        // make a new intent
        Intent intent = new Intent(getApplicationContext(), OpeningActivity.class);
        // make sure the BackStack is cleared
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // start activity with new intent for the login activity
        startActivity(intent);
        // set the new animation
        overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
    }

    /**
    when following is pressed take user to view users they follow
    goes to a tab layout where they can switch between followers and following
     */
    public void onFollowing(View v) {
        // set the tab position for FriendsActivity
        int position = 1;
        // make a new intent
        Intent intent = new Intent(context, FriendsActivity.class);
        // put position into the intent
        intent.putExtra("int", position);
        // start activity
        startActivity(intent);
    }

    /**
   when followers is pressed take user to view users who follow them
   goes to a tab layout where they can switch between followers and following
    */
    public void onFollowers(View v) {
        // set the tab position for FriendsActivity
        int position = 0;
        // make a new intent
        Intent intent = new Intent(context, FriendsActivity.class);
        // put position into the intent
        intent.putExtra("int", position);
        // start activity
        startActivity(intent);
    }

    /**
     * Method to say what to do when back is pressed
     * Override in this class if just to set the animations for the transitions between activities
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
