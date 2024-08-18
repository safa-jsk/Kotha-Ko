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

public class chatbox extends AppCompatActivity {
    String reciverimg,reciverUID,reciverName,senderUID;
    CircleImageView profile;
    TextView reciverNName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbox);

        reciverName=getIntent().getStringExtra("nameeeee");
        reciverimg=getIntent().getStringExtra("reciverImg");
        reciverUID=getIntent().getStringExtra("uID");


        profile = findViewById(R.id.image_Pfp_chat);
        reciverNName =findViewById(R.id.recivername);

        Picasso.get()
                .load(reciverimg)
                .placeholder(R.drawable.man)
                .error(R.drawable.man)
                .into(profile );

        reciverNName.setText(""+reciverName);








        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}