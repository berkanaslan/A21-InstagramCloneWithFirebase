package com.berkanaslan.instagramclonefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText emailText, passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.mailText);
        passText = findViewById(R.id.passText);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void signInClicked (View view) {
        String email = emailText.getText().toString();
        String password = passText.getText().toString();

        if (email.matches("") || password.matches("")) {
            Toast.makeText(LoginActivity.this,"E-Mail or password is empty. Please check it.",Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void signUpClicked (View view) {
        String email = emailText.getText().toString();
        String password = passText.getText().toString();

        if (email.matches("") || password.matches("")) {
            Toast.makeText(LoginActivity.this,"E-Mail or password is empty. Please check it.",Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "User created!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
