package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.models.Recipe;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_CODE = 25;

    // Instance variables
    Recipe recipe;
    Context context;



    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @Nullable@BindView(R.id.ivMedia) ImageView ivMedia;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvForks) TextView tvForks;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.tvIngredients) TextView tvIngredients;
    @BindView(R.id.tvSteps) TextView tvSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // bind with butterknife
        ButterKnife.bind(this);

        // set the context
        context = getApplicationContext();

        // get the recipe from the intent
        recipe = (Recipe) Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s", recipe.title));

        // set all the views
        tvTitle.setText(recipe.title);
        tvDescription.setText(recipe.description);
        tvIngredients.setText(recipe.ingredients);
        tvForks.setText(recipe.forkCount + " Forks");
        tvUsername.setText(recipe.user.username);
        tvSteps.setText(recipe.steps);

        Glide.with(context)
                .load(recipe.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                .into(ivProfileImage);

        Glide.with(context)
                .load(recipe.mediaurl)
                .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                .into(ivMedia);



        // set the toolbar at the top
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // draw the navigation item
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // set up the navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline ,menu);
        return true;
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

}
