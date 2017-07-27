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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.fragments.HomeTimelineFragment;
import com.example.pandrews.shakebake.fragments.InstructionsPagerAdapter;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

import org.parceler.Parcels;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class InstructionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_CODE = 25;

    // Instance Variables
    User profile;
    Context context;
    public Recipe recipe;
    int fragPosition;

    InstructionsPagerAdapter adapterViewPager;

    //for shake listener
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    // set up for ButterKnife
    @BindView(R.id.instruction_viewpager) ViewPager vpPager;
    @BindView(R.id.instruction_tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        // Bind with ButterKnife
        ButterKnife.bind(this);

        // set context
        context = getApplicationContext();

        // set the overall user for navigation view
        profile = MainActivity.profile;

        // get the recipe from the intent
        recipe = (Recipe) Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        Log.d("InstructionsActivity", String.format("Showing details for %s", recipe.title));

        // get the boolean from the intent to see which fragment to go to
        // get the position from the intent to see which fragment to go to
        fragPosition = getIntent().getIntExtra("int", 0);

        // set up the Navigation View
        setNavigationView();

        // set the adapter for the pager
        adapterViewPager = new InstructionsPagerAdapter(getSupportFragmentManager(), this, recipe, false);

        vpPager.setAdapter(adapterViewPager);

        // setup the TabLayout to use the viewPager
        tabLayout.setupWithViewPager(vpPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));


        // set the current tab based on which tab was pressed (fragPosition)
        // set the current tab based on which container was pressed (fragPosition)
        vpPager.setCurrentItem(fragPosition);

        // set up the shake feature
        setShake();
    }

    public void setNavigationView() {
        // set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // set the drawer layout and button to access it
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.instructions_drawer_layout);
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
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.instructions_drawer_layout);
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
        // start activity with new intent for the login activity
        startActivity(new Intent(getApplicationContext(), OpeningActivity.class));
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

}
