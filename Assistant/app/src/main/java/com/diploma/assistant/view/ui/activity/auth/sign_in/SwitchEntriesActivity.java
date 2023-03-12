package com.diploma.assistant.view.ui.activity.auth.sign_in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.diploma.assistant.view.adapter.SignAdapter;
import com.google.android.material.tabs.TabLayout;

public class SwitchEntriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.diploma.assistant.databinding.ActivityMainBinding binding = com.diploma.assistant.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tabLayoutLogin.addTab(binding.tabLayoutLogin.newTab().setText("Sign In"));
        binding.tabLayoutLogin.addTab(binding.tabLayoutLogin.newTab().setText("Sign Up"));

        FragmentManager manager = getSupportFragmentManager();
        SignAdapter adapter = new SignAdapter(manager, getLifecycle());
        binding.viewPager2Login.setAdapter(adapter);

        binding.tabLayoutLogin.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager2Login.setCurrentItem(tab.getPosition());
                Log.i("TAG", "onTabSelected");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("TAG", "onTabUnselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("TAG", "onTabReselected");
            }
        });

        binding.viewPager2Login.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayoutLogin.selectTab(  binding.tabLayoutLogin.getTabAt(position));
            }
        });
    }
}