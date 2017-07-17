package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.models.Recipe;
import com.example.pandrews.shakebake.models.User;

import org.parceler.Parcels;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by pandrews on 7/10/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    // Instance variables
    ArrayList<Recipe> mRecipes = new ArrayList<>();
    Context context;
    private RecipeAdapterListener mlistener;


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
        holder.tvDescription.setText(recipe.description);
        holder.tvForks.setText(recipe.forkCount + " Forks");


        if (recipe.user.profileImageUrl != null) {
            Glide.with(context)
                    .load(recipe.user.profileImageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 200, 0))
                    .into(holder.ivProfileImage);
        }
        if (recipe.mediaurl != null) {
            Glide.with(context)
                    .load(recipe.mediaurl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                    .into(holder.ivMedia);


        } else if (recipe.targetUri != null){
            Uri targetUri = Uri.parse(recipe.targetUri);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(targetUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            holder.ivMedia.setImageBitmap(bitmap);
        } else {
            holder.ivMedia.setVisibility(View.GONE);

        }

        // set onClickListener for the profile image to open the profile activity
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make a new intent
                Intent intent = new Intent(context, ProfileActivity.class);
                // put the user into the intent
                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(recipe.user));
                // start activity with intent
                context.startActivity(intent);
            }
        });
    }


//    Bitmap bitmap;
//            try {
//        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));


    @Override
    public int getItemCount() {
        return mRecipes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @Nullable@BindView(R.id.ivMedia) ImageView ivMedia;
        @BindView(R.id.tvForks) TextView tvForks;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDescription) TextView tvDescription;



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
                // show the activity
                context.startActivity(intent);
            }

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mRecipes.clear();
        notifyDataSetChanged();
    }

}