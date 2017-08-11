package com.example.pandrews.shakebake;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.fragments.HomeTimelineFragment;
import com.example.pandrews.shakebake.fragments.RecipesPagerAdapter;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;
import com.example.pandrews.shakebake.utils.CircleGlide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_CODE = 25;

    // Instance variables
    RecipesPagerAdapter adapterViewPager;
    private FirebaseAnalytics mFirebaseAnalytics;
    //MenuItem miSearch;
    //SearchView searchView;  ---- maybe delete these lines -- TODO

    public static User profile;

    FirebaseDatabase database =FirebaseDatabase.getInstance();


    ArrayList<User> followersList = new ArrayList<>();
    ArrayList<User> followingList = new ArrayList<>();

    //for shake listener
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    ArrayList<MediaStore.Video> videoArrayList;
    VideoView vvVideo;

    //private StorageReference storageReference;


    @BindView(R.id.viewpager) ViewPager vpPager;
    @BindView(R.id.sliding_tabs) TabLayout tabLayout;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind with butterknife
        ButterKnife.bind(this);

        // for now just set a mock user to be the profile in the navigation view
        profile = new User();

        // set the followers and following list
        getFollowers();

        // set the navigation view
        setNavigationView();

        // set the adapter for the pager
        adapterViewPager = new RecipesPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(adapterViewPager);

        // setup the TabLayout to use the viewPager -- bound with butterknife
        //  TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

        //init storage
        //storageReference = FirebaseStorage.getInstance().getReference("videos");

        //initialize analytics
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // set up the shake feature
        setShake();
    }

    /**
     * Method to find which user matches up with the current user
     * retrieves the followers and following list
     */
    public void getFollowers() {
        //create database reference
        DatabaseReference myRef = database.getReference("Users");


        //create listener. this one adds all recipes currently in database w/fork count above 300
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    User newUser = postSnapshot.getValue(User.class);
//                    Log.d("profile", "profile");
                    String newUserKey = postSnapshot.getKey();

                    // check if this is the right user
                    if (newUserKey.equals(profile.username)) {
                        // now look through all the aspects of the user
                        for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                            // get the key
                            String tempKey = childSnapshot.getKey();
                            // if the key is the followers then set accordingly
                            if (tempKey.equals("followers")) {
                                // look through each user and add them to the followersList
                                for (DataSnapshot infantSnapShot : childSnapshot.getChildren()) {
                                    User tempUser = infantSnapShot.getValue(User.class);
                                    followersList.add(tempUser);
                                }
                                profile.followersCount = followersList.size();
                                profile.followers = followersList;
                            }
                            if (tempKey.equals("following")) {
                                // loop through each user and add them to the followingList
                                for (DataSnapshot infantSnapShot : childSnapshot.getChildren()) {
                                    User tempUser = infantSnapShot.getValue(User.class);
                                    followingList.add(tempUser);
                                }
                                profile.followingCount = followingList.size();
                                profile.following = followingList;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void setNavigationView() {
        // set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        //set up searchbar
//        miSearch = (MenuItem) findViewById(R.id.miSearch);
//        searchView = (SearchView) miSearch.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        }); ----- delete or change how searchView lines are bc it causes crash -- TODO

        // set the drawer layout and button to access it
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // set the navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigationView.setItemTextColor(null);



        // access the header view to set the text according to the user details
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.tvName);
        TextView username = (TextView) header.findViewById(R.id.tvUsername);
        final ImageView Image = (ImageView) header.findViewById(R.id.ivProfileImage);

        // set text
        name.setText(profile.name);
        username.setText(profile.username);

//        // set profile image
//        Glide.with(getApplicationContext())
//                .load(profile.profileImageUrl)
//                .asBitmap()
//                .centerCrop()
//                .into(new BitmapImageViewTarget(Image) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
//                        circularBitmapDrawable.setCircular(true);
//                        Image.setImageDrawable(circularBitmapDrawable);
//                    }
//                });


        Glide.with(this)
                .load(profile.profileImageUrl)
                .transform(new CircleGlide(this))
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
                // set the animation
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                // close the navigation view
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    public void setShake() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                //bounds for random number. add a min to create bounds
                int max = HomeTimelineFragment.recipes.size();
                Random r = new Random();

                //generates random position. change this line to add bounds to random number
                int randomRecipePosition = r.nextInt(max);

                Recipe randomRecipe = HomeTimelineFragment.recipes.get(randomRecipePosition);
                //Toast.makeText(getApplicationContext(), randomRecipe.title, Toast.LENGTH_LONG).show();
                //on shake, get detail page of random recipe
                Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                i.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(randomRecipe));
                getApplicationContext().startActivity(i);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);  //changed from .SENSOR_DELAY_UI to NORMAL for a forced pause between shakes registered
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.miSearch).getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle the presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miSearch:
                onSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

// added drawer code from default to case if new recipe still doesnt add to list remove this new code-- TODO
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
                item.setChecked(false);
                return true;
            case R.id.nav_activity_add_recipe:
                onCreateRecipeView(item);
                // close the navigation view
                drawerLayout.closeDrawer(GravityCompat.START);
                item.setChecked(false);

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
                Snackbar.make(vpPager, R.string.snackbar_text, Snackbar.LENGTH_SHORT)
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
        // clear the backstack

    }


    private void onSearch() {
        return;
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //get recipe from intent and add to hometimeline list
        if (resultCode != RESULT_CANCELED) {
            Recipe recipe = Recipe.fromBundle(data.getExtras());
            recipe.user = profile;
            HomeTimelineFragment fragmentHome = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
            fragmentHome.appendRecipe(recipe);
            FirebaseDatabase database =  FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(recipe.title);
            myRef.setValue(recipe);

        }
    }

    public void setNavHeader() {
        User u1 = new User();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // set the new animation
        overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
    }
}


