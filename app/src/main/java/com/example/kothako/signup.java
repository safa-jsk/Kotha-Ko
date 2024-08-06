package com.example.kothako;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class signup extends AppCompatActivity {
    TextView text_login;
    EditText edit_user, edit_email, edit_pass, edit_confirm_pass;
    Button button_sign_up;
    CircleImageView image_pfp;
    FirebaseAuth auth;
    Uri image_uri;
    String image_URI;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        text_login = findViewById(R.id.text_login);
        edit_user = findViewById(R.id.edit_user);
        edit_email = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_pass);
        edit_confirm_pass = findViewById(R.id.edit_confirm_pass);
        image_pfp = findViewById(R.id.image_pfp);
        button_sign_up = findViewById(R.id.button_sign_up);


        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edit_user.getText().toString();
                String mail = edit_email.getText().toString();
                String pass = edit_pass.getText().toString();
                String confirm_pass = edit_confirm_pass.getText().toString();
                String status = "Using this App";


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mail) ||
                        TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirm_pass)){
                    Toast.makeText(signup.this, "Enter Valid Information", Toast.LENGTH_SHORT).show();
                }else  if (!mail.matches(emailPattern)){
                    edit_email.setError("Enter Correct Mail");
                }else if (pass.length() < 6 || pass.length() > 20){
                    edit_pass.setError("Password must be between 6 - 20 characters");
                }else if (!pass.equals(confirm_pass)){
                    edit_pass.setError("Password Doesn't Match");
                }else{
                    auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if (image_uri != null){
                                    storageReference.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        image_URI = uri.toString();
                                                        user user = new user(id, name, mail, pass, image_URI, status);
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Intent intent = new Intent(signup.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(signup.this, "Unable to create user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status = "Using this App";
                                    image_URI = "https://firebasestorage.googleapis.com/v0/b/kotha-ko-c09d9.appspot.com/o/user.png?alt=media&token=e499441c-626d-4551-b1a7-c51a976a2bb8";
                                    user user = new user(id, name, mail, pass, image_URI, status);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(signup.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(signup.this, "Unable to create user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }else {
                                Toast.makeText(signup.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }



            }
        });


        image_pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a picture"), 50);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50){
            if (data != null){
                image_uri = data.getData();
                image_pfp.setImageURI(image_uri);
            }
        }
    }
}