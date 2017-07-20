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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pandrews.shakebake.models.Recipe;

import org.parceler.Parcels;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by andreagarcia on 7/13/17.
 */

public class MyForksAdapter  extends RecyclerView.Adapter<MyForksAdapter.ViewHolder>{
    // Instance variables
    ArrayList<Recipe> mForkRecipes = new ArrayList<>();
    Context context;
    private ForkAdapterListener mlistener;


    // define an interface required by the viewholder
    public interface ForkAdapterListener {
        public void onItemSelected(View view, int position);
    }

    // pass in the Recipes array in the constructor
    public MyForksAdapter(ArrayList<Recipe> recipes, ForkAdapterListener listener) {
        mForkRecipes = recipes;
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
        final Recipe recipe = mForkRecipes.get(position);

        // populate the view according to the position
        holder.tvTitle.setText(recipe.title);
        holder.tvUsername.setText(recipe.user.username);

        // set the appropriate tags and make then not visible when null
        if (recipe.keywords != null) {
            if (recipe.keywords.size() > 0) {
                holder.tvTag1.setVisibility(View.VISIBLE);
                holder.tvTag1.setText("#" + recipe.keywords.get(0));

                // set an onClickListener for this tag
                holder.tvTag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, recipe.keywords.get(0), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, recipe.keywords.get(1), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, recipe.keywords.get(2), Toast.LENGTH_SHORT).show();
                    }
                });


            } else { holder.tvTag3.setVisibility(View.GONE); }


        } else {
            holder.tvTag1.setVisibility(View.GONE);
            holder.tvTag2.setVisibility(View.GONE);
            holder.tvTag3.setVisibility(View.GONE);
        }

//        holder.tvDescription.setText(recipe.description);

        holder.tvForks.setText(recipe.forkCount + "");

        if (recipe.user.profileImageUrl != null) {
            Glide.with(context)
                    .load(recipe.user.profileImageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                    .into(holder.ivProfileImage);
        }

        if (recipe.mediaurl != null) {
            holder.ivMedia.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(recipe.mediaurl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                    .into(holder.ivMedia);
        } else if (recipe.targetUri != null){
            holder.ivMedia.setVisibility(View.VISIBLE);
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
    }


    @Override
    public int getItemCount() {
        if (mForkRecipes == null) {
            return 0;
        } else {
            return mForkRecipes.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @Nullable @BindView(R.id.ivMedia) ImageView ivMedia;
        @BindView(R.id.tvForks) TextView tvForks;
        @BindView(R.id.tvTitle) TextView tvTitle;
//        @BindView(R.id.tvDescription) TextView tvDescription;
        @Nullable@BindView(R.id.tvTag1) TextView tvTag1;
        @Nullable@BindView(R.id.tvTag2) TextView tvTag2;
        @Nullable@BindView(R.id.tvTag3) TextView tvTag3;


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
                Recipe recipe = mForkRecipes.get(position);
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
        mForkRecipes.clear();
        notifyDataSetChanged();
    }
}
