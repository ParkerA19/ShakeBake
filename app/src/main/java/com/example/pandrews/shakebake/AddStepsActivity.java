package com.example.pandrews.shakebake;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddStepsActivity extends AppCompatActivity {
    Uri videoUri;
    Context context;

    @BindView(R.id.vvStep) VideoView vvStep;
    @BindView(R.id.etStep) EditText etStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_steps);
        ButterKnife.bind(this);

        context = getApplicationContext();
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;

    public void dispatchTakeVideoIntent(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    //get video from camera and play in video viewer
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
        if (videoUri != null) {
            Intent i = new Intent(this, AddRecipeActivity.class);
            i.putExtra("step", etStep.getText().toString());
            i.putExtra("videoUri", videoUri.toString());
            setResult(RESULT_OK, i);
            finish();
        } else {
            Toast.makeText(context, "Add a video", Toast.LENGTH_LONG).show();
        }

    }

}
