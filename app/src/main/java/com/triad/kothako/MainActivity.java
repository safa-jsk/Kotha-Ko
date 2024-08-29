package com.triad.kothako;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView main_userRecyclerView, group_recycler_view;
    user_adapter adapter;
    group_adapter grp_adapter;
    FirebaseDatabase database;
    ArrayList<users> userArrayList;
    ArrayList<groups> group_ArrayList;
    ImageView img_create_group, img_logout, img_camera, img_chat, img_settings;
    DatabaseReference reference, user_reference, group_reference;
    EditText group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
        reference = database.getReference();

        user_reference = reference.child("user");
        userArrayList = new ArrayList<>();
        main_userRecyclerView = findViewById(R.id.layout_main_userRecyclerView);
        main_userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new user_adapter(MainActivity.this, userArrayList);
        main_userRecyclerView.setAdapter(adapter);

        // Load others users to chat in User_Adapter
        user_reference.addValueEventListener(new ValueEventListener() {
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

        group_reference = reference.child("groups");
        group_ArrayList = new ArrayList<>();
        group_recycler_view = findViewById(R.id.group_recycler_view);
        group_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        grp_adapter = new group_adapter(MainActivity.this, group_ArrayList);
        group_recycler_view.setAdapter(grp_adapter);

        // Load Groups to chat in Grp_Adapter
        group_reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                group_ArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    groups group = dataSnapshot.getValue(groups.class);
                    group_ArrayList.add(group);
                }
                grp_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });

        //Buttons to Navigate the App: Create Group, Logout, Camera, Chat, Settings
        img_create_group = findViewById(R.id.create_group);
        img_logout = findViewById(R.id.logout_button);
        img_camera = findViewById(R.id.camera);
        img_chat = findViewById(R.id.chat);
        img_settings = findViewById(R.id.settings);

        img_create_group.setOnClickListener(view -> {
            Dialog dialog = new Dialog(MainActivity.this, R.style.dialogue);
            dialog.setContentView(R.layout.layout_create_group);
            group_name = dialog.findViewById(R.id.edit_group_name);

            Button cancel, create;
            cancel = dialog.findViewById(R.id.button_cancel);
            create = dialog.findViewById(R.id.button_create);

            cancel.setOnClickListener(view1 -> dialog.dismiss());

            create.setOnClickListener(view2 -> {
                if (TextUtils.isEmpty(group_name.getText().toString())){
                    Toast.makeText(MainActivity.this,"Please write group name",Toast.LENGTH_SHORT).show();
                } else{
                    create_group(group_name.getText().toString());
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

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

    public void create_group(String group_name){
        group_reference = reference.child("groups");
        String pfp = "https://firebasestorage.googleapis.com/v0/b/kotha-ko-c09d9.appspot.com/o/group.png?alt=media&token=d2d80069-2c01-470b-a621-506ba7c9836b";
        String group_id = group_name + "_" + Objects.requireNonNull(auth.getCurrentUser()).getUid();
        ArrayList<String> members = new ArrayList<>();
        members.add(auth.getCurrentUser().getUid());
        groups group = new groups(pfp, group_name, group_id, members);
        group_reference.child(group_id).setValue(group).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                Toast.makeText(MainActivity.this,group_name+" group is created successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }
}