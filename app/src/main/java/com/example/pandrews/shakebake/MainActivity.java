package com.example.pandrews.shakebake;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.fragments.HomeTimelineFragment;
import com.example.pandrews.shakebake.fragments.RecipesPagerAdapter;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

import org.parceler.Parcels;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_CODE = 25;

    // Instance variables
    RecipesPagerAdapter adapterViewPager;
    //MenuItem miSearch;
    //SearchView searchView;  ---- maybe delete these lines -- TODO

    public static User profile;

    //for shake listener
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @BindView(R.id.viewpager) ViewPager vpPager;
    @BindView(R.id.sliding_tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind with butterknife
        ButterKnife.bind(this);

        // for now just set a mock user to be the profile in the navigation view
        profile = new User();

        // set the navigation view
        setNavigationView();



        // set the adapter for the pager
        adapterViewPager = new RecipesPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(adapterViewPager);

        // setup the TabLayout to use the viewPager -- bound with butterknife
        //  TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        // set up the shake feature
        setShake();
    }


    public void setNavigationView() {
        // set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Toast.makeText(getApplicationContext(), randomRecipe.title, Toast.LENGTH_LONG).show();
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
                SensorManager.SENSOR_DELAY_NORMAL);  //changed from .SENSOR_DELAY_UI to normal for a forced pause between shakes registered
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
                return true;
            case R.id.nav_activity_add_recipe:
                onCreateRecipeView(item);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
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
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }

    /*
    on click listener for the snackbar
    when you click it will confirm the logout and bring user to the login activity
     */
    View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
                public void onClick(View v) {
            logout();
        }
    };

    /*
    starts the AddRecipeActivity for result
    called in onNavigationSelected
     */
    public void onCreateRecipeView(MenuItem item) {
        Intent i = new Intent(this, AddRecipeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    /*
    takes user back to the logout screen
    called in myOnClickListener
     */
    public void logout() {
        // start activity with new intent for the login activity
        startActivity(new Intent(getApplicationContext(), OpeningActivity.class));
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
        }
    }

    public void setNavHeader() {
        User u1 = new User();
    }

}


