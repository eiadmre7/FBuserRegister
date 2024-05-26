package com.example.fbuserregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout etMail, etPass;
    private Button btnLogin, btnSignup, btnCancel;
    private String email, password;
    private Firebase firebaseComm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        firebaseComm=new Firebase();
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        // check if user is signed in -
        // show a toast in such a case
        if (Firebase.isUserSignedIn()) {
            String mail = Firebase.authUserEmail();
            Toast.makeText(this, "Signed in as: " + mail, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            Toast.makeText(this, " id = " + uid, Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);
        btnSignup = findViewById(R.id.btnSignup);
        etMail = findViewById(R.id.email);
        etPass = findViewById(R.id.password);
    }

    public void RegisterUser() {
        email = etMail.getEditText().getText().toString();
        password = etPass.getEditText().getText().toString();
        if (email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Fill in fields please..", Toast.LENGTH_SHORT).show();
        else if (firebaseComm.createUser(email, password))
            Toast.makeText(this, "User was Registered", Toast.LENGTH_SHORT).show();
    }

    public void LogIn() {
        email = etMail.getEditText().getText().toString();
        password = etPass.getEditText().getText().toString();
        if (email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Fill in fields please..", Toast.LENGTH_SHORT).show();
        else {
            firebaseComm.loginUser(email, password);
            if (Firebase.isUserSignedIn()) {
                String mail = Firebase.authUserEmail();
                Toast.makeText(this, "Signed in as: " + mail, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, RegisterActivity.class));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            LogIn();
        }
        if (view.getId() == R.id.btnSignup) {
            RegisterUser();
        }
        if (view.getId()==R.id.btnCancel)
                System.exit(0);
    }
}