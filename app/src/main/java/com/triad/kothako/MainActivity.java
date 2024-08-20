package com.triad.kothako;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView main_userRecyclerView;
    user_adapter adapter;
    FirebaseDatabase database;
    ArrayList<users> userArrayList;
    ImageView img_logout, img_camera, img_chat, img_settings;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent intent=new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }

        database = FirebaseDatabase.getInstance("https://kotha-ko-c09d9-default-rtdb.firebaseio.com/");
        reference = database.getReference().child("user");
        userArrayList = new ArrayList<>();

        main_userRecyclerView = findViewById(R.id.layout_main_userRecyclerView);
        main_userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new user_adapter(MainActivity.this, userArrayList);
        main_userRecyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    users user = dataSnapshot.getValue(users.class);
                    userArrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });

        /*
          Buttons to Navigate the App
          Logout, Camera, Chat, Settings
         */

        img_logout = findViewById(R.id.logout_button);
        img_camera = findViewById(R.id.camera);
        img_chat = findViewById(R.id.chat);
        img_settings = findViewById(R.id.settings);

        img_logout.setOnClickListener(view -> {
            Dialog dialog = new Dialog(MainActivity.this, R.style.dialogue);
            dialog.setContentView(R.layout.dialogue_layout);
            Button no, yes;
            no = dialog.findViewById(R.id.button_no);
            yes = dialog.findViewById(R.id.button_yes);

            no.setOnClickListener(view1 -> dialog.dismiss());

            yes.setOnClickListener(view12 -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                finish();
            });
            dialog.show();
        });

        img_camera.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 50);
        });

        img_chat.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        img_settings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, settings.class);
            startActivity(intent);
        });

    }
}