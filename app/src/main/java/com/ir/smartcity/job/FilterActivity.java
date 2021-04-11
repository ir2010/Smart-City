package com.ir.smartcity.job;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.ir.smartcity.R;
import com.ir.smartcity.register.LoginActivity;
import com.ir.smartcity.register.RegisterActivity;

import android.view.View;


public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }
    public void ChangeFragment(View view)
    {
        Fragment fragment;
        if(view==findViewById(R.id.button1))
        {
            fragment= new FilterFragment2();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.fragplace,fragment,null);
            ft.commit();
        }
        if(view==findViewById(R.id.button2))
        {
            fragment= new FilterFragment1();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.fragplace,fragment,null);
            ft.commit();
        }
    }
    public void back(View view) {
        startActivity(new Intent(FilterActivity.this, JobsActivity.class));

    }
    public void apply(View view) {
        startActivity(new Intent(FilterActivity.this, JobsActivity.class));
    }
}