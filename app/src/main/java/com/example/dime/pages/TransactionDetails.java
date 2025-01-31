package com.example.dime.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.dime.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TransactionDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_details);
        ImageView backbtn = findViewById(R.id.up_arrow_icon);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionDetails.this, Homepage.class);
                intent.putExtra("userID", getIntent().getStringExtra("userID"));
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        intent.getStringExtra("userID");

        TextView status_text = findViewById(R.id.status_text);
        TextView from_text = findViewById(R.id.from_text);
        TextView date_text = findViewById(R.id.date_text);
        TextView earnings_text = findViewById(R.id.earnings_text);
        TextView total_text = findViewById(R.id.total_text);


        DatabaseReference expensesref = FirebaseDatabase.getInstance().getReference("expences");



    }
}
