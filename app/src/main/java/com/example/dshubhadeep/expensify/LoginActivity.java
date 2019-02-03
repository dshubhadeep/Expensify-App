package com.example.dshubhadeep.expensify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // TODO : Add biometric lock

    private TextInputEditText emailTextField, passwordTextField;

    private MaterialButton registerButton, loginButton;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private CollectionReference userRef;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(this, BudgetActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVars();
        setClickListeners();
    }

    private void initVars() {

        // Text fields
        emailTextField = findViewById(R.id.email_edit_text);
        passwordTextField = findViewById(R.id.password_edit_text);

        // Buttons
        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.login_button);

        // Firebase stuff
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");
    }

    private void setClickListeners() {

        // Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTextField.getText().toString();
                String password = passwordTextField.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(LoginActivity.this,
                                            "Woah !! You are signed in.",
                                            Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(LoginActivity.this, BudgetActivity.class);
                                    startActivity(i);

                                    finish();

                                } else {

                                    Toast.makeText(LoginActivity.this,
                                            "Could you please try to enter the correct password ?",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                Log.d("FireBaseAuth", "onClick: LOGIN " + email + " " + password);
            }
        });

        // Register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTextField.getText().toString();
                String password = passwordTextField.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this,
                                            "User signed in",
                                            Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(LoginActivity.this, BudgetActivity.class);
                                    startActivity(i);

                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "You couldn't sign in. You should give up.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });

    }
}
