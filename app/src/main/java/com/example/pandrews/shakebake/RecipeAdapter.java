package com.example.pandrews.shakebake;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andreagarcia on 7/11/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    ArrayList<String> supplyList;
    Context context;

    public RecipeAdapter(ArrayList<String> supplyList, Context context) {
        this.supplyList = supplyList;
    }


    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
//        supplyList = new ArrayList<String>();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_step, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        holder.tvStep.setText(supplyList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return supplyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStep;
        public ViewHolder(View itemView) {
            super(itemView);
            tvStep = (TextView) itemView.findViewById(R.id.tvStep);
        }
    }

}
