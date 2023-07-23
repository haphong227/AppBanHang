package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.appbanhang.adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static BottomNavigationView navigationView;
    public ViewPager viewPager;
    Toolbar toolbar;
    TextView tvTitle;
    public static String emailuser = "";
    public static int cart_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.nav);
        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Home");
        tvTitle.setTextSize(30);
        Intent intent = getIntent();
        emailuser = intent.getStringExtra("email");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Home");
                        navigationView.getMenu().findItem(R.id.mFood).setChecked(true);
                        break;
                    case 1:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Cart");
                        navigationView.getMenu().findItem(R.id.mCart).setChecked(true);
                        break;

                    case 2:
                        toolbar.setVisibility(View.GONE);
                        navigationView.getMenu().findItem(R.id.mUser).setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mFood:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Home");
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.mCart:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Cart");
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.mUser:
                        toolbar.setVisibility(View.GONE);
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }
}