package com.example.dime.pages;

import android.os.Bundle;
import android.content.Intent;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dime.R;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, Onboarding.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
