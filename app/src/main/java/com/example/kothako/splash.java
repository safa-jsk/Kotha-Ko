package com.example.kothako;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash extends AppCompatActivity {
    ImageView logo;
    TextView name, from, triad;
    Animation top_anim, bot_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
//            getSupportActionBar().hide();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logo = findViewById(R.id.image_logo);
        name = findViewById(R.id.text_app_name);
        from = findViewById(R.id.text_from);
        triad = findViewById(R.id.text_triad);

        top_anim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bot_anim = AnimationUtils.loadAnimation(this, R.anim.bot_animation);

        logo.setAnimation(top_anim);
        name.setAnimation(bot_anim);
        from.setAnimation(bot_anim);
        triad.setAnimation(bot_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);

    }
}