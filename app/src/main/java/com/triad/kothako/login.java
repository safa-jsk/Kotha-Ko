package com.triad.kothako;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login extends AppCompatActivity {
    Button button;
    TextView signup;
    EditText email, pass;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE); // Initially set invisible, visible if login successful.

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button_login);
        email = findViewById(R.id.edit_email);
        pass = findViewById(R.id.edit_pass);
        signup = findViewById(R.id.text_already_acc);

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, signup.class);
            startActivity(intent);
            finish();
        });

        button.setOnClickListener(view -> {
            String Email = email.getText().toString();
            String Pass = pass.getText().toString();

            if (TextUtils.isEmpty(Email)){
                Toast.makeText(login.this, "Enter Email", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(Pass)){
                Toast.makeText(login.this, "Enter Password", Toast.LENGTH_SHORT).show();
            } else if (!Email.matches(emailPattern)){
                email.setError("Give Proper Gmail Address");
            } else if (pass.length() < 6 || pass.length() > 20){
                pass.setError("Password must be between 6 - 20 characters");
                Toast.makeText(login.this, "Password must be between 6 - 20 characters", Toast.LENGTH_SHORT).show();
            } else {
                auth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        progressBar.setVisibility(View.VISIBLE);
                        try{
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (Exception e){
                            Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}