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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dime.Classes.Transaction;
import com.example.dime.Classes.TransactionAdapter;
import com.example.dime.R;
import com.example.dime.pages.TransactionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Statistics extends Fragment {

private   String userID;


    public Statistics() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("userID")) {
            userID = bundle.getString("userID");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        setupIncome(userID, view);
        setupExpense(userID, view);

        return view;
    }
    private void setupIncome(String userID, View view) {
        DatabaseReference usertransactions = FirebaseDatabase.getInstance().getReference("Expenses");
        RecyclerView recyclerView = view.findViewById(R.id.income);

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
                List<Transaction> IncomeList = new ArrayList<>();


                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);

                    if (transaction != null && userID.equals(transaction.getUserID())) {

                        if ("Income".equals(transaction.getCategory())) {

                            IncomeList.add(transaction);
                        }
                    }
                }



                Collections.sort(IncomeList, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        return Double.compare(Double.parseDouble(t2.getAmount()), Double.parseDouble(t1.getAmount()));
                    }
                });


                if (IncomeList.size() > 3) {
                    IncomeList = IncomeList.subList(0, 3);
                }

                if (!IncomeList.isEmpty()) {
                    recyclerViewAdapter.setTransactionList(IncomeList);
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


    private void setupExpense(String userID, View view) {
        DatabaseReference usertransactions = FirebaseDatabase.getInstance().getReference("Expenses");
        RecyclerView recyclerView = view.findViewById(R.id.expensecard);

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
                List<Transaction> ExpenseList = new ArrayList<>();


                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);

                    if (transaction != null && userID.equals(transaction.getUserID())) {

                        if ("Expense".equals(transaction.getCategory())) {

                            ExpenseList.add(transaction);
                        }
                    }
                }



                Collections.sort(ExpenseList, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        return Double.compare(Double.parseDouble(t2.getAmount()), Double.parseDouble(t1.getAmount()));
                    }
                });


                if (ExpenseList.size() > 3) {
                    ExpenseList = ExpenseList.subList(0, 3);
                }

                if (!ExpenseList.isEmpty()) {
                    recyclerViewAdapter.setTransactionList(ExpenseList);
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


}