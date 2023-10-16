package com.example.appbanhang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.fragment.FragmentCart;
import com.example.appbanhang.fragment.FragmentHome;
import com.example.appbanhang.fragment.FragmentNotification;
import com.example.appbanhang.fragment.FragmentUser;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //    public static BottomNavigationView navigationView;
//    public ViewPager viewPager;
    Toolbar toolbar;
    TextView tvTitle;
    public static String emailuser = "";
    public static int cart_count = 0;

    private ChipNavigationBar nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Home");
        tvTitle.setTextSize(30);
        Intent intent = getIntent();
        emailuser = intent.getStringExtra("email");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();

        nav.setItemSelected(R.id.mFood, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmentHome()).commit();
        nav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.mFood:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Home");
                        fragment = new FragmentHome();
                        break;
                    case R.id.mCart:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Cart");
                        fragment = new FragmentCart();
                        break;
                    case R.id.mNotification:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Notification");
                        fragment = new FragmentNotification();
                        break;
                    case R.id.mUser:
                        toolbar.setVisibility(View.GONE);
                        fragment = new FragmentUser();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
            }
        });

//
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        toolbar.setVisibility(View.VISIBLE);
//                        tvTitle.setText("Home");
//                        navigationView.getMenu().findItem(R.id.mFood).setChecked(true);
//                        break;
//                    case 1:
//                        toolbar.setVisibility(View.VISIBLE);
//                        tvTitle.setText("Cart");
//                        navigationView.getMenu().findItem(R.id.mCart).setChecked(true);
//                        break;
//
//                    case 2:
//                        toolbar.setVisibility(View.GONE);
//                        navigationView.getMenu().findItem(R.id.mUser).setChecked(true);
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.mFood:
//                        toolbar.setVisibility(View.VISIBLE);
//                        tvTitle.setText("Home");
//                        viewPager.setCurrentItem(0);
//                        break;
//                    case R.id.mCart:
//                        toolbar.setVisibility(View.VISIBLE);
//                        tvTitle.setText("Cart");
//                        viewPager.setCurrentItem(1);
//                        break;
//                    case R.id.mUser:
//                        toolbar.setVisibility(View.GONE);
//                        viewPager.setCurrentItem(2);
//                        break;
//                }
//                return true;
//            }
//        });
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                if (id == R.id.notification) {
//                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
//                }
//                return true;
//            }
//        });
    }

    private void initView() {
        //        navigationView = findViewById(R.id.nav);
//        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolBar);
        nav = findViewById(R.id.nav);
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.notification) {
//            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
//        }
//        return super.onOptionsItemSelected(item);
//    }

}