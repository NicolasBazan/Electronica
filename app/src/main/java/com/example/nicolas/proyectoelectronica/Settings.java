package com.example.nicolas.proyectoelectronica;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Settings extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btn_logout;
    private Button btn_home;
    private TextView tv_user;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.customToolbar);
        FirebaseUser user= firebaseAuth.getCurrentUser();
        tv_user = findViewById(R.id.tv_user);
        if (user==null){
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }else{
            tv_user.setText("Email Logged: "+ user.getEmail());
        }
        setSupportActionBar(toolbar);
        btn_home = findViewById(R.id.btn_home);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }
        if (v==btn_home){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
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

