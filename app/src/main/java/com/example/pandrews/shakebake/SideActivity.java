package com.example.pandrews.shakebake;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SideActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side, menu);
//        getMenuInflater().inflate(R.menu.side, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
//
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        return true;
    }
//----uncomment TODO
//        if (id == R.id.nav_activity_add_recipe) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, new AddRecipeActivity()).commit();
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        switch(id) {
//            case R.id.nav_add_a_recipe:
//                Intent h= new Intent(this, AddRecipeActivity.class);
//                startActivity(h);
//                break;
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        if (id == R.id.nav_add_a_recipe) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, new MyForksActivity()).commit();
//        } else if (id == R.id.nav_my_forks) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, new MyForksActivity()).commit();
//        } else if (id == R.id.nav_settings) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, new SettingsActivity()).commit();
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

//        if (id == R.id.nav_add_a_recipe) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, new MyForksActivity()).commit();
//        } else if (id == R.id.nav_my_forks) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, new MyForksActivity()).commit();
//        } else if (id == R.id.nav_settings) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, new SettingsActivity()).commit();
//
//        } else if (id == R.id.nav_my_forks) {
//
//        } else if (id == R.id.nav_settings) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//


}
