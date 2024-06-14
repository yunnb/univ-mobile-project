package com.example.mobileproject;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment select = null;
                int itemId = item.getItemId();

                if (itemId == R.id.menu_home) select = new HomeFragment();
                else if (itemId == R.id.menu_news) select = new NewsFragment();
                else if (itemId == R.id.menu_category) select = new CategoryFragment();
                else if (itemId == R.id.menu_diary) select = new DiaryFragment();
                else if (itemId == R.id.menu_my) select = new AlarmFragment();

                if (select != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select).commit();

                return true;
            }
        });

        if (savedInstanceState == null) nav.setSelectedItemId(R.id.menu_home);

    }
}
