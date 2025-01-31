package com.example.dime.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dime.R;
import com.example.dime.pages.Login;
import com.example.dime.pages.Mapactivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Users extends Fragment {

    private TextView username , email;
    private Button logout, mapbtn;
            private   String userID;
    public Users() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_users, container, false);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("userID")) {
            userID  = bundle.getString("userID");

        }

                mapbtn = view.findViewById(R.id.map1);
                 username = view.findViewById(R.id.userName);
            email = view.findViewById(R.id.userEmail);
               logout = view.findViewById(R.id.logoutButton);

        setvalues(userID);
        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Mapactivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            });


            return view;
    }

    private void setvalues(String userID) {
        DatabaseReference userSpecificRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userID);

        userSpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Dbname = snapshot.child("username").getValue(String.class);
                String Dbemail = snapshot.child("email").getValue(String.class);

                if (Dbname != null && !Dbname.isEmpty()) {
                    String[] words = Dbname.split(" ");
                    for (int i = 0; i < words.length; i++) {
                        words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
                    }
                    username.setText(String.join(" ", words));
                } else {
                    username.setText("Unknown User");
                }

                if (Dbemail != null && !Dbemail.isEmpty()) {
                    email.setText(Dbemail);
                } else {
                    email.setText("No email provided");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error retrieving user information", Toast.LENGTH_SHORT).show();
            }
        });
    }

}