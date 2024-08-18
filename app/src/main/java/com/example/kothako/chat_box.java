package com.example.kothako;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_box extends AppCompatActivity {
    String receiver_img, receiver_UID, receiver_Name, sender_UID;
    CircleImageView profile;
    TextView receiverNName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbox);

        receiver_Name =getIntent().getStringExtra("name");
        receiver_img =getIntent().getStringExtra("receiverImg");
        receiver_UID =getIntent().getStringExtra("uID");


        profile = findViewById(R.id.image_Pfp_chat);
        receiverNName =findViewById(R.id.receiver_name);

        Picasso.get()
                .load(receiver_img)
                .placeholder(R.drawable.man)
                .error(R.drawable.man)
                .into(profile);

        receiverNName.setText(receiver_Name);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}