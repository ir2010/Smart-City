package com.ir.smartcity.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.ir.smartcity.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity  {
    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;
    private CircleImageView ProfileImage;
    private static final int Pick_image=1;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        tabLayout=findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);

        ProfileImage=(CircleImageView) findViewById(R.id.profile_pic);
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery= new Intent();
                gallery.setType("image/*");

                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),Pick_image);


            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Pick_image && resultCode == RESULT_OK) {
            imageUri = data.getData();

            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    ProfileImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}