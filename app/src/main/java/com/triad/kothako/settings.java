package com.triad.kothako;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class settings extends AppCompatActivity {

    String email, pass, status, pfp, name;
    ImageView settings_pfp;
    EditText settings_name, settings_status;
    Button button_save;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference reference;
    StorageReference storageReference;
    Uri set_image_URI;
    String final_image_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = database.getReference().child("user").child(Objects.requireNonNull(auth.getUid()));
        storageReference = storage.getReference().child("Upload").child(auth.getUid());

        settings_pfp = findViewById(R.id.settings_pfp);
        settings_name = findViewById(R.id.settings_name);
        settings_status = findViewById(R.id.settings_status);
        button_save = findViewById(R.id.button_save);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = (String) snapshot.child("gmail").getValue();
                pass = (String) snapshot.child("pass").getValue();
                pfp = (String) snapshot.child("pfp").getValue();
                status = (String) snapshot.child("status").getValue();
                name = (String) snapshot.child("username").getValue();

                settings_name.setText(name);
                settings_status.setText(status);
                Picasso.get().load(pfp).into(settings_pfp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        settings_pfp.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 50);
        });

        button_save.setOnClickListener(view -> {
            name = settings_name.getText().toString();
            status = settings_status.getText().toString();

            if (set_image_URI != null){
                storageReference.putFile(set_image_URI).addOnCompleteListener(task -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    final_image_URI = uri.toString();
                    users user = new users(auth.getUid(), name, email, pass, final_image_URI, status);
                    reference.setValue(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            Toast.makeText(settings.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(settings.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(settings.this, "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }));
            } else {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    final_image_URI = uri.toString();
                    users user = new users(auth.getUid(), name, email, pass, final_image_URI, status);
                    reference.setValue(user).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(settings.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(settings.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(settings.this, "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50){
            if (data != null){
                set_image_URI = data.getData();
                settings_pfp.setImageURI(set_image_URI);
            }
        }
    }
}