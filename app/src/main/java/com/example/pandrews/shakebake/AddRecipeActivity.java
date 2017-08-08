package com.example.pandrews.shakebake;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRecipeActivity extends AppCompatActivity {

    @BindView(R.id.btnIngredients) FloatingActionButton btnIngredients;
    @BindView(R.id.bRecipeSteps) Button bRecipeSteps;
    @BindView(R.id.bAddImage) Button bAddImage;
    @BindView(R.id.etRecipeTitle) EditText etRecipeTitle;
    @BindView(R.id.etRecipeDescription) EditText etRecipeDescription;
    @BindView(R.id.etKey1) EditText etKey1;
    @BindView(R.id.etKey2) EditText etKey2;
    @BindView(R.id.etKey3) EditText etKey3;
    @BindView(R.id.ivPicture) ImageView ivPicture;
    @BindView(R.id.llSteps) LinearLayout llSteps;
    @BindView(R.id.etIngredient) EditText etIngredient;
    @BindView(R.id.llIngredientList) LinearLayout llIngredientList;
    @BindView(R.id.ivCamera) ImageView ivCamera;

    TextView tvIngredient;
    TextView tvStep;
    TextView tvNumber;

    //params for ingredient/step inflated views
    LinearLayout.LayoutParams layoutParams;


    private Uri outputFileUri;
    Uri imageUri;
    ContentValues values;

    ArrayList<String> keywords;
    ArrayList<String> supplyList;
    ArrayList<String> stepList;

    Context context;
    Uri targetUri;
    Integer stepsCount = 0;
    private StorageReference mStorageRef;
    Uri videoUri;
    String videoUriString;

    String arrayActions[] = new String[2];

    static Context mContext;


    //variables for step:video dictionary
    public HashMap<String, String> stepVideo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_recipe);
        ButterKnife.bind(this);


        arrayActions[0] = "Camera";
        arrayActions[1] = "Gallery";

        //keyboard only pops up when user clicks into an EditText
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mContext = getApplicationContext();
        context = getApplicationContext();

        supplyList = new ArrayList<>();
        stepList = new ArrayList<>();
        keywords = new ArrayList<>();
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //initialize storage reference to videos folder
        mStorageRef = FirebaseStorage.getInstance().getReference("videos");

        stepVideo = new HashMap<>();

        //keyboard only pops up when a user clicks into an EditText
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void onAddStep(View v) {
        stepsCount += 1;
        Integer REQUEST_CODE = 21;
        Intent i = new Intent(this, AddStepsActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 21) {

                final String step = data.getExtras().get("step").toString();
                //add to steplist and get uri to put into firestorage
                stepList.add(step);
                videoUri = Uri.parse(data.getExtras().getString("videoUri"));
                videoUriString = data.getExtras().getString("videoUri");

                //store video to firebase storage
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

            } else if (requestCode == 23) {
                targetUri = data.getData();

                Glide
                        .with(this)
                        .load(targetUri)
                        .asBitmap()
                        .into(ivPicture);

                bAddImage.setVisibility(View.GONE);
                ivCamera.setVisibility(View.GONE);
            } else if (requestCode == 24) {

                Glide
                        .with(this)
                        .load(imageUri)
                        .asBitmap()
                        .centerCrop()
                        .into(ivPicture);

                bAddImage.setVisibility(View.GONE);
                ivCamera.setVisibility(View.GONE);

            }
        }
    }


    //publish recipe
    public void onAdd(View v) {
        //check that all fields are filled
        if (validate()) {
            //bundle all recipe information and send to home timeline fragment via main activity
            Bundle recipe = new Bundle();
            recipe.putString("title", etRecipeTitle.getText().toString());
            recipe.putString("description", etRecipeDescription.getText().toString());
            recipe.putStringArrayList("keywords", createKeywords());
            recipe.putString("targetUri", targetUri.toString());
            recipe.putStringArrayList("stepList", stepList);
            recipe.putStringArrayList("supplyList", supplyList);
            recipe.putSerializable("stepVideo", stepVideo);

            Intent i = new Intent(this, MainActivity.class);
            i.putExtras(recipe);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    public void onAddAnIngredient(View v) {
        String ingredient = etIngredient.getText().toString();
        supplyList.add(ingredient);
        llIngredientList.addView(createNewIngredientView(ingredient));
        etIngredient.setText("");

    }


    public View createNewIngredientView(String text) {
        View view = getLayoutInflater().inflate(R.layout.item_ingredient, null);
        view.setLayoutParams(layoutParams);
        tvIngredient = (TextView) view.findViewById(R.id.tvIngredient);
        tvIngredient.setText(text);
        return view;
    }

    public View createNewStep(String text, Integer count) {
        View view = getLayoutInflater().inflate(R.layout.item_instruction, null);
        view.setLayoutParams(layoutParams);
        tvStep = (TextView) view.findViewById(R.id.tvStep);
        tvNumber = (TextView) view.findViewById(R.id.tvNumber);
        tvStep.setText(text);
        tvNumber.setText(count.toString());
        return view;
    }

    //make keyword list
    public ArrayList<String> createKeywords(){
        ArrayList<String> keyword_list = new ArrayList<>();

        keyword_list.add(etKey1.getText().toString());
        keyword_list.add(etKey2.getText().toString());
        keyword_list.add(etKey3.getText().toString());

        return keyword_list;
    }

    //for deleting ingredients & their views
    public void onDelete(View view) {
        //remove row by calling getParent on button
        View viewParent = (View) view.getParent();
        llIngredientList.removeView((ViewGroup) viewParent.getParent());
        supplyList.remove(tvIngredient.getText().toString());
    }

    //for deleting steps & their views
    public void onDeleteStep(View view) {
        //remove row by calling getParent on button
        View viewParent = (View) view.getParent();
        llSteps.removeView((ViewGroup) viewParent.getParent());
        stepList.remove(tvStep.getText().toString());
    }

    public boolean validate() {
        boolean valid = true;

        String title = etRecipeTitle.getText().toString();
        String key1 = etKey1.getText().toString();
        String key2 = etKey2.getText().toString();
        String key3 = etKey3.getText().toString();
        String description = etRecipeDescription.getText().toString();
        Integer ingredientCount = supplyList.size();
        Integer stepCount = stepList.size();


        if (title.isEmpty() || title.length() < 2) {
            etRecipeTitle.setError("at least 3 characters");
            valid = false;
        } else {
            etRecipeTitle.setError(null);
        }

        if (key1.isEmpty() || key1.length() < 1) {
            etKey1.setError("at least 3 characters");
            valid = false;
        } else {
            etKey1.setError(null);
        }

        if (key2.isEmpty() || key2.length() < 1) {
            etKey2.setError("at least 3 characters");
            valid = false;
        } else {
            etKey2.setError(null);
        }

        if (key3.isEmpty() || key3.length() < 1) {
            etKey3.setError("at least 3 characters");
            valid = false;
        } else {
            etKey3.setError(null);
        }

        if (description.isEmpty() || description.length() < 3) {
            etRecipeDescription.setError("at least 3 characters");
            valid = false;
        } else {
            etRecipeDescription.setError(null);
        }

        if (targetUri == null) {
            Toast.makeText(context, "Add an image", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (ingredientCount < 1) {
            Toast.makeText(context, "Add at least one ingredient", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (stepCount < 1) {
            Toast.makeText(context, "Add at least one step", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    public void onAddImage(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
        //add title and buttons
        builder.setTitle("Pick A Source")
                .setItems(arrayActions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {
                            Integer REQUEST_CODE = 23;
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, REQUEST_CODE);
                        } else {
                            //handle camera intent and image setting here

                            values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your camera");
                            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Integer REQUEST_CODE = 24;
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(i, REQUEST_CODE);
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}










