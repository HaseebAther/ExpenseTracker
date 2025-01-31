package com.example.dime.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dime.R;
import com.example.dime.Classes.Transaction;
import com.example.dime.Classes.TransactionAdapter;
import com.example.dime.pages.AllTransactionActivity;
import com.example.dime.pages.TransactionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private TextView username, balance, expenses, income, seeall;


    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Retrieve userID from bundle
        Bundle bundle = getArguments();

        if (bundle == null || !bundle.containsKey("userID")) {
            Toast.makeText(getContext(), "User ID is missing", Toast.LENGTH_SHORT).show();
            return view;
        }
        String userID = bundle.getString("userID");

        // Initialize UI elements
        username = view.findViewById(R.id.Username);
        balance = view.findViewById(R.id.income);
        expenses = view.findViewById(R.id.expenses);
        income = view.findViewById(R.id.incomepp);
        seeall = view.findViewById(R.id.seeall);

        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getContext(), AllTransactionActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
            }
        });
        // Set Username
        setUsername(userID);

        // Set Transactions in RecyclerView
        setupTransactions(userID, view);

        // Calculate and display balance
        calbalance(userID);
        calincome(userID);
        calexpenses(userID);

        return view;
    }

    private void setUsername(String userID) {
        DatabaseReference userSpecificRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userID)
                .child("username");

        userSpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Dbname = snapshot.getValue(String.class);

                if (Dbname != null && !Dbname.isEmpty()) {
                    String[] words = Dbname.split(" ");
                    words[0] = words[0].substring(0, 1).toUpperCase() + words[0].substring(1);
                    username.setText(String.join(" ", words));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error retrieving username", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTransactions(String userID, View view) {
        DatabaseReference usertransactions = FirebaseDatabase.getInstance().getReference("Expenses");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TransactionAdapter recyclerViewAdapter = new TransactionAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(expenseID -> {
            Intent intent = new Intent(getContext(), TransactionDetails.class);
            intent.putExtra("expenseID", expenseID); // Pass the expenseID
            startActivity(intent);
        });

        usertransactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Transaction> transactionList = new ArrayList<>();
                int count = 0;

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    if (count >= 6) break;

                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);

                    if (transaction != null && userID.equals(transaction.getUserID())) {
                        transactionList.add(transaction);
                        count++;
                    }
                }

                if (!transactionList.isEmpty()) {
                    recyclerViewAdapter.setTransactionList(transactionList);
                } else {
                    Toast.makeText(getContext(), "No transactions found for this user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error retrieving transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void calbalance(String userID) {
        DatabaseReference userTransactions = FirebaseDatabase.getInstance().getReference("Expenses");

        userTransactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalBalance = 0.0;

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    if (transaction != null && userID.equals(transaction.getUserID())) {
                        double amount = Double.parseDouble(transaction.getAmount());
                        if ("Expense".equals(transaction.getCategory())) {
                            totalBalance -= amount; // Deduct for expense
                        } else {
                            totalBalance += amount; // Add for income
                        }
                    }
                }
                updateBalanceUI(totalBalance,balance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error retrieving transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calincome(String userID) {
        DatabaseReference userTransactions = FirebaseDatabase.getInstance().getReference("Expenses");

        userTransactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalIncome = 0.0;

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    if (transaction != null && userID.equals(transaction.getUserID())) {
                        double amount = Double.parseDouble(transaction.getAmount());
                        if ("Income".equals(transaction.getCategory())) {
                            totalIncome += amount; // Add amount to totalIncome
                        }
                    }
                }
                updateBalanceUI(totalIncome, income); // Pass income TextView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error retrieving transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void calexpenses(String userID) {
        DatabaseReference userTransactions = FirebaseDatabase.getInstance().getReference("Expenses");

        userTransactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalExpense = 0.0;

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    if (transaction != null && userID.equals(transaction.getUserID())) {
                        double amount = Double.parseDouble(transaction.getAmount());
                        if ("Expense".equals(transaction.getCategory())) {
                            totalExpense += amount; // Add amount to totalExpense
                        }
                    }
                }
                updateBalanceUI(totalExpense, expenses); // Pass expenses TextView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error retrieving transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void updateBalanceUI(double totalBalance, TextView balance) {
        String balanceText = String.format("$%.2f", totalBalance);
        balance.setText(balanceText);
    }
}
