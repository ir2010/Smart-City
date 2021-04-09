package com.ir.smartcity.home;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.ir.smartcity.R;
import com.ir.smartcity.community.CommunityActivity;
import com.ir.smartcity.job.JobsActivity;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class BottomNavigation extends AppCompatActivity {

    private ChipNavigationBar bottomNav;

    public void implement(ChipNavigationBar bnav, Context context)
    {
        bottomNav = bnav;
        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if(i == R.id.notification)
                {
                    context.startActivity(new Intent(context, NotificationActivity.class));
                }
                if(i == R.id.jobs)
                {
                    context.startActivity(new Intent(context, JobsActivity.class));
                }
                if(i == R.id.community)
                {
                    context.startActivity(new Intent(context, CommunityActivity.class));
                }
            }
        });
    }



}
