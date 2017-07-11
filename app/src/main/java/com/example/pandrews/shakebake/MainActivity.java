package com.example.pandrews.shakebake;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.pandrews.shakebake.fragments.RecipesPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // Instance variables
    RecipesPagerAdapter adapterViewPager;

    @BindView(R.id.viewpager) ViewPager vpPager;
    @BindView(R.id.sliding_tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // get the view pager -- bound with butterknife
       // ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        adapterViewPager = new RecipesPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(adapterViewPager);

        // setup the TabLayout to use the viewPager -- bound with butterknife
      //  TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }
}
