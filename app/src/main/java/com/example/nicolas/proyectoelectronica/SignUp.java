package com.example.nicolas.proyectoelectronica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText txt_email;
    private EditText txt_password;
    private EditText txt_confirmPassword;
    private Button btn_signUp;
    private TextView tv_Login;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressDialog = new ProgressDialog(this);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        txt_confirmPassword =findViewById(R.id.txt_confirmPassword);
        btn_signUp = findViewById(R.id.btn_signUp);
        tv_Login = findViewById(R.id.tv_Login);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        btn_signUp.setOnClickListener(this);
        tv_Login.setOnClickListener(this);

    }

    private void signUp(){
        String email = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        String confirmationPassword = txt_confirmPassword.getText().toString().trim();
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
        if(TextUtils.isEmpty(confirmationPassword)){
            progressDialog.dismiss();
            Toast.makeText(this,"Please confirm your password",Toast.LENGTH_SHORT).show();
            return;

        }
        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this,"Registered Successfully, Logged In",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this,"Could not sign in. Try again",Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        if(view == btn_signUp){
            signUp();
        }
        if (view == tv_Login){
            finish();
            startActivity(new Intent(this, Login.class));
        }

    }
}
