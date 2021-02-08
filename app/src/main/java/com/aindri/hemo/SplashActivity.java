package com.aindri.hemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    ImageView beat;
    Animation top_anim;

    public static final int timer=5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        beat=findViewById(R.id.beat);

        top_anim= AnimationUtils.loadAnimation(this,R.anim.top_anim);

        beat.setAnimation(top_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
  Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
  startActivity(intent);
  finish();
            }
        },timer);

    }
}