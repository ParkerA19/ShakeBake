package com.example.pandrews.shakebake;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by pandrews on 7/10/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    // Instance variables
    List<Recipe> mRecipes;
    Context context;
    private RecipeAdapterListener mlistener;

    // define an interface required by the viewholder
    public interface RecipeAdapterListener {
        public void onItemSelected(View view, int position);
    }

    // pass in the Recipes array in the constructor
    public RecipeAdapter(List<Recipe> recipes, RecipeAdapterListener listener) {
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
        Recipe recipe = mRecipes.get(position);

        // populate the view according to the position
        holder.tvTitle.setText(recipe.title);
        holder.tvUsername.setText(recipe.user.username);
        holder.tvDescription.setText(recipe.description);
        holder.tvForks.setText(recipe.forkCount + " Forks");

        Glide.with(context)
                .load(recipe.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                .into(holder.ivProfileImage);

        Glide.with(context)
                .load(recipe.mediaurl)
                .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                .into(holder.ivMedia);
    }

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
                //     Intent intent = new Intent(context, DetailsActivity.class);       TODO: create a details activity
                // serialize the movie using parceler, use its short name as a key
                //      intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                //             context.startActivity(intent);
            }

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mRecipes.clear();
        notifyDataSetChanged();
    }

}