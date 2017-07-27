package com.example.pandrews.shakebake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.pandrews.shakebake.models.User;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pandrews on 7/13/17.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    // Instance variables
    List<User> mUsers;
    Context context;
    private FriendAdapterListener mlistener;

    // define an interface required by the viewholder
    public interface FriendAdapterListener {
        public void onItemSelected(View view, int position);
    }

    // pass in the Recipes array in the constructor
    public FriendAdapter(List<User> users, FriendAdapterListener listener) {
        mUsers = users;
        mlistener = listener;
    }


    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View userView = inflater.inflate(R.layout.item_friend, parent, false);
        FriendAdapter.ViewHolder viewHolder = new FriendAdapter.ViewHolder(userView);
        return viewHolder;
    }


    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(final FriendAdapter.ViewHolder holder, int position) {

        // get the data according to the position
        final User user = mUsers.get(position);

        // populate the view according to the position
        holder.tvName.setText(user.name);
        holder.tvUsername.setText(user.username);

        if (user.profileImageUrl != null) {
            holder.ivProfileImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(user.profileImageUrl)
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
        else {
            holder.ivProfileImage.setVisibility(View.VISIBLE);
        }

//        // set onClickListener for the profile image to open the profile activity
//        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // make a new intent
//                Intent intent = new Intent(context, ProfileActivity.class);
//                // put the user into the intent
//                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(user));
//                // start activity with intent
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @BindView(R.id.tvName) TextView tvName;



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
                User user = mUsers.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, ProfileActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(user));
                // show the activity
                context.startActivity(intent);
                // set the activity
                Activity activity = (Activity) context;
                // set the animation
                activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }

        }
    }

        // Clean all elements of the recycler
    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }
}
