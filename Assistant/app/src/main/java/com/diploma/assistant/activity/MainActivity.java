package com.diploma.assistant.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.diploma.assistant.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.diploma.assistant.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tabLayout = binding.tabLayoutLogin;
        viewPager2 = binding.viewPager2Login;

        tabLayout.addTab(tabLayout.newTab().setText("Увійти"));
        tabLayout.addTab(tabLayout.newTab().setText("Створити"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentAdapterSignIn fragmentAdapterSignIn = new FragmentAdapterSignIn(fragmentManager, getLifecycle());
        viewPager2.setAdapter(fragmentAdapterSignIn);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}