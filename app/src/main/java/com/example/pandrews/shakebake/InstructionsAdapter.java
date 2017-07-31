package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pandrews.shakebake.models.Recipe;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pandrews on 7/18/17.
 */

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.ViewHolder> {

    // Instance Variables
    ArrayList<String> mSteps;
    Context context;
    Recipe mRecipe;
    int fragmentPosition;
    boolean clickable;

    // constructor
    public InstructionsAdapter(ArrayList<String> steps, Recipe recipe, int position, boolean click) {
        mSteps = steps;
        mRecipe = recipe;
        fragmentPosition = position;
        clickable = click;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stepsView = inflater.inflate(R.layout.item_instruction, parent, false);
        ViewHolder viewHolder = new ViewHolder(stepsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the step based on position
        String step = mSteps.get(position);
        // set it in the viewholder
        holder.tvStep.setText(step);

        // set the symbol imageView and the number
        if (fragmentPosition == 0) {
            holder.ivSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.vector_knife));
            holder.tvNumber.setVisibility(View.GONE);
        } else {
            holder.ivSymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chef_hat));
            holder.tvNumber.setText((position + 1) + "");
        }

        holder.ibDelete.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvStep) TextView tvStep;
        @Nullable@BindView(R.id.tvNumber) TextView tvNumber;
        @BindView(R.id.ivSymbol) ImageView ivSymbol;
        @Nullable@BindView(R.id.ibDelete) ImageButton ibDelete;

        public ViewHolder(View itemView) {
            // call the super method
            super(itemView);

            // Bind with ButterKnife
            ButterKnife.bind(this, itemView);

            if (clickable) {
                // set the onClickListener
                itemView.setOnClickListener(this);
            }
        }


        @Override
        public void onClick(View v) {
            // make intent
            Intent intent = new Intent(context, InstructionsActivity.class);
            // pass in recipe
            intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(mRecipe));
            // pass in the fragment position to go to
            intent.putExtra("int", fragmentPosition);
            // start activity
            context.startActivity(intent);
//            // get the Activity
//            Activity activity = (Activity) context;
//            // set the animation
//            activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        }


//        @Override
//        public void onClick(View v) {
//            // make intent
//            Intent intent = new Intent(context, StepsActivity.class);
//            // pass in recipe
//            intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(mRecipe);
//            // start the activity
//            context.startActivity(intent);
//        }
    }

    public void clear() {
        mSteps.clear();
        notifyDataSetChanged();
    }
}
