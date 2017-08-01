package com.example.pandrews.shakebake;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.pandrews.shakebake.models.Recipe;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {
    //init stuff in step activity xml
    Integer stepCount = 1;

    @BindView(R.id.tvStepTitle) TextView tvStepTitle;
    @BindView(R.id.tvStepDescription) TextView tvStepDescription;
    @BindView(R.id.bNext) Button bNext;
    @BindView(R.id.vvStepVideo) VideoView vvStepVideo;
    @Nullable@BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv1) TextView tv1;
    @BindView(R.id.tv2) TextView tv2;
    @BindView(R.id.tv3) TextView tv3;
    @BindView(R.id.tv4) TextView tv4;
    @BindView(R.id.bLast) TextView bLast;


    Recipe recipe;
    Uri uri;
    String step;
    String videoName;
    FirebaseDatabase database;
    Context context;
    ArrayList<TextView> tvList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        tvList = new ArrayList<>(Arrays.asList(tv1, tv2, tv3, tv4));

        //set last button invisible
        bLast.setVisibility(View.GONE);

        // get the recipe from the intent
        recipe = Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s", recipe.title));

        context = getApplicationContext();

        step = recipe.steps.get(stepCount - 1);
        //get intent, start setting textviews and videos. have entire recipe passed through intent
        //recipe = Recipe.fromBundle(getIntent().getExtras());

        //set description, title, and video
        tvStepTitle.setText("Step " + stepCount.toString());
        tvStepDescription.setText(step);

        videoName = recipe.stepVideo.get("step " + stepCount.toString());

        //store actual filename in recipe database as value for each step. then append here. since each key is just the step, get step's value
        uri = Uri.parse("android.resource://com.example.pandrews.shakebake/raw/" + videoName);
        //uri = Uri.parse("android.resource://com.example.pandrews.shakebake/raw/" + "cat");
        vvStepVideo.setVideoURI(uri);
        vvStepVideo.start();

    }

    public void displayNextStep(View view) {
        //probably send intent to same activity with next info in list. make public counter that goes through each key in stepVideos and then displays the value in the videoview
        stepCount += 1;
        if (stepCount <= recipe.steps.size()) {
            step = recipe.steps.get(stepCount - 1);
            //set if condition here so if stepcount = length of steps, change button text to done. then set condition that if button text = done, make an intent back to details page
            tvStepTitle.setText("Step " + stepCount.toString());
            tvStepDescription.setText(recipe.steps.get(stepCount - 1));

            //change chef hats
            changeStepNumbers(tvList.get(stepCount - 2), tvList.get(stepCount - 1));

            //display LAST button
            bLast.setVisibility(View.VISIBLE);

            videoName = recipe.stepVideo.get("step " + stepCount.toString());
            uri = Uri.parse("android.resource://com.example.pandrews.shakebake/raw/" + videoName);
            vvStepVideo.setVideoURI(uri);
            vvStepVideo.start();
            if (stepCount == recipe.steps.size()) {
                //since last step, change button to DONE
                bNext.setText("DONE");
            }
        } else if (bNext.getText().toString().equalsIgnoreCase("DONE")) {
            super.onBackPressed();
        }
    }

    public void changeStepNumbers(TextView tvLast, TextView tvCurrent) {
        tvLast.setBackgroundResource(R.drawable.noun_202830_empty);
        tvLast.setTextColor(ContextCompat.getColor(context, R.color.off_black));

        tvCurrent.setBackgroundResource(R.drawable.noun_202830_edited);
        tvCurrent.setTextColor(ContextCompat.getColor(context, R.color.white));

    }

    public void displayLastStep(View view) {
        stepCount -= 1;
        if (stepCount > 0) {
            step = recipe.steps.get(stepCount - 1);
            //set if condition here so if stepcount = length of steps, change button text to done. then set condition that if button text = done, make an intent back to details page
            tvStepTitle.setText("Step " + stepCount.toString());
            tvStepDescription.setText(recipe.steps.get(stepCount - 1));

            //change chef hats
            changeStepNumbers(tvList.get(stepCount), tvList.get(stepCount - 1));

            videoName = recipe.stepVideo.get("step " + stepCount.toString());
            uri = Uri.parse("android.resource://com.example.pandrews.shakebake/raw/" + videoName);
            vvStepVideo.setVideoURI(uri);
            vvStepVideo.start();

            bNext.setText("NEXT STEP");

            if (stepCount - 1 <= 0) {
                bLast.setVisibility(View.GONE);
            }
        }

    }
}
