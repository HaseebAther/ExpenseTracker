package com.example.dime.pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.dime.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class AddExpense extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText editTextName, editTextAmount, editTextDate;
    private Button saveButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextName = findViewById(R.id.editTextName);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        saveButton = findViewById(R.id.savebtn);
        String userID = getIntent().getStringExtra("userID");
        
        String[] options = {"Select", "Expense", "Income"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Expenses");

        saveButton.setOnClickListener(v -> saveDataToFirebase(userID));
    }

    private void saveDataToFirebase(String userID) {
        String category = spinnerCategory.getSelectedItem().toString();
        String Name = editTextName.getText().toString().trim();
        String amount = editTextAmount.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();


        if (category.equals("Select")) {
            Toast.makeText(this, "Please select a valid category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Name)) {
            editTextName.setError("Category name is required");
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            editTextAmount.setError("Amount is required");
            return;
        }
        if (TextUtils.isEmpty(date) || !isValidDate(date) ) {
            editTextDate.setError("Correct Date is required");
            return;
        }

        String entryId = databaseReference.push().getKey();
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("category", category);
        dataMap.put("Name", Name);
        dataMap.put("amount", amount);
        dataMap.put("date", date);
        dataMap.put("userID", userID);

        if (entryId != null) {
            databaseReference.child(entryId).setValue(dataMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddExpense.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                            editTextName.setText("");
                            editTextAmount.setText("");
                            editTextDate.setText("");
                        } else {
                            Toast.makeText(AddExpense.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public static boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);

        try {
            // Try parsing the date
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;         }
    }
    public void back(View view) {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
    }
}