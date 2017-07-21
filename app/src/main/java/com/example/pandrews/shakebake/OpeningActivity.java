package com.example.pandrews.shakebake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpeningActivity extends AppCompatActivity {

    @BindView(R.id.btnSignup) Button btn;
    @BindView(R.id.btnLogin) Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        // Bind with ButterKnife
        ButterKnife.bind(this);

//        Button btn = (Button)findViewById(R.id.btnSignup);
//        Button btn2= (Button)findViewById(R.id.btnLogin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent i= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
