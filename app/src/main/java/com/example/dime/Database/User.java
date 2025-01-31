package com.example.dime.Database;

public class User {
    public String userId;
    public String username;
    public String email;
    public String password;

    // Required empty constructor
    public User() {}

    public User(String userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
