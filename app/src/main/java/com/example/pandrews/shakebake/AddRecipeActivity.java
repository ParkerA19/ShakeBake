package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {
    //Button bAddIngredient;
    ImageButton bAddIngredient;
    Button bRecipeSteps;
    Button bAddImage;
    TextView etRecipeTitle;
    TextView etRecipeDescription;
    TextView etRecipeKeywords;

    //from inflated view
    TextView tvIngredient;

    EditText etIngredient;
    LinearLayout llIngredientList;
    LinearLayout llSteps;

    LinearLayout.LayoutParams layoutParams;

    ImageView ivPicture;
    //RecyclerView rvIngredients;
    //RecyclerView rvSteps;
    //RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> supplyList;
    ArrayList<String> stepList;
    static Context context;
    Uri targetUri;
    Integer ingredientCount = 0;
    Integer stepsCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_recipe);
        context = getApplicationContext();
        //bAddIngredient = (Button) findViewById(R.id.bAddIngredients);
        bAddIngredient = (ImageButton) findViewById(R.id.bAddIngredients);
        bRecipeSteps = (Button) findViewById(R.id.bRecipeSteps);
        bAddImage = (Button) findViewById(R.id.bAddImage);
        etRecipeTitle = (TextView) findViewById(R.id.etRecipeTitle);
        etRecipeDescription = (TextView) findViewById(R.id.etRecipeDescription);
        etRecipeKeywords = (TextView) findViewById(R.id.etRecipeKeywords);
        //rvIngredients = (RecyclerView) findViewById(R.id.flIngredients);
        //rvSteps = (RecyclerView) findViewById(R.id.rvSteps);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);

        llSteps = (LinearLayout) findViewById(R.id.llSteps);

        etIngredient = (EditText) findViewById(R.id.etIngredient);
        //llIngredients = (LinearLayout) findViewById(R.id.llIngredients);
        llIngredientList = (LinearLayout) findViewById(R.id.llIngredientList);

        //layoutManager = new LinearLayoutManager(this);

        adapter = new AddRecipeAdapter(supplyList, this);
        //rvIngredients.setAdapter(adapter);
        supplyList = new ArrayList<>();
        stepList = new ArrayList<>();

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void onAddIngredient(View v) {
        Integer REQUEST_CODE = 20;
        Intent i = new Intent(this, AddActivity.class);
        i.putExtra("title", "Add Ingredients");
        i.putExtra("button", "Add Ingredient");
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onAddStep(View v) {
        stepsCount += 1;
        Integer REQUEST_CODE = 21;
        Intent i = new Intent(this, AddStepsActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onAddImage(View v) {
        Integer REQUEST_CODE = 23;
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == 20) {
                //add ingredients to list in add recipe activity
                supplyList = data.getExtras().getStringArrayList("supplyList");
                //rvIngredients.setLayoutManager(new LinearLayoutManager(this));
                AddRecipeAdapter rAdapter = new AddRecipeAdapter(supplyList, this);
                //rvIngredients.setAdapter(rAdapter);
            } else if (requestCode == 21) {
                String step = data.getExtras().get("step").toString();
                stepList.add(step);
                //rvSteps.setLayoutManager(new LinearLayoutManager(this));
//                if (data.getExtras().get("videoUri") != null) {
//                    //display a toast that tells user to delete step and remake with proper video if videoUri is null
//                    //maybe later make a button that when user clicks lets them preview their video
//                }
                //display step in same format as item.ingredient


                //count problem will be fixed when I make a separate layout for steps and therefore another createNewTextView
                llSteps.addView(createNewTextView(step, stepsCount));



                //eventually i probably want to map steps to videos in a dictionary

                // if old steps are erased when new ones are added, try moving below lines outside method into onCreate  ------ TODO
                AddRecipeAdapter rAdapter = new AddRecipeAdapter(stepList, this);


            } else {
                //code for adding picture from that intent
                targetUri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    ivPicture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onAdd(View v) {
        //bundle all recipe information and send to home timeline fragment via main activity
        Bundle recipe = new Bundle();
        recipe.putString("title", etRecipeTitle.getText().toString());
        recipe.putString("description", etRecipeDescription.getText().toString());
        recipe.putStringArrayList("keywords", createKeywords(etRecipeKeywords.getText().toString()));
        recipe.putString("targetUri", targetUri.toString());
        recipe.putStringArrayList("stepList", stepList);
        recipe.putStringArrayList("supplyList", supplyList);
//      check intent is going to correct location
        Intent i = new Intent(this, MainActivity.class);
        i.putExtras(recipe);
        setResult(RESULT_OK, i);
//        startActivity(i);
        finish();
    }

    public void onAddAnIngredient(View v) {
        ingredientCount += 1;
        String ingredient = etIngredient.getText().toString();
        supplyList.add(ingredient);
        //possibly instead of adding to llIngredients, create new vertical linear layout and add view to there
        llIngredientList.addView(createNewTextView(ingredient, ingredientCount));
        etIngredient.setText("");

        //mLayout.addView(createNewButton());  add and reformat each line. createNewButton is in AddActivity -- TODO

    }


    public View createNewTextView(String text, Integer count) {
        View view = getLayoutInflater().inflate(R.layout.item_ingredient, null);
        view.setLayoutParams(layoutParams);
        tvIngredient = (TextView) view.findViewById(R.id.tvIngredient);
        tvIngredient.setText(text);
        TextView tvNumber = (TextView) view.findViewById(R.id.tvNumber);
        tvNumber.setText(count.toString());
        ImageButton ibDelete = (ImageButton) view.findViewById(R.id.ibDelete);

        return view;
    }

    public ArrayList<String> createKeywords(String keywords){
        ArrayList<String> keyword_list = new ArrayList<String>();
        for(String word : keywords.trim().split(" ")) {
            keyword_list.add(word);
        }
        return keyword_list;
    }

    public void onDelete(View view) {
        //remove row by calling getParent on button
        View viewParent = (View) view.getParent();
        //((ViewManager)view.getParent()).removeView(view);
        //((ViewManager)llIngredientList).removeView(viewParent);
        //((ViewManager)view.getParent().getParent().removeView(view));
        llIngredientList.removeView((View) viewParent);
        supplyList.remove(tvIngredient.getText().toString());
    }
}
