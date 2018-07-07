package com.monstertechno.hiddendairies.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.monstertechno.hiddendairies.FormActivity;
import com.monstertechno.hiddendairies.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

        private FirebaseAuth mAuth;
        private EditText textEmail;
        private EditText textPass;
        private Button btnRegister;
        private ProgressDialog progressDialog;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private Button Signup;
        private TextView forgot;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(1);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            setContentView(R.layout.activity_login);

            progressDialog = new ProgressDialog(this);
            textEmail = (EditText) findViewById(R.id.etxt_email);
            textPass = (EditText) findViewById(R.id.etxt_password);
            btnRegister = (Button) findViewById(R.id.btn_login);
            mAuth = FirebaseAuth.getInstance();
            Signup = (Button) findViewById(R.id.signup);
            forgot = (TextView) findViewById(R.id.forgot);

            forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
                    finish();
                }
            });

            Signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                    finish();
                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doLogin();
                }
            });

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        Toast.makeText(LoginActivity.this, "Now you are logged In " + firebaseAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, FormActivity.class);
                        startActivity(intent);
                        finish();
                        //mAuth.signOut();
                    }
                }
            };
        }

        @Override
        protected void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        private void doLogin() {
            String email = textEmail.getText().toString().trim();
            String password = textPass.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                progressDialog.setMessage("Logging , please wait...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(LoginActivity.this,"Error try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else if(TextUtils.isEmpty(email)){
                Toast.makeText(LoginActivity.this, "Enter your email first", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(password)){
                Toast.makeText(LoginActivity.this, "Enter your Password", Toast.LENGTH_SHORT).show();
            }
    }
}

