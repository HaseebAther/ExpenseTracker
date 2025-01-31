package com.example.dime.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dime.Fragments.Home;
import com.example.dime.R;
import com.example.dime.Fragments.Statistics;
import com.example.dime.Fragments.Users;
import com.example.dime.Fragments.Wallet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);



        EdgeToEdge.enable(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);



        Home homesfrag = new Home();
        Users usersfrag = new Users();
        Wallet walletfrag = new Wallet();
        Statistics statsfrag = new Statistics();



        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);


        homesfrag.setArguments(bundle);
        usersfrag.setArguments(bundle);
        walletfrag.setArguments(bundle);
        statsfrag.setArguments(bundle);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.homefrag, homesfrag);
        transaction.commit();





        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;


                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = homesfrag;
                } else if (item.getItemId() == R.id.nav_users) {
                    selectedFragment = usersfrag;
                } else if (item.getItemId() == R.id.nav_wallet) {
                    selectedFragment = walletfrag;
                } else if (item.getItemId() == R.id.nav_stats) {
                    selectedFragment = statsfrag;
                }


                if (selectedFragment != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.homefrag, selectedFragment);
                    transaction.commit();
                }

                return true;
            }
        });


        ImageButton plusbtn = findViewById(R.id.plusbtn);
        plusbtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(Homepage.this, AddExpense.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
        });

        ImageButton aibot = findViewById(R.id.aibot);
        aibot.setOnClickListener(view -> {
            Intent intent1 = new Intent(Homepage.this, Chatbot.class);
            Toast.makeText(Homepage.this, "Chat bot ", Toast.LENGTH_SHORT).show();
            startActivity(intent1);
        });
    }


}
