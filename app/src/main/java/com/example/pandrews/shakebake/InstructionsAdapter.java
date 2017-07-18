package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    // constructor
    public InstructionsAdapter(ArrayList<String> steps, Recipe recipe, int position) {
        mSteps = steps;
        mRecipe = recipe;
        fragmentPosition = position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stepsView = inflater.inflate(R.layout.item_step, parent, false);
        ViewHolder viewHolder = new ViewHolder(stepsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the step based on position
        String step = mSteps.get(position);

        // set it in the viewholder
        holder.tvStep.setText(step);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvStep) TextView tvStep;

        public ViewHolder(View itemView) {
            // call the super method
            super(itemView);

            // Bind with ButterKnife
            ButterKnife.bind(this, itemView);

            // set the onClickListener
            itemView.setOnClickListener(this);

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
        }
    }

    public void clear() {
        mSteps.clear();
        notifyDataSetChanged();
    }
}