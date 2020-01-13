package com.bank.it.jobs.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bank.it.jobs.Adapters.PagerAdapterAllTabs;
import com.bank.it.jobs.R;
import com.google.android.material.tabs.TabLayout;

public class AllTabs extends AppCompatActivity {
    private TabLayout tabLayout;
    private int tab_value;
    ImageView back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tabs);

        // Hide Action bar
        getSupportActionBar().hide();

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        tab_value = intent.getIntExtra("layout", 0);

        if (Build.VERSION.SDK_INT >= 21) {
            // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
            //status bar or the time bar at the top
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }


        tabLayout = findViewById(R.id.tab_layout);


        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.addTab(tabLayout.newTab().setText("Study By Others"));
        tabLayout.addTab(tabLayout.newTab().setText("Your Study"));
        tabLayout.addTab(tabLayout.newTab().setText("Your Collection"));
        tabLayout.addTab(tabLayout.newTab().setText("Saved Collection"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        /*on tab viewpager change code is here*/

        final ViewPager viewPager1 = findViewById(R.id.pager);
        PagerAdapterAllTabs adapter = new PagerAdapterAllTabs(getSupportFragmentManager(), 4);
        viewPager1.setAdapter(adapter);
        viewPager1.setOffscreenPageLimit(1);
        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


//      tabLayout.getTabAt(tab_value);
        if (tab_value >= 0) {
            tabLayout.setScrollPosition(tab_value, 0f, true);
            viewPager1.setCurrentItem(tab_value);
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager1.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
