package com.example.dime.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dime.R;


public class Onboarding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);


    }

    public void openLogin(View view) {
        Intent intent = new Intent(Onboarding.this, Login.class);
        startActivity(intent);
    }

    public void openSignup(View view) {
        Intent intent = new Intent(Onboarding.this, SignUp.class);
        startActivity(intent);
    }
}