package com.example.dime.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dime.Classes.Transaction;
import com.example.dime.Classes.TransactionAdapter;
import com.example.dime.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllTransactionActivity extends AppCompatActivity {
        private ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transaction);
            backbtn = findViewById(R.id.imageView5);
            backbtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(AllTransactionActivity.this, Homepage.class);
                                               intent.putExtra("userID", getIntent().getStringExtra("userID"));
                                               startActivity(intent);
                                               finish();
                                           }
            });

        String userID = getIntent().getStringExtra("userID");
        if (userID == null || userID.isEmpty()) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        setupTransactions(userID);
    }

    private void setupTransactions(String userID) {

        DatabaseReference userTransactionsRef = FirebaseDatabase.getInstance().getReference("Expenses");

        RecyclerView recyclerView = findViewById(R.id.Alltransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TransactionAdapter recyclerViewAdapter = new TransactionAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(expenseID -> {
            Intent intent = new Intent(AllTransactionActivity.this, TransactionDetails.class);
            intent.putExtra("expenseID", expenseID); // Pass the expenseID
            startActivity(intent);
        });


        Query userTransactionsQuery = userTransactionsRef.orderByChild("userID").equalTo(userID);

        userTransactionsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Transaction> transactionList = new ArrayList<>();


                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {

                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    if (transaction != null) {
                        transactionList.add(transaction);

                    }
                }


                if (!transactionList.isEmpty()) {
                    recyclerViewAdapter.setTransactionList(transactionList);
                    recyclerViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AllTransactionActivity.this, "No transactions found for this user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllTransactionActivity.this, "Error retrieving transactions: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
