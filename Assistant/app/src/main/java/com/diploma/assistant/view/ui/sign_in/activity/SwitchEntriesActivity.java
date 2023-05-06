package com.diploma.assistant.view.ui.sign_in.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.SignUpInSwipeBinding;
import com.diploma.assistant.view.adapter.SignAdapter;
import com.google.android.material.tabs.TabLayout;

public class SwitchEntriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SignUpInSwipeBinding binding = SignUpInSwipeBinding.inflate(getLayoutInflater());
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
                setCustomTab(binding.tabLayoutLogin);
                Log.i("TAG", "onTabReselected");
            }
        });

        binding.viewPager2Login.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayoutLogin.selectTab(binding.tabLayoutLogin.getTabAt(position));
            }
        });
    }


    private void setCustomTab(TabLayout tabLayoutLogin){
        ViewGroup tabStrip = (ViewGroup) tabLayoutLogin.getChildAt(0);
        for(int i = 0; i < tabLayoutLogin.getTabCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if(tabLayoutLogin.getSelectedTabPosition() == 0)  {
                if(i == 0) ViewCompat.setBackground(tabView, AppCompatResources.getDrawable(tabView.getContext(),R.drawable.tab_indicator_left_selected));
                else ViewCompat.setBackground(tabView, AppCompatResources.getDrawable(tabView.getContext(), R.drawable.tab_indicator_right_unselected));
            } else {
                if(i == 0) ViewCompat.setBackground(tabView, AppCompatResources.getDrawable(tabView.getContext(), R.drawable.tab_indicator_left_unselected));
                else ViewCompat.setBackground(tabView, AppCompatResources.getDrawable(tabView.getContext(), R.drawable.tab_indicator_right_selected));
            }
            ViewCompat.setPaddingRelative(tabView, tabView.getPaddingStart(), tabView.getPaddingTop(), tabView.getPaddingEnd(), tabView.getPaddingBottom());
       }
    }
}