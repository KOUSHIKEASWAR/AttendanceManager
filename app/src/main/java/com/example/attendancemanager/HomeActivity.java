package com.example.attendancemanager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    final Fragment viewAttendanceFragment = new ViewAttendanceFragment();
    static final Fragment attendanceFragment = new AttendanceFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    static Fragment active = attendanceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        fragmentManager.beginTransaction().add(R.id.frameLayout, profileFragment, "3").hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayout, viewAttendanceFragment, "2").hide(viewAttendanceFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayout, attendanceFragment, "1").commit();
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        int id = item.getItemId();
        switch (id){
            case R.id.view_attendance:
                fragmentManager.beginTransaction().hide(active).show(viewAttendanceFragment).commit();
                active = viewAttendanceFragment;
                return true;

            case R.id.attendance:
                fragmentManager.beginTransaction().hide(active).show(attendanceFragment).commit();
                active = attendanceFragment;
                return true;

            case R.id.profile:
                fragmentManager.beginTransaction().hide(active).show(profileFragment).commit();
                active = profileFragment;
                return true;
        }
        return false;
    };
}