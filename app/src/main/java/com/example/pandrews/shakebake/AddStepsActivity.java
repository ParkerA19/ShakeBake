package com.example.pandrews.shakebake;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddStepsActivity extends AppCompatActivity {
    Uri videoUri;
    Context context;

    //array and variables for dialog box
    String arrayActions[] = new String[2];
    ContentValues values;
    Uri newVideoUri;


    @BindView(R.id.vvStep) VideoView vvStep;
    @BindView(R.id.etStep) EditText etStep;
    @BindView(R.id.ibVideo) ImageButton ibVideo;
    @BindView(R.id.bAddImage) Button bAddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_steps);
        ButterKnife.bind(this);

        arrayActions[0] = "Film a video";
        arrayActions[1] = "Pick from gallery";

        context = getApplicationContext();
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;

    public void dispatchTakeVideoIntent(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddStepsActivity.this);
            //add title and buttons
            builder.setTitle("Pick A Source")
                    .setItems(arrayActions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 1) {
                                Integer REQUEST_CODE = 23;
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, REQUEST_CODE);
                            } else {
                                //handle camera intent and image setting here
                                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                                }
                            }
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    //get video from camera and play in video viewer
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            vvStep.setVideoURI(videoUri);

            ibVideo.setVisibility(View.GONE);
            bAddImage.setText("");

            vvStep.start();
            vvStep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    vvStep.start();
                }
            });
        } else if (requestCode == 23 && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            vvStep.setVideoURI(videoUri);

            ibVideo.setVisibility(View.GONE);
            bAddImage.setText("");

            vvStep.start();
            vvStep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    vvStep.start();
                }
            });
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
