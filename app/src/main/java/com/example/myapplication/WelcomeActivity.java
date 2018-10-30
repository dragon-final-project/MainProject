package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        logo = findViewById(R.id.imageView3);

        Animation myanim = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.welcome_anim);
        logo.startAnimation(myanim);

        final Intent aftersplash = new Intent(WelcomeActivity.this,MainActivity.class);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(aftersplash);
                    finish();
                }
            }
        };
        timer.start();
    }
}