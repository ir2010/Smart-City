package com.ir.smartcity.user;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ir.smartcity.R;

public class ProfileActivity extends AppCompatActivity  {
    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        tabLayout=findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);
        /**place holder title as "Chats"
         * when app opens for 1st time
         */
        getSupportActionBar().setTitle("User History");

        final ViewPager viewPager = findViewById(R.id.pager);

        /** Pager adapter decides which fragment to show
         when which page is selected
         **/
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        /** listener to change context when page is swiped left
         * or swiped right
         */

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                /** changes toolbar,tabloyut color
                 * when differernt pages are swipped
                 */
                if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimary));


                    tabLayout.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimary));
                    getWindow().setStatusBarColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimary));

                } else if (tab.getPosition() == 2) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimary));
                    getWindow().setStatusBarColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimary));

                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimaryDark));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimaryDark));
                    getWindow().setStatusBarColor(ContextCompat.getColor(ProfileActivity.this,
                            R.color.colorPrimaryDark));

                }
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