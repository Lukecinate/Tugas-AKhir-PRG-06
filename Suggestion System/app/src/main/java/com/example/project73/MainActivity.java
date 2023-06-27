package com.example.project73;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // number of selected tab.
    private int selectedTab = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout addLayout = findViewById(R.id.addLayout);
        final LinearLayout profileLayout = findViewById(R.id.profileLayout);

        final ImageView homeImage = findViewById(R.id.homeImage);
        final ImageView addImage = findViewById(R.id.addImage);
        final ImageView profileImage = findViewById(R.id.profileImage);

        final TextView homeText = findViewById(R.id.homeText);
        final TextView addText = findViewById(R.id.addText);
        final TextView profileText = findViewById(R.id.profileText);

        // set login fragment by default
        Fragment fragment = new HomeFragment().newInstance();

        getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                                .replace(R.id.fragment_container, fragment)
                                        .commit();

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check if home is already selected or not.
                if(selectedTab != 1){

                    // set home fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container, HomeFragment.class, null)
                            .commit();

                    addText.setVisibility(View.GONE);
                    profileText.setVisibility(View.GONE);

                    addImage.setImageResource(R.drawable.icon_add);
                    profileImage.setImageResource(R.drawable.icon_profile);

                    addLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    // select home tab
                    homeText.setVisibility(View.VISIBLE);
                    homeImage.setImageResource(R.drawable.icon_home_selected);
                    homeLayout.setBackgroundResource(R.drawable.round_back_home);

                    // create animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(300);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    // set 1st tab as selected tab
                    selectedTab = 1;
                }
            }
        });

        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if add is already selected or not.
                if(selectedTab != 2){

                    // set add fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container, SuggestFragment.class, null)
                            .commit();

                    homeText.setVisibility(View.GONE);
                    profileText.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.icon_home);
                    profileImage.setImageResource(R.drawable.icon_profile);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    // select home tab
                    addText.setVisibility(View.VISIBLE);
                    addImage.setImageResource(R.drawable.icon_add_selected);
                    addLayout.setBackgroundResource(R.drawable.round_back_add);

                    // create animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f);
                    scaleAnimation.setDuration(300);
                    scaleAnimation.setFillAfter(true);
                    addLayout.startAnimation(scaleAnimation);

                    // set 1st tab as selected tab
                    selectedTab = 2;
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if report is already selected or not.
                if(selectedTab != 3){

                    // set report fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container, AccessDeniedFragment.class, null)
                            .commit();

                    homeText.setVisibility(View.GONE);
                    addText.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.icon_home);
                    addImage.setImageResource(R.drawable.icon_add);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    addLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    // select home tab
                    profileText.setVisibility(View.VISIBLE);
                    profileImage.setImageResource(R.drawable.icon_profile_selected);
                    profileLayout.setBackgroundResource(R.drawable.round_back_profile);

                    // create animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f);
                    scaleAnimation.setDuration(300);
                    scaleAnimation.setFillAfter(true);
                    profileLayout.startAnimation(scaleAnimation);

                    // set 1st tab as selected tab
                    selectedTab = 3;
                }
            }
        });
    }
}