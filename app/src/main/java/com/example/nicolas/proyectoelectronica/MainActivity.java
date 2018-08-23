package com.example.nicolas.proyectoelectronica;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private TripFragment tripFragment;
    private MonthFragment monthFragment;
    private DayFragment dayFragment;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);
        mMainFrame=(FrameLayout) findViewById(R.id.main_frame);
        mMainNav=(BottomNavigationView) findViewById(R.id.main_nav);
        tripFragment = new TripFragment();
        monthFragment = new MonthFragment();
        dayFragment = new DayFragment();
        setFragment(tripFragment);
        firebaseAuth = FirebaseAuth.getInstance();

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_trip:
                        mMainNav.setItemBackgroundResource(R.color.colorBlack);
                        setFragment(tripFragment);
                        return true;

                    case R.id.nav_month:
                        mMainNav.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(monthFragment);
                        return true;

                    case R.id.nav_day:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(dayFragment);
                        return true;

                        default:
                            return false;
                }
            }


        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentTransaction replace = fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.toolbar_items,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                finish();
                startActivity(new Intent(this, Settings.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

