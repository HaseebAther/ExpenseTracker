package com.example.dime.pages;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dime.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void openSignup(View view) {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }

    public boolean openhome(View view) {
        EditText loginemail = findViewById(R.id.editTextTextEmailAddress);
        EditText loginpass = findViewById(R.id.editTextTextPassword);

        String userEmail = loginemail.getText().toString();
        String userPass = loginpass.getText().toString();


        if (userEmail.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(userEmail)) {
            Toast.makeText(this, "Invalid email format. Please include '@'.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isStrongPassword(userPass)) {
            Toast.makeText(this, "Password is too weak.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            loginUser(userEmail, userPass);
            return true;
        }

    }

    private boolean loginUser(String userEmail, String userPass) {
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean loginSuccess = false;
                String loggedInUserId = null;


                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String dbEmail = userSnapshot.child("email").getValue(String.class);
                    String dbPassword = userSnapshot.child("password").getValue(String.class);

                    if (userEmail.equals(dbEmail) && userPass.equals(dbPassword)) {
                        loginSuccess = true;
                        loggedInUserId = userSnapshot.getKey();
                        break;
                    }
                }

                if (loginSuccess) {

                    Toast.makeText(Login.this, "Login successful! ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Homepage.class);
                    intent.putExtra("userID",loggedInUserId);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(Login.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }

    private boolean isValidEmail(String email) {

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isStrongPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
