package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {
    Button bAddIngredient;
    Button bRecipeSteps;
    Button bAddImage;
    TextView etRecipeTitle;
    TextView etRecipeDescription;
    TextView etRecipeKeywords;
    ImageView ivPicture;
    RecyclerView rvIngredients;
    RecyclerView rvSteps;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> supplyList;
    ArrayList<String> stepList;
    static Context context;
    Uri targetUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        context = getApplicationContext();
        bAddIngredient = (Button) findViewById(R.id.bAddIngredients);
        bRecipeSteps = (Button) findViewById(R.id.bRecipeSteps);
        bAddImage = (Button) findViewById(R.id.bAddImage);
        etRecipeTitle = (TextView) findViewById(R.id.etRecipeTitle);
        etRecipeDescription = (TextView) findViewById(R.id.etRecipeDescription);
        etRecipeKeywords = (TextView) findViewById(R.id.etRecipeKeywords);
        rvIngredients = (RecyclerView) findViewById(R.id.flIngredients);
        rvSteps = (RecyclerView) findViewById(R.id.rvSteps);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);

        layoutManager = new LinearLayoutManager(this);

        adapter = new AddRecipeAdapter(supplyList, this);
        rvIngredients.setAdapter(adapter);
        supplyList = new ArrayList<>();
        stepList = new ArrayList<>();
    }

    public void onAddIngredient(View v) {
        Integer REQUEST_CODE = 20;
        Intent i = new Intent(this, AddActivity.class);
        i.putExtra("title", "Add Ingredients");
        i.putExtra("button", "Add Ingredient");
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onAddStep(View v) {
        Integer REQUEST_CODE = 21;
        Intent i = new Intent(this, AddActivity.class);
        i.putExtra("title", "Add Steps");
        i.putExtra("button", "Add Step");
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
        if (requestCode == 20) {
            //add ingredients to list in add recipe activity
            supplyList = data.getExtras().getStringArrayList("supplyList");
            rvIngredients.setLayoutManager(new LinearLayoutManager(this));
            AddRecipeAdapter rAdapter = new AddRecipeAdapter(supplyList, this);
            rvIngredients.setAdapter(rAdapter);
        } else if (requestCode == 21) {
            stepList = data.getExtras().getStringArrayList("supplyList");
            rvSteps.setLayoutManager(new LinearLayoutManager(this));
            AddRecipeAdapter rAdapter = new AddRecipeAdapter(stepList, this);
            rvSteps.setAdapter(rAdapter);
        } else {
            //code for adding picture from that intent
            targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
//                try {
//                    handleSamplingAndRotationBitmap(context, targetUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                ivPicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void onAdd(View v) {
        //bundle all recipe information and send to home timeline fragment via main activity
        Bundle recipe = new Bundle();
        recipe.putString("title", etRecipeTitle.getText().toString());
        recipe.putString("description", etRecipeDescription.getText().toString());
        recipe.putString("keywords", etRecipeKeywords.getText().toString());
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

 // figure out if intent for result can be replaced with a regular intent so it doesnt crash when you go back to add recipe -- TODO
    // add comments -- TODO

//solve problem so that when user presses done in android keyboard, app doesnt crash -- TODO


//this code adds recipes to the database
    //delete data change listener and just set a call to the database in refresh to go through and add more recipes
    //also maybe add id tags to recipes so in the call to refresh, code is not searching entire database and just most recent recipes  -- TODO


//    FirebaseDatabase database =  FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("Recipe 2");
//        myRef.setValue(r2);
//        myRef.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            //called once with initial value and again when data here is updated
//            //crashes because original value is a recipe and not string. change to make compatible with recipe or get recipe.title -- TODO
//
//            //String value = dataSnapshot.getValue(String.class);
//            Log.d(TAG, "Value is: ");
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            Log.w(TAG, "Failed to read value.", databaseError.toException());
//        }
//    });








//    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
//            throws IOException {
//        int MAX_HEIGHT = 1024;
//        int MAX_WIDTH = 1024;
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
//        BitmapFactory.decodeStream(imageStream, null, options);
//        imageStream.close();
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        imageStream = context.getContentResolver().openInputStream(selectedImage);
//        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);
//
//        img = rotateImageIfRequired(img, selectedImage);
//        return img;
//    }
//
//    private static int calculateInSampleSize(BitmapFactory.Options options,
//                                             int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            // Calculate ratios of height and width to requested height and width
//            final int heightRatio = Math.round((float) height / (float) reqHeight);
//            final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
//            // with both dimensions larger than or equal to the requested height and width.
//            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//
//            // This offers some additional logic in case the image has a strange
//            // aspect ratio. For example, a panorama may have a much larger
//            // width than height. In these cases the total pixels might still
//            // end up being too large to fit comfortably in memory, so we should
//            // be more aggressive with sample down the image (=larger inSampleSize).
//
//            final float totalPixels = width * height;
//
//            // Anything more than 2x the requested pixels we'll sample down further
//            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
//
//            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
//                inSampleSize++;
//            }
//        }
//        return inSampleSize;
//    }
//
//    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
//        InputStream input = context.getContentResolver().openInputStream(selectedImage);
//        //InputStream input = getContext().getContentResolver().openInputStream(selectedImage);
//        ExifInterface ei;
//        if (Build.VERSION.SDK_INT > 23)
//            ei = new ExifInterface(input);
//        else
//            ei = new ExifInterface(selectedImage.getPath());
//
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotateImage(img, 90);
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotateImage(img, 180);
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotateImage(img, 270);
//            default:
//                return img;
//        }
//    }
//
//    private static Bitmap rotateImage(Bitmap img, int degree) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degree);
//        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
//        img.recycle();
//        Bitmap bitmap = rotatedImg;
//        return bitmap;
//    }
    //possiboly make call to set image to rotated image within line that sets it -- line 108

}
