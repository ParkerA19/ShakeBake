package com.example.pandrews.shakebake;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    TextView tvTitle;
    private ScrollView mLayout;
    private LinearLayout linearLayout;
    private EditText mEditText;
    public ArrayList<String> supplyList = new ArrayList<String>();
    //used for both ingredient list and steps list
    LinearLayout.LayoutParams lparams;
    Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mLayout = (ScrollView) findViewById(R.id.mLayout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mEditText = (EditText) findViewById(R.id.mEditText);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        mButton = (Button) findViewById(R.id.mButton);
        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setText("New Text");
        String title = getIntent().getStringExtra("title");
        String button = getIntent().getStringExtra("button");
        tvTitle.setText(title);
        mButton.setText(button);
    }

    public void onClick(View v) {
        linearLayout.addView(createNewTextView(mEditText.getText().toString()));
        //mLayout.addView(createNewButton());  add and reformat each line -- TODO

        //append new item to list for use in add recipe activity
        supplyList.add(mEditText.getText().toString());
        mEditText.setText("");
    }

    private TextView createNewTextView(String text) {
        final TextView textView = new TextView(this);
        //final Button bDelete = new Button(this);
        textView.setLayoutParams(lparams);
        //bDelete.setLayoutParams(lparams);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setText(text);
        //bDelete.setBackgroundResource(R.drawable.delete_button);
        return textView;
    }

    // clear text every time user clicks add ingredient -- TODO

    private Button createNewButton() {
        final Button bDelete = new Button(this);
        bDelete.setLayoutParams(lparams);
        bDelete.setBackgroundResource(R.drawable.delete_button);
        return bDelete;
    }


    //call this method from Done button in add_activity.xml
    public void onFinish(View v) {
        Intent i = new Intent(this, AddRecipeActivity.class);
        //or putSerializable
//        bundle.putStringArrayList("supplyList", supplyList);
        i.putStringArrayListExtra("supplyList", supplyList);
//        i.putExtras(bundle);
        setResult(RESULT_OK, i);
        finish();
    }

}
