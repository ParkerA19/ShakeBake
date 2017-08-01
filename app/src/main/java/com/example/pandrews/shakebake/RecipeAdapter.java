package com.example.pandrews.shakebake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.pandrews.shakebake.R.drawable.vector_forked;
import static com.example.pandrews.shakebake.R.drawable.vector_real_fork;

/**
 * Created by pandrews on 7/10/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    // Instance variables
    ArrayList<Recipe> mRecipes = new ArrayList<>();
    Context context;
    private RecipeAdapterListener mlistener;
    Uri uri;
    ArrayList<String> videos;
    Integer currentVideo;


    // define an interface required by the viewholder
    public interface RecipeAdapterListener {
        public void onItemSelected(View view, int position);
    }

    // pass in the Recipes array in the constructor
    public RecipeAdapter(ArrayList<Recipe> recipes, RecipeAdapterListener listener) {
        mRecipes = recipes;
        mlistener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeView = inflater.inflate(R.layout.item_recipe, parent, false);
        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }


    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        // get the data according to the position
        final Recipe recipe = mRecipes.get(position);

        // populate the view according to the position
        holder.tvTitle.setText(recipe.title);
        holder.tvUsername.setText(recipe.user.username);

        // based on the forked boolean choose the vector resource for ibFork
        int forkResource = (recipe.forked) ? vector_forked: vector_real_fork;
        holder.ibFork.setImageResource(forkResource);

        // set the forkCount text
        String forkString = (recipe.forkCount == 0) ? "" : recipe.forkCount.toString();
        holder.tvForks.setText(forkString);


        // now set an OnClickListener for the Fork
        holder.ibFork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipe.forked) {
                    // change the boolean
                    recipe.forked = false;
                    // set new image resource
                    holder.ibFork.setImageResource(vector_real_fork);
                    // set the new forkCount
                    recipe.forkCount = recipe.forkCount - 1;
                    // set the new forkCount text
                    String tempString = (recipe.forkCount == 0) ? "" : recipe.forkCount.toString();
                    holder.tvForks.setText(tempString);
                    //change forked value on database
                    FirebaseDatabase.getInstance().getReference(recipe.title + "/forked").setValue(recipe.forked);
                    FirebaseDatabase.getInstance().getReference(recipe.title + "/forkCount").setValue(recipe.forkCount);
                }

                else {
                    // change the boolean
                    recipe.forked = true;
                    // set the new image resource
                    holder.ibFork.setImageResource(vector_forked);
                    // set teh new forkCount
                    recipe.forkCount = recipe.forkCount + 1;
                    // set the new forkCount text
                    String tempString = (recipe.forkCount == 0) ? "": recipe.forkCount.toString();
                    holder.tvForks.setText(tempString);
                    //change forked value on database
                    FirebaseDatabase.getInstance().getReference(recipe.title + "/forked").setValue(recipe.forked);
                    FirebaseDatabase.getInstance().getReference(recipe.title + "/forkCount").setValue(recipe.forkCount);


                }
            }
        });

        // set the appropriate tags and make then not visible when null
        if (recipe.keywords != null) {
            if (recipe.keywords.size() > 0) {
                holder.tvTag1.setVisibility(View.VISIBLE);
                holder.tvTag1.setText("#" + recipe.keywords.get(0));

                // set an onClickListener for this tag
                holder.tvTag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, recipe.keywords.get(0), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, SearchActivity.class);
                        i.putExtra("query", recipe.keywords.get(0));
                        context.startActivity(i);
                    }
                });

            } else { holder.tvTag1.setVisibility(View.GONE); }

            if (recipe.keywords.size() > 1) {
                holder.tvTag2.setVisibility(View.VISIBLE);
                holder.tvTag2.setText("#" + recipe.keywords.get(1));

                // set an onClickListener for this tag
                holder.tvTag2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, recipe.keywords.get(1), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, SearchActivity.class);
                        i.putExtra("query", recipe.keywords.get(1));
                        context.startActivity(i);
                    }
                });

            } else { holder.tvTag2.setVisibility(View.GONE); }

            if (recipe.keywords.size() > 2) {
                holder.tvTag3.setVisibility(View.VISIBLE);
                holder.tvTag3.setText("#" + recipe.keywords.get(2));

                // set an onClickListener for this tag
                holder.tvTag3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, recipe.keywords.get(2), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, SearchActivity.class);
                        i.putExtra("query", recipe.keywords.get(2));
                        context.startActivity(i);
                    }
                });


            } else { holder.tvTag3.setVisibility(View.GONE); }


        } else {
            holder.tvTag1.setVisibility(View.GONE);
            holder.tvTag2.setVisibility(View.GONE);
            holder.tvTag3.setVisibility(View.GONE);
        }


        // Use Glide to load Profile Image
        if (recipe.user.profileImageUrl != null) {
            holder.ivProfileImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(recipe.user.profileImageUrl)
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.ivProfileImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.ivProfileImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        if (recipe.mediaurl != null) {
            holder.vvMedia.setVisibility(View.VISIBLE);
        }

        else {
            holder.vvMedia.setVisibility(View.GONE);
        }

        // set onClickListener for the profile image to open the profile activity
        holder.llImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make a new intent
                Intent intent = new Intent(context, ProfileActivity.class);
                // put the user into the intent
                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(recipe.user));
                // start activity with intent
                context.startActivity(intent);
                // set the activity
                Activity activity = (Activity) context;
                // set the new animation
                activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

            }
        });


        // set the video view and video url
        String path="android.resource://com.example.pandrews.shakebake/" + R.raw.cat;
        String path1="http://www.youtube.com/v/VA770wpLX-Q?version=3&f=videos&app=youtube_gdata";

        //init videos list
        videos = new ArrayList<>();
        currentVideo = 0;
        if (recipe.mediaurl != null) {
//            if (recipe.stepVideo != null){
//                for (String value : recipe.stepVideo.values()) {
//                    videos.add(value);
//                }
//                uri = Uri.parse(videos.get(currentVideo));
//
//            } else {
//                uri = Uri.parse(recipe.mediaurl);
//            }
            uri = Uri.parse("android.resource://com.example.pandrews.shakebake/raw/" + recipe.mediaurl);
            holder.vvMedia.setVisibility(View.VISIBLE);

            holder.vvMedia.setVideoURI(uri);

            holder.vvMedia.requestFocus();
            holder.vvMedia.start();
        } else {
            Log.d("null video" , "Null video");
        }
//        holder.vvMedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                currentVideo += 1;
//                if (recipe.stepVideo != null && currentVideo < videos.size()) {
//                    uri = Uri.parse(videos.get(currentVideo));
//                    holder.vvMedia.setVideoURI(uri);
//                    holder.vvMedia.start();
//
//                } else if (recipe.stepVideo != null && currentVideo >= videos.size()) {
//                    currentVideo = 0;
//                    uri = Uri.parse(videos.get(currentVideo));
//                    holder.vvMedia.setVideoURI(uri);
//                    holder.vvMedia.start();
//                }
//            }
//        });
    }



    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        } else {
            return mRecipes.size();
        }
    }

    /**
    ViewHolder class
    Where the binding of all the Views is done (ButterKnife)
    Also sets onClick for each recipe to take user to DetailActivity
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @Nullable@BindView(R.id.vvMedia) VideoView vvMedia;
        @BindView(R.id.tvForks) TextView tvForks;
        @BindView(R.id.tvTitle) TextView tvTitle;
//        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.llImage) LinearLayout llImage;
        @Nullable@BindView(R.id.tvTag1) TextView tvTag1;
        @Nullable@BindView(R.id.tvTag2) TextView tvTag2;
        @Nullable@BindView(R.id.tvTag3) TextView tvTag3;
        @BindView(R.id.ibFork) ImageButton ibFork;
        @BindView(R.id.cardView) CardView cardView;



        public ViewHolder (View itemView) {
            super(itemView);

            // bind with butterknife
            ButterKnife.bind(this, itemView);

            // set onClickListener
            itemView.setOnClickListener(this);

            // perform findViewById lookups

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mlistener != null) {
//                        // get the position of row element
//                        int position = getAdapterPosition();
//                        // fire the listener callback
//                        mlistener.onItemSelected(view, position);
//                    }
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            // get item position
            int position = getAdapterPosition();
            //make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet and the position
                Recipe recipe = mRecipes.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, DetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(recipe));
                // show the new activity
                context.startActivity(intent);
                // get the activity
                Activity activity = (Activity) context;
                // set the new animation
                activity.overridePendingTransition(R.anim.shrink_and_rotate_enter, R.anim.shrink_and_rotate_exit);
            }

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mRecipes.clear();
        notifyDataSetChanged();
    }

}