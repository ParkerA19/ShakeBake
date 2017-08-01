package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.pandrews.shakebake.fragments.IngredientsFragment;
import com.example.pandrews.shakebake.fragments.StepsFragment;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.pandrews.shakebake.R.drawable.vector_forked;
import static com.example.pandrews.shakebake.R.drawable.vector_real_fork;

public class DetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_CODE = 25;

    // Instance variables
    Recipe recipe;
    Context context;
    User profile;
    ScrollView view;
    Uri uri;



    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvForks) TextView tvForks;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.flIngredients) FrameLayout flIngredients;
    @BindView(R.id.flSteps) FrameLayout flSteps;
    @BindView(R.id.scroll) NestedScrollView scrollView1;
    @BindView(R.id.tvTag1) TextView tvTag1;
    @BindView(R.id.tvTag2) TextView tvTag2;
    @BindView(R.id.tvTag3) TextView tvTag3;
    @BindView(R.id.tvIngredient) TextView tvIngredient;
    @BindView(R.id.tvPreparation) TextView tvPreparation;
    @BindView(R.id.ibFork) ImageButton ibFork;
    @BindView(R.id.drawer_layout2) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;


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

        // set the the collapsing image layout
        populateCollapsingToolbarLayout();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline ,menu);
        return true;
    }

    public void populateCollapsingToolbarLayout(){
        collapsingToolbarLayout.setTitleEnabled(false);

        Glide.with(this)
                .load(Uri.parse("https://images.pexels.com/photos/140831/pexels-photo-140831.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb"))
                .into(image);
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

        // add the ibFork ImageButton
        populateForkButton();

        tvUsername.setText(recipe.user.username);
        AddRecipeAdapter sAdapter = new AddRecipeAdapter(recipe.steps, this);
   //     rvSteps.setAdapter(sAdapter);

        populateKeywords();

        // set profile image
        if (recipe.user.profileImageUrl != null) {

            Glide.with(getApplicationContext())
                    .load(profile.profileImageUrl)
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
                // set the animation
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                // close the navigation view
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * Method that sets up the ibFork ImageButton
     * Sets appropriate image based on the boolean recipe.forked
     * Sets TextView tvForks based on the integer recipe.forkCount
     * called in populateDetailsHeadline
     */
    public void populateForkButton() {
        // based on the forked boolean choose the vector resource for ibFork
        int forkResource = (recipe.forked) ? R.drawable.vector_forked : R.drawable.vector_real_fork;
        ibFork.setImageResource(forkResource);

        // set the forkCount text
        String forkString = (recipe.forkCount == 0) ? "" : recipe.forkCount.toString();
        tvForks.setText(forkString);

        // now set an OnClickListener for the Fork
        ibFork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipe.forked) {
                    // change the boolean
                    recipe.forked = false;
                    // set new image resource
                    ibFork.setImageResource(vector_real_fork);
                    // set the new forkCount
                    recipe.forkCount = recipe.forkCount - 1;
                    // set the new forkCount text
                    String tempString = (recipe.forkCount == 0) ? "" : recipe.forkCount.toString();
                    tvForks.setText(tempString);
                    //change forked value in database
                    FirebaseDatabase.getInstance().getReference(recipe.title + "/forked").setValue(recipe.forked);
                    FirebaseDatabase.getInstance().getReference(recipe.title + "/forkCount").setValue(recipe.forkCount);
                } else {
                    // change the boolean
                    recipe.forked = true;
                    // set the new image resource
                    ibFork.setImageResource(vector_forked);
                    // set teh new forkCount
                    recipe.forkCount = recipe.forkCount + 1;
                    // set the new forkCount text
                    String tempString = (recipe.forkCount == 0) ? "" : recipe.forkCount.toString();
                    tvForks.setText(tempString);
                    //change forked value on database
                    FirebaseDatabase.getInstance().getReference(recipe.title + "/forked").setValue(recipe.forked);
                    FirebaseDatabase.getInstance().getReference(recipe.title + "/forkCount").setValue(recipe.forkCount);


                }
            }
        });
    }

    /**
     * Method to make the keywords shown and set the onClick methods
     * called in populateDetailsHeadline
     */
    public void populateKeywords() {
        // set the appropriate tags and make then not visible when null
        if (recipe.keywords != null) {
            if (recipe.keywords.size() > 0) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag1.setText("#" + recipe.keywords.get(0));

                // set an onClickListener for this tag
                tvTag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, recipe.keywords.get(0), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, SearchActivity.class);
                        i.putExtra("query", recipe.keywords.get(0));
                        context.startActivity(i);
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
                        //Toast.makeText(context, recipe.keywords.get(1), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, SearchActivity.class);
                        i.putExtra("query", recipe.keywords.get(1));
                        context.startActivity(i);
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
                        //Toast.makeText(context, recipe.keywords.get(2), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, SearchActivity.class);
                        i.putExtra("query", recipe.keywords.get(2));
                        context.startActivity(i);
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

    /**
     * Method to put the StepsFragment into the second FrameLayout Container
     * Called in onCreate
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

    /**
     * method to set the toolbar and activate the navigation view
     * sets the text and images within the navigation view
     * puts a toolbar icon at top left that activates the navigation view
     *  - can also be done by swiping screen to the right
     *  called in onCreate
     */
    public void setNavigationView() {
        // set the toolbar at the top
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        toolbarTitle.setText("DETAILS");
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
            }
        });

    }

    public void onIngredient(View v) {
        // make intent
        Intent intent = new Intent(context, InstructionsActivity.class);
        // pass in recipe
        intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(recipe));
        // pass in the fragment position to go to
        intent.putExtra("int", 0);
        // start activity
        context.startActivity(intent);
    }

    public void onPrep(View v) {
        // make intent
        Intent intent = new Intent(context, StepActivity.class);
        // pass in recipe
        intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(recipe));
        // setFlags so this activity does not go into the BackStack
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        // start the activity
        context.startActivity(intent);
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
                // make a new intent
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                // start activity
                startActivity(intent);
                return true;
            case R.id.nav_logout:
                // Pass in the click listener when displaying the Snackbar
                Snackbar.make(scrollView1, R.string.snackbar_text, Snackbar.LENGTH_SHORT)
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
     * On click listener for the snackbar
     * When you click it will confirm the logout and bring user to the login activity
     */
    View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logout();
        }
    };

    /**
     * starts the AddRecipeActivity for result
     * called in onNavigationSelected
     */
    public void onCreateRecipeView(MenuItem item) {
        Intent i = new Intent(this, AddRecipeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    /**
     * takes user back to the logout screen
     * called in myOnClickListener
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.shrink_and_rotate_enter, R.anim.shrink_and_rotate_exit);

    }
}
