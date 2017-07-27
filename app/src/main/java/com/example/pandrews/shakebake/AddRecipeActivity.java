package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddRecipeActivity extends AppCompatActivity {
    //Button bAddIngredient;
    ImageButton bAddIngredient;
    Button bRecipeSteps;
    Button bAddImage;
    TextView etRecipeTitle;
    TextView etRecipeDescription;
    EditText etKey1;
    EditText etKey2;
    EditText etKey3;

    ArrayList<String> keywords;
    //from inflated view
    TextView tvIngredient;

    TextView tvStep;
    TextView tvNumber;

    EditText etIngredient;
    LinearLayout llIngredientList;
    LinearLayout llSteps;

    LinearLayout.LayoutParams layoutParams;

    ImageView ivPicture;

    RecyclerView.Adapter adapter;
    ArrayList<String> supplyList;
    ArrayList<String> stepList;
    static Context context;
    Uri targetUri;
    Integer ingredientCount = 0;
    Integer stepsCount = 0;
    private StorageReference mStorageRef;
    Uri videoUri;
    String videoUriString;

    //variables for step:video dictionary
    public HashMap<String, String> stepVideo;
    ObjectMapper mapper = new ObjectMapper();
    String jsonFromStepVideo;

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
        etKey1 = (EditText) findViewById(R.id.etKey1);
        etKey2 = (EditText) findViewById(R.id.etKey2);
        etKey3 = (EditText) findViewById(R.id.etKey3);
        //rvIngredients = (RecyclerView) findViewById(R.id.flIngredients);
        //rvSteps = (RecyclerView) findViewById(R.id.rvSteps);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);

        llSteps = (LinearLayout) findViewById(R.id.llSteps);

        etIngredient = (EditText) findViewById(R.id.etIngredient);
        //llIngredients = (LinearLayout) findViewById(R.id.llIngredients);
        llIngredientList = (LinearLayout) findViewById(R.id.llIngredientList);

        adapter = new AddRecipeAdapter(supplyList, this);
        //rvIngredients.setAdapter(adapter);
        supplyList = new ArrayList<>();
        stepList = new ArrayList<>();
        keywords = new ArrayList<>();

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //initialize storage
        mStorageRef = FirebaseStorage.getInstance().getReference("videos");

        stepVideo = new HashMap<>();
    }

//    public void onAddIngredient(View v) {
//        Integer REQUEST_CODE = 20;
//        Intent i = new Intent(this, AddActivity.class);
//        i.putExtra("title", "Add Ingredients");
//        i.putExtra("button", "Add Ingredient");
//        startActivityForResult(i, REQUEST_CODE);
//    }

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
//            if (requestCode == 20) {
//                //add ingredients to list in add recipe activity
//                supplyList = data.getExtras().getStringArrayList("supplyList");
//                //rvIngredients.setLayoutManager(new LinearLayoutManager(this));
//                AddRecipeAdapter rAdapter = new AddRecipeAdapter(supplyList, this);
//                //rvIngredients.setAdapter(rAdapter);
//            }
                if (requestCode == 21) {
                final String step = data.getExtras().get("step").toString();

                stepList.add(step);
                if (data.getExtras().getString("videoUri") == null) {
                    //display a toast that tells user to delete step and remake with proper video if videoUri is null
                    Toast.makeText(context, "Step is missing a video. Remake and add a video", Toast.LENGTH_LONG).show();
                } else {
                    videoUri = Uri.parse(data.getExtras().getString("videoUri"));
                    videoUriString = data.getExtras().getString("videoUri");
                }


                StorageReference videoRef = mStorageRef.child(videoUri.getLastPathSegment());

                videoRef.putFile(videoUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String downloadString = downloadUrl.toString();
                                stepVideo.put(step, downloadString);
                                //Toast.makeText(context, downloadUrl.toString(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //handle unsuccessful uploads
                            }
                        });

                llSteps.addView(createNewStep(step, stepsCount));

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
        recipe.putStringArrayList("keywords", createKeywords());
        recipe.putString("targetUri", targetUri.toString());
        recipe.putStringArrayList("stepList", stepList);
        recipe.putStringArrayList("supplyList", supplyList);
        //turn hashmap into string before sending as intent
//        try {
//            jsonFromStepVideo = mapper.writeValueAsString(stepVideo);
//            recipe.putString("stepVideo", jsonFromStepVideo);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        recipe.putSerializable("stepVideo", stepVideo);
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
        ImageButton ibDelete = (ImageButton) view.findViewById(R.id.ibDelete);

        return view;
    }

    public View createNewStep(String text, Integer count) {
        View view = getLayoutInflater().inflate(R.layout.item_instruction, null);
        view.setLayoutParams(layoutParams);
        tvStep = (TextView) view.findViewById(R.id.tvStep);
        tvNumber = (TextView) view.findViewById(R.id.tvNumber);
        tvStep.setText(text);
        tvNumber.setText(count.toString());
        ImageButton ibDelete = (ImageButton) view.findViewById(R.id.ibDelete);

        return view;
    }

    public ArrayList<String> createKeywords(){
        ArrayList<String> keyword_list = new ArrayList<>();
//        for(String word : keywords.trim().split(" ")) {
//            keyword_list.add(word);
//        }

        keyword_list.add(etKey1.getText().toString());
        keyword_list.add(etKey2.getText().toString());
        keyword_list.add(etKey3.getText().toString());

        return keyword_list;
    }

    public void onDelete(View view) {
        //remove row by calling getParent on button
        View viewParent = (View) view.getParent();
        llIngredientList.removeView((ViewGroup) viewParent.getParent());
        supplyList.remove(tvIngredient.getText().toString());
    }

    public void onDeleteStep(View view) {
        //remove row by calling getParent on button
        View viewParent = (View) view.getParent();
        llSteps.removeView((ViewGroup) viewParent.getParent());
        stepList.remove(tvStep.getText().toString());
    }
}
