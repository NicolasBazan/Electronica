package com.example.nicolas.proyectoelectronica;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "MainActivity";
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private TripFragment tripFragment;
    private MonthFragment monthFragment;
    private DayFragment dayFragment;
    private MainFragment mainFragment;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    public TextView info_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.customToolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(mainFragment, "mainFragment");
            }
        });
        setSupportActionBar(toolbar);
        mMainFrame=(FrameLayout) findViewById(R.id.main_frame);
        mMainNav=(BottomNavigationView) findViewById(R.id.main_nav);
        info_text = findViewById(R.id.info_text);
        tripFragment = new TripFragment();
        monthFragment = new MonthFragment();
        dayFragment = new DayFragment();
        mainFragment = new MainFragment();

        firebaseAuth = FirebaseAuth.getInstance();

        setFragment(mainFragment, "mainFragment");
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_trip:
                        mMainNav.setItemBackgroundResource(R.color.colorBlack);
                        setFragment(tripFragment, "tripFragment");
                        return true;

                    case R.id.nav_month:
                        mMainNav.setItemBackgroundResource(R.color.colorBlack);
                        setFragment(monthFragment, "monthFragment");
                        return true;

                    case R.id.nav_day:
                        mMainNav.setItemBackgroundResource(R.color.colorBlack);
                        setFragment(dayFragment, "dayFragment");
                        return true;

                        default:
                            return false;
                }
            }


        });
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
    private void setFragment(Fragment fragment, String name) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentTransaction replace = fragmentTransaction.replace(R.id.main_frame, fragment, name);
        fragmentTransaction.commit();

    }
}

