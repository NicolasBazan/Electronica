package com.example.nicolas.proyectoelectronica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private EditText txt_email;
    private EditText txt_password;
    private TextView tv_signUp;
    private TextView tv_resetPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();

        if (user!=null){
            finish();
            startActivity(new Intent(Login.this, MainActivity.class));
        }
        btn_login = findViewById(R.id.btn_login);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        tv_signUp = findViewById(R.id.tv_signUp);
        tv_resetPassword = findViewById(R.id.tv_resetPassword);
        progressDialog = new ProgressDialog(this);
        btn_login.setOnClickListener(this);
        tv_signUp.setOnClickListener(this);
        tv_resetPassword.setOnClickListener(this);

    }
    private void login() {
        String email = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            progressDialog.dismiss();
            Toast.makeText(this,"Email can't be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            progressDialog.dismiss();
            Toast.makeText(this,"Password can't be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        firebaseAuth.signOut();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this,"Logged in  Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else{
                            progressDialog.dismiss();
                                Toast.makeText(Login.this,"Could not log in. Try again",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view ==btn_login){
            login();
        }
        if (view==tv_signUp){
            finish();
            startActivity(new Intent(this, SignUp.class));
        }
        if (view==tv_resetPassword){
            String email = txt_email.getText().toString().trim();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this,"Reset email sent",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}
