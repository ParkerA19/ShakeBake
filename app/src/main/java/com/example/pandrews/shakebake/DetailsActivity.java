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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_CODE = 25;

    // Instance variables
    Recipe recipe;
    Context context;
    User profile;



    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @Nullable@BindView(R.id.ivMedia) ImageView ivMedia;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvForks) TextView tvForks;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.rvIngredients) RecyclerView rvIngredients;
    @BindView(R.id.rvSteps) RecyclerView rvSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // bind with butterknife
        ButterKnife.bind(this);

        // set the context
        context = getApplicationContext();

        // set the account user
        profile = MainActivity.profile;

        // get the recipe from the intent
        recipe = (Recipe) Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s", recipe.title));

        // set the text and images for the recipe
        populateDetailsHeadline();

        // set the navigation view
        setNavigationView();

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

    /*
    Method to set each text and image view
    is called in onCreate
     */
    public void populateDetailsHeadline() {
        // set all the views
        tvTitle.setText(recipe.title);
        tvDescription.setText(recipe.description);
        AddRecipeAdapter iAdapter = new AddRecipeAdapter(recipe.ingredients, this);
        rvIngredients.setAdapter(iAdapter);
        tvForks.setText(recipe.forkCount + " Forks");
        tvUsername.setText(recipe.user.username);
        AddRecipeAdapter sAdapter = new AddRecipeAdapter(recipe.steps, this);
        rvSteps.setAdapter(sAdapter);

        if (recipe.user.profileImageUrl != null) {
            Glide.with(context)
                    .load(recipe.user.profileImageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                    .into(ivProfileImage);
        }

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the user
                User user = recipe.user;
                // set intent
                Intent intent = new Intent(context, ProfileActivity.class);
                // populate intent
                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(user));
                // start activity
                startActivity(intent);
            }
        });

        if (recipe.mediaurl != null) {
            Glide.with(context)
                    .load(recipe.mediaurl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                    .into(ivMedia);
        }
    }

    /*
    method to set the toolbar and activate the navigation view
    sets the text and images within the navigation view
    puts a toolbar icon at top left that activates the navigation view
        - can also be done by swiping screen to the right
    called in onCreate
     */
    public void setNavigationView() {
        // set the toolbar at the top
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // draw the navigation item
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
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

}
