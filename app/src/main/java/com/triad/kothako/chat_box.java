package com.triad.kothako;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_box extends AppCompatActivity {
    String receiver_img, receiver_UID, receiver_Name, sender_UID;
    CircleImageView profile;
    TextView receiverNName;
    CardView card_send;
    EditText edit_chat;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference, sender_reference, receiver_reference;
    public static String senderImg;
    public static String receiverImg;
    String sender_box, receiver_box;
    RecyclerView msg_adapter;
    ArrayList<message_base> messagesArrayList;
    message_adapter messageAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbox);

        // Initialize Firebase instances
        database = FirebaseDatabase.getInstance("https://kotha-ko-c09d9-default-rtdb.firebaseio.com/");
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        msg_adapter = findViewById(R.id.msg_adapter);
        card_send = findViewById(R.id.card_send);
        edit_chat = findViewById(R.id.edit_chat);
        profile = findViewById(R.id.image_Pfp_chat);
        receiverNName = findViewById(R.id.receiver_name);

        // Get data from Intent
        receiver_Name = getIntent().getStringExtra("name");
        receiver_img = getIntent().getStringExtra("receiverImg");
        receiver_UID = getIntent().getStringExtra("uID");
        sender_UID = firebaseAuth.getUid();

        // Initialize messagesArrayList before passing it to the adapter
        messagesArrayList = new ArrayList<>();

        // Set up the RecyclerView and Adapter
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        msg_adapter.setLayoutManager(linearLayoutManager);
        messageAdapter = new message_adapter(this, messagesArrayList);
        msg_adapter.setAdapter(messageAdapter);

        // Set up user interface with received data
        receiverNName.setText(receiver_Name);
        Picasso.get()
                .load(receiver_img)
                .placeholder(R.drawable.man)
                .error(R.drawable.man)
                .into(profile);

        // Set up Firebase references for chat
        sender_box = sender_UID + "_" + receiver_UID;
        receiver_box = receiver_UID + "_" + sender_UID;

        reference = database.getReference().child("user").child(Objects.requireNonNull(firebaseAuth.getUid()));
        sender_reference = database.getReference().child("chat").child(sender_box).child("messages");
        receiver_reference = database.getReference().child("chat").child(receiver_box).child("messages");

        // Retrieve user data from Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = (String) snapshot.child("pfp").getValue();
                receiverImg = receiver_img;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors here
            }
        });

        // Listen for chat messages from Firebase (Either sender_reference or receiver_reference will do)
        sender_reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    message_base messages = dataSnapshot.getValue(message_base.class);
                    messagesArrayList.add(messages);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors here
            }
        });

        // Set up the send button listener
        card_send.setOnClickListener(view -> {
            String message = edit_chat.getText().toString();
            if (message.isEmpty()) {
                Toast.makeText(chat_box.this, "Need to write something", Toast.LENGTH_SHORT).show();
                return;
            }
            edit_chat.setText("");
            Date date = new Date();
            message_base messages = new message_base(message, sender_UID, date.getTime());

            database = FirebaseDatabase.getInstance();
            sender_reference.push()
                    .setValue(messages)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            receiver_reference.push()
                                    .setValue(messages)
                                    .addOnCompleteListener(task1 -> {
                                        if (!task1.isSuccessful()) {
                                            Toast.makeText(chat_box.this, "Failed to send to receiver", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(chat_box.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Handle system UI visibility and insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}