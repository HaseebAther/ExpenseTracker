package com.example.dime.pages;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dime.Database.User;
import com.example.dime.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public boolean openLogin(View view) {
        EditText name = findViewById(R.id.Name);
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText pass = findViewById(R.id.editTextTextPassword);

        String username = name.getText().toString();
        String useremail = email.getText().toString();
        String userpass = pass.getText().toString();

        if (username.isEmpty() || useremail.isEmpty() || userpass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(useremail)) {
            Toast.makeText(this, "Invalid email format. Please include '@'.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isStrongPassword(userpass)) {
            Toast.makeText(this, "Password is too weak.", Toast.LENGTH_LONG).show();
            return false;
        }


        Intent intent = new Intent(SignUp.this, Login.class);
        storeUserData(username, useremail, userpass);
        startActivity(intent);
        return true;
    }
    public void openloginpage(View view)
    {
        Intent intent=new Intent(SignUp.this,Login.class);
        startActivity(intent);
    }

    private void storeUserData(String username, String useremail, String userpass) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");
        String userId = userRef.push().getKey();
        HashMap<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("email", useremail);
        userData.put("password", userpass);


        userRef.child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to register user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

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