package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.fragments.IngredientsFragment;
import com.example.pandrews.shakebake.fragments.StepsFragment;
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
    ScrollView view;



    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @Nullable@BindView(R.id.vvMedia) VideoView vvMedia;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvForks) TextView tvForks;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.flIngredients) FrameLayout flIngredients;
    @BindView(R.id.flSteps) FrameLayout flSteps;
    @BindView(R.id.scrollView1) ScrollView scrollView1;
    @BindView(R.id.tvTag1) TextView tvTag1;
    @BindView(R.id.tvTag2) TextView tvTag2;
    @BindView(R.id.tvTag3) TextView tvTag3;


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
        recipe = Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s", recipe.title));

        // set the text and images for the recipe
        populateDetailsHeadline();

        // set the IngredientsFragment
        populateIngredients();

        // set the StepsFragment
        populateSteps();

        // set the navigation view
        setNavigationView();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline ,menu);
        return true;
    }

    /**
     * Method to set each text and image view
     * is called in onCreate
     */
    public void populateDetailsHeadline() {
        // set all the views
        tvTitle.setText(recipe.title);
        tvDescription.setText(recipe.description);
        AddRecipeAdapter iAdapter = new AddRecipeAdapter(recipe.ingredients, this);
   //     rvIngredients.setAdapter(iAdapter);
        tvForks.setText(recipe.forkCount + " Forks");
        tvUsername.setText(recipe.user.username);
        AddRecipeAdapter sAdapter = new AddRecipeAdapter(recipe.steps, this);
   //     rvSteps.setAdapter(sAdapter);

        // set the appropriate tags and make then not visible when null
        if (recipe.keywords != null) {
            if (recipe.keywords.size() > 0) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag1.setText("#" + recipe.keywords.get(0));

                // set an onClickListener for this tag
                tvTag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, recipe.keywords.get(0), Toast.LENGTH_SHORT).show();
                    }
                });

            } else { tvTag1.setVisibility(View.GONE); }

            if (recipe.keywords.size() > 1) {
                tvTag2.setVisibility(View.VISIBLE);
                tvTag2.setText("#" + recipe.keywords.get(1));

                // set an onClickListener for this tag
                tvTag2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, recipe.keywords.get(1), Toast.LENGTH_SHORT).show();
                    }
                });

            } else { tvTag2.setVisibility(View.GONE); }

            if (recipe.keywords.size() > 2) {
                tvTag3.setVisibility(View.VISIBLE);
                tvTag3.setText("#" + recipe.keywords.get(2));

                // set an onClickListener for this tag
                tvTag3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, recipe.keywords.get(2), Toast.LENGTH_SHORT).show();
                    }
                });


            } else { tvTag3.setVisibility(View.GONE); }


        } else {
            tvTag1.setVisibility(View.GONE);
            tvTag2.setVisibility(View.GONE);
            tvTag3.setVisibility(View.GONE);
        }

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

//        if (recipe.mediaurl != null) {
//            Glide.with(context)
//                    .load(recipe.mediaurl)
//                    .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
//                    .into(vvMedia);
//        }

        Uri uri=Uri.parse(recipe.mediaurl);

        vvMedia.setVideoURI(uri);
        vvMedia.requestFocus();
        vvMedia.start();
    }

    /**
     * Method to put the IngredientsFragment into the initial FrameLayout Containter
     * Called in onCreate
     */
    public void populateIngredients(){
        // Create the IngredientsFragment and set the recipe and ingredients
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setRecipe(recipe);
        ingredientsFragment.setIngredients(recipe.ingredients);
        ingredientsFragment.setClickable(true);

        // Display the IngredientsFragment inside the container (dynamically)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make changes
        ft.replace(R.id.flIngredients, ingredientsFragment);

        // commit the transaciton
        ft.commit();
    }

    /*
    Method to put the StepsFragment into the second FrameLayout Container
    Called in onCreate
     */
    public void populateSteps() {
        // Create the StepsFragment and set the recipe and steps
        StepsFragment stepsFragment = new StepsFragment();
        stepsFragment.setRecipe(recipe);
        stepsFragment.setSteps(recipe.steps);
        stepsFragment.setClickable(true);

        // Display the StepsFragment inside the stepsContainer
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();

        // make changes
        ft2.replace(R.id.flSteps, stepsFragment);

        // commit the transaction
        ft2.commit();

        flSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Hey", Toast.LENGTH_SHORT).show();
            }
        });
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
                Snackbar.make(scrollView1, R.string.snackbar_text, Snackbar.LENGTH_SHORT)
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
}
