package com.example.nicolas.proyectoelectronica;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Settings extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Settings" ;
    private Toolbar toolbar;
    private Button btn_logout;
    private Button btn_updPassword;
    private Button bluetooth;
    private TextView tv_user;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.customToolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });

        FirebaseUser user= firebaseAuth.getCurrentUser();
        tv_user = findViewById(R.id.tv_user);
        if (user==null){
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }else{
            tv_user.setText("Email Logged: "+ user.getEmail());
        }
        setSupportActionBar(toolbar);
        btn_logout = findViewById(R.id.btn_logout);
        btn_updPassword = findViewById(R.id.btn_updPassword);
        bluetooth = findViewById(R.id.bluetooth);
        btn_logout.setOnClickListener(this);
        btn_updPassword.setOnClickListener(this);
        bluetooth.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == btn_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }
        if (v==btn_updPassword) {
            final String email = firebaseAuth.getCurrentUser().getEmail().toString().trim();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Settings.this,"Reset email sent to: "+ email,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        if (v==bluetooth){
            startActivity(new Intent(this, BluetoothSettings.class));
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

