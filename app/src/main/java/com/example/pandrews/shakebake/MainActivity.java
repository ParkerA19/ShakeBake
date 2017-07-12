package com.example.pandrews.shakebake;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pandrews.shakebake.fragments.RecipesListFragment;
import com.example.pandrews.shakebake.fragments.RecipesPagerAdapter;
import com.example.pandrews.shakebake.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_CODE = 25;

    // Instance variables
    RecipesPagerAdapter adapterViewPager;

    @BindView(R.id.viewpager)
    ViewPager vpPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind with butterknife
        ButterKnife.bind(this);

        // set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // set the drawer layout and button to access it
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // set the navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get the view pager -- bound with butterknife
        // ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        adapterViewPager = new RecipesPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(adapterViewPager);

        // setup the TabLayout to use the viewPager -- bound with butterknife
        //  TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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


    private void onSearch() {
        return;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Recipe recipe = Recipe.fromBundle(data.getExtras());

        RecipesPagerAdapter pagerAdapter = (RecipesPagerAdapter) vpPager.getAdapter();
        RecipesListFragment.recipes.add(0, recipe);
    }
}


//send an intent from this activity (use startactivityforresult) then send the recipe back and use onactivityresult here