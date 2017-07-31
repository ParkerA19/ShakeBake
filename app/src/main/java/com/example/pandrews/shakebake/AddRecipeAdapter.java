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

public class AddRecipeAdapter extends RecyclerView.Adapter<AddRecipeAdapter.ViewHolder> {

    ArrayList<String> supplyList;
    Context context;
    TextView tvStep;

    public AddRecipeAdapter(ArrayList<String> supplyList, Context context) {
        this.supplyList = supplyList;
    }


    @Override
    public AddRecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_instruction, parent, false);
        tvStep = (TextView) parent.findViewById(R.id.tvStep);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddRecipeAdapter.ViewHolder holder, int position) {
    }


    @Override
    public int getItemCount() {
        if (supplyList == null) {
            return 0;
        } else {
            return supplyList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);

        }
    }

}


