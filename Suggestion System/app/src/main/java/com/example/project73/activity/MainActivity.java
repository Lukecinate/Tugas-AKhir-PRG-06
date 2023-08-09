package com.example.project73.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project73.R;
import com.example.project73.fragment.AccessDeniedFragment;
import com.example.project73.fragment.HomeFragment;
import com.example.project73.fragment.ProfileFragment;
import com.example.project73.fragment.SuggestFragment;

public class MainActivity extends AppCompatActivity {

    // number of selected tab.
    private int selectedTab = 1;
    private LinearLayout mHomeLayout;
    private LinearLayout mAddLayout;
    private LinearLayout mProfileLayout;

    private ImageView mHomeImage;
    private ImageView mAddImage;
    private ImageView mProfileImage;
    private TextView mHomeText, mAddText, mProfileText;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHomeLayout = findViewById(R.id.homeLayout);
        mAddLayout = findViewById(R.id.addLayout);
        mProfileLayout = findViewById(R.id.profileLayout);

        mHomeImage = findViewById(R.id.homeImage);
        mAddImage = findViewById(R.id.addImage);
        mProfileImage = findViewById(R.id.profileImage);

        mHomeText = findViewById(R.id.homeText);
        mAddText = findViewById(R.id.addText);
        mProfileText = findViewById(R.id.profileText);

        // set login fragment by default
        Fragment fragment = new HomeFragment().newInstance();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, fragment)
                .commit();

        // Set click listeners for each tab button
        setHomeTabClickListener(mHomeLayout, mHomeText, mHomeImage);
        setAddTabClickListener(mAddLayout, mAddText, mAddImage);
        setProfileTabClickListener(mProfileImage, mProfileText, mProfileLayout);
    }

    public void setHomeTabClickListener(final View homeLayout, final TextView homeText, final ImageView homeImage) {
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 1) {
                    // set home fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                    resetTabUI();
                    homeText.setVisibility(View.VISIBLE);
                    homeImage.setImageResource(R.drawable.icon_home_selected);
                    homeLayout.setBackgroundResource(R.drawable.round_back_home);
                    animateTab(homeLayout, 0.0f, 1.0f);
                    selectedTab = 1;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAddTabClickListener(final View addLayout, final TextView addText, final ImageView addImage) {
        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 2) {
                    // set add fragment
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                    if (!isLoggedIn) {
                        // If the user is not logged in, show the AccessDeniedFragment
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container, new AccessDeniedFragment())
                                .commit();
                    } else {
                        // If the user is logged in, show the SuggestFragment
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container, new SuggestFragment())
                                .commit();
                    }

                    resetTabUI();
                    addText.setVisibility(View.VISIBLE);
                    addImage.setImageResource(R.drawable.icon_add_selected);
                    addLayout.setBackgroundResource(R.drawable.round_back_add);
                    animateTab(addLayout, 1.0f, 1.0f);
                    selectedTab = 2;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setProfileTabClickListener(final ImageView profileImage, final TextView profileText, final View profileLayout) {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 3) {
                    // set report fragment
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                    if (!isLoggedIn) {
                        // If the user is not logged in, show the AccessDeniedFragment
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container, new AccessDeniedFragment())
                                .commit();
                    } else {
                        // If the user is logged in, show the ProfileFragment
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container, new ProfileFragment())
                                .commit();
                    }

                    resetTabUI();
                    profileText.setVisibility(View.VISIBLE);
                    profileImage.setImageResource(R.drawable.icon_profile_selected);
                    profileLayout.setBackgroundResource(R.drawable.round_back_profile);
                    animateTab(profileLayout, 1.0f, 1.0f);
                    selectedTab = 3;
                }
            }
        });
    }

    private void resetTabUI() {
        findViewById(R.id.homeText).setVisibility(View.GONE);
        findViewById(R.id.addText).setVisibility(View.GONE);
        findViewById(R.id.profileText).setVisibility(View.GONE);

        ((ImageView) findViewById(R.id.homeImage)).setImageResource(R.drawable.icon_home);
        ((ImageView) findViewById(R.id.addImage)).setImageResource(R.drawable.icon_add);
        ((ImageView) findViewById(R.id.profileImage)).setImageResource(R.drawable.icon_profile);

        findViewById(R.id.homeLayout).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.addLayout).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.profileLayout).setBackgroundResource(android.R.color.transparent);
    }

    private void animateTab(final View tabLayout, float fromScale, float toScale) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromScale, toScale, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, fromScale);
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);
        tabLayout.startAnimation(scaleAnimation);
    }
}
