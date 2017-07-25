package com.example.pandrews.shakebake;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.VideoView;

public class AddStepsActivity extends AppCompatActivity {
    VideoView vvStep;
    EditText etStep;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_steps);
        vvStep = (VideoView) findViewById(R.id.vvStep);
        etStep = (EditText) findViewById(R.id.etStep);
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;

    public void dispatchTakeVideoIntent(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            vvStep.setVideoURI(videoUri);
            //vvStep.requestFocus();
            vvStep.start();
        }
    }

    public void onAddingStep(View view){
        Intent i = new Intent(this, AddRecipeActivity.class);
        i.putExtra("step", etStep.getText().toString());
        i.putExtra("videoUri", videoUri.toString());
        setResult(RESULT_OK, i);
        finish();
    }

}
