package com.example.pandrews.shakebake;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.pandrews.shakebake.fragments.HomeTimelineFragment;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.pandrews.shakebake.MainActivity.profile;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_CODE = 25;

    public ArrayList<Recipe> resultRecipes;
    public ResultAdapter resultAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 20;
    public String query;

    //for shake listener
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;


    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.rvResult) RecyclerView rvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Bind with ButterKnife
        ButterKnife.bind(this);

        // set the navigation view
        setNavigationView();

        setShake();

        resultRecipes = new ArrayList<>();
        query = getIntent().getStringExtra("query");

        //create database reference
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recipes");

        //create listener. this one adds all recipes currently in database w/fork count above 300
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                    if (query.toLowerCase().equalsIgnoreCase(newRecipe.title.toLowerCase()) | newRecipe.keywords.contains(query)) {    //later add | newRecipe.keywords.contains(query) into logic to search keywords
                        appendRecipe(newRecipe);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });



        resultAdapter = new ResultAdapter(resultRecipes, null);
        rvResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvResult.setAdapter(resultAdapter);

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
                // this is where adapter will be filtered, still have to add
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //route back to same activity to handle searches -- TODO
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

//    @Override
    public void onItemSelected(View view, int position) {

    }

    public void setNavigationView() {
        // set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // set the drawer layout and button to access it
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // set the navigation view
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
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
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
        // clear the backstack

    }

    public void appendRecipe(Recipe recipe){
        // add a tweet
        resultRecipes.add(0, recipe);
        // inserted at position 0
        resultAdapter.notifyItemInserted(0);
        rvResult.scrollToPosition(0);
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

}
