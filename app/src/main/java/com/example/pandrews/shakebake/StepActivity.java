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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {
    //init stuff in step activity xml
    Integer stepCount = 2;

    @BindView(R.id.tvStepTitle) TextView tvStepTitle;
    @BindView(R.id.tvStepDescription) TextView tvStepDescription;
    @BindView(R.id.bNext) Button bNext;
    @BindView(R.id.vvStepVideo) VideoView vvStepVideo;
    @Nullable@BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv1) TextView tv1;
    @BindView(R.id.tv2) TextView tv2;
    @BindView(R.id.tv3) TextView tv3;
    @BindView(R.id.bLast) TextView bLast;


    Recipe recipe;
    Uri uri;
    String step;
    String videoName;
    Context context;
    ArrayList<TextView> tvList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        tvList = new ArrayList<>(Arrays.asList(tv1, tv2, tv3));

        //set top toolbar
        setStepNumbers(tvList.get(stepCount - 1));

        //set last button invisible if on first step
        if (stepCount > 1) {
            bLast.setVisibility(View.GONE);
        }

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

        //parse uri for each step's video using value in step dictionary
        uri = Uri.parse("android.resource://com.example.pandrews.shakebake/raw/" + videoName);
        vvStepVideo.setVideoURI(uri);
        vvStepVideo.start();

    }

    public void displayNextStep(View view) {
        //reset title, description, top bar, and video displayed
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

            //set video uri and start video
            videoName = recipe.stepVideo.get("step " + stepCount.toString());
            uri = Uri.parse("android.resource://com.example.pandrews.shakebake/raw/" + videoName);
            vvStepVideo.setVideoURI(uri);
            vvStepVideo.start();

            //if last step, set button to done
            if (stepCount == recipe.steps.size()) {
                //since last step, change button to DONE
                bNext.setText("DONE");
            }
         //if button reads "done" then send intent to next activity
        } else if (bNext.getText().toString().equalsIgnoreCase("DONE")) {
            super.onBackPressed();
        }
    }

    public void changeStepNumbers(TextView tvLast, TextView tvCurrent) {
        //change backgrounds of textviews to match whether it is the current step or not
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

    public void setStepNumbers(TextView tvCurrent) {
        //change backgrounds of current textview
        tvCurrent.setBackgroundResource(R.drawable.noun_202830_edited);
        tvCurrent.setTextColor(ContextCompat.getColor(context, R.color.white));

    }

}
