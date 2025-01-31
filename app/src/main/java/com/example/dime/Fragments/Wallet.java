package com.example.dime.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dime.Classes.Transaction;
import com.example.dime.R;
import com.example.dime.pages.AddExpense;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

public class Wallet extends Fragment {
    private ImageView btnScan, btnAdd;
    private TextView tvResult;
    private String userID;
    private DatabaseReference expensesref;

    public Wallet() {
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

        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        expensesref = FirebaseDatabase.getInstance().getReference("Expenses"); // Fixed typo in "expenses"

        tvResult = view.findViewById(R.id.tvResult);
        btnScan = view.findViewById(R.id.btnScan);
        btnAdd = view.findViewById(R.id.imageView3);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddExpense.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        btnScan.setOnClickListener(v -> initiateQRScanner());

        return view;
    }

    private void initiateQRScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this); // Use for fragments
        integrator.setPrompt("Scan a QR code");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    // Handle the result from the QR code scanner
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Display the scanned result
                tvResult.setText("Results Scanned");

                try {
                    // Parse the scanned QR code data (assuming it's in JSON format)
                    JSONObject jsonObject = new JSONObject(result.getContents());
                    String category = jsonObject.getString("category");
                    String name = jsonObject.getString("name");
                    String amount = jsonObject.getString("amount"); // Fixed 'string' to 'String'
                    String date = jsonObject.getString("date");

                    // Save the transaction to Firebase
                    saveTransactionToFirebase(category, name, amount, date, userID);
                } catch (Exception e) {
                    tvResult.setText("Invalid QR Code");
                    Toast.makeText(getContext(), "Error parsing QR code data", Toast.LENGTH_SHORT).show();
                }
            } else {
                tvResult.setText("Scan Cancelled");
            }
        }
    }

    private void saveTransactionToFirebase(String category, String name, String amount, String date, String userID) {
        // Create a unique key for the transaction
        String transactionId = expensesref.push().getKey();

        // Create a transaction object
        Transaction transaction = new Transaction(category, name, amount, date, userID);

        // Save the transaction to Firebase
        if (transactionId != null) {
            expensesref.child(transactionId).setValue(transaction)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Transaction added successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to add transaction", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
