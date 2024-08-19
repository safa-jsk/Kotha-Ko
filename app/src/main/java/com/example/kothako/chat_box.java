package com.example.kothako;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_box extends AppCompatActivity {
    String receiver_img, receiver_UID, receiver_Name, sender_UID;
    CircleImageView profile;
    TextView receiverNName;
    CardView card_send;
    EditText edit_chat;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String receiverImg;
    String sender_box, receiver_box;
    RecyclerView match_parentt;
    ArrayList<message_base> messagesArrayList;
    message_adapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbox);

        match_parentt = findViewById(R.id.msgadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        match_parentt.setLayoutManager(linearLayoutManager);
        messageAdapter = new message_adapter(chat_box.this , messagesArrayList);
        match_parentt.setAdapter(messageAdapter);


        receiver_Name =getIntent().getStringExtra("name");
        receiver_img =getIntent().getStringExtra("receiverImg");
        receiver_UID =getIntent().getStringExtra("uID");

        messagesArrayList = new ArrayList<>();


        card_send = findViewById(R.id.card_send);
        edit_chat = findViewById(R.id.edit_chat);


        profile = findViewById(R.id.image_Pfp_chat);
        receiverNName =findViewById(R.id.receiver_name);

        Picasso.get()
                .load(receiver_img)
                .placeholder(R.drawable.man)
                .error(R.drawable.man)
                .into(profile);

        receiverNName.setText(receiver_Name);

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference = database.getReference().child("user").child("sender_box").child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    message_base messages = dataSnapshot.getValue(message_base.class);
                    messagesArrayList.add(messages);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("DP").getValue().toString();
                receiverImg = receiver_img;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sender_UID = firebaseAuth.getUid();

        sender_box = sender_UID + receiver_UID;
        receiver_box = receiver_UID + sender_UID;

        card_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edit_chat.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(chat_box.this, "Need to write something", Toast.LENGTH_SHORT).show();
                }
                edit_chat.setText("");
                Date date = new Date();
                message_base messages = new message_base(message, sender_UID, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chat").
                        child("sender_box").
                        child("messages").
                        push().
                        setValue(messages).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            database.getReference().
                                    child("chat").
                                    child("receiver_box").
                                    child("messages").
                                    push().
                                    setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                            });

                        }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}