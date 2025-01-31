    package com.example.dime.Classes;

    import android.content.Context;
    import android.graphics.Color;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.core.content.ContextCompat;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.dime.R;

    import java.util.List;

    public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

        private List<Transaction> transactionList;
        private OnItemClickListener listener;

        // Define an interface for the click listener
        public interface OnItemClickListener {
            void onItemClick(String expenseID);
        }

        // Method to set the click listener
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public TransactionAdapter(List<Transaction> transactionList) {
            this.transactionList = transactionList;
        }

        @NonNull
        @Override
        public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list, parent, false);
            return new TransactionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
            Transaction transaction = transactionList.get(position);

            // Bind text data
            holder.categoryTextView.setText(transaction.getName());
            holder.customCategoryTextView.setText(transaction.getCategory());
            holder.amountTextView.setText(transaction.getAmount());
            holder.dateTextView.setText(transaction.getDate());

            // Set color and prefix for income/expense
            if (transaction.getCategory().equals("Expense")) {
                holder.amountTextView.setText("-$" + transaction.getAmount());
                holder.amountTextView.setTextColor(Color.RED);
                holder.transactionIcon.setImageResource(R.drawable.expense);
            } else {
                holder.amountTextView.setText("+$" + transaction.getAmount());
                holder.amountTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                holder.transactionIcon.setImageResource(R.drawable.income);
            }

            // Set item click listener
            holder.itemView.setOnClickListener(v -> {
                if (listener != null && transaction != null) {
                    listener.onItemClick(transaction.getExpenseID()); // Pass the expense ID to the listener
                }
            });
        }

        @Override
        public int getItemCount() {
            return transactionList.size();
        }

        public void setTransactionList(List<Transaction> transactionList) {
            this.transactionList = transactionList;
            notifyDataSetChanged();
        }

        public static class TransactionViewHolder extends RecyclerView.ViewHolder {
            TextView categoryTextView, customCategoryTextView, amountTextView, dateTextView;
            ImageView transactionIcon;

            public TransactionViewHolder(View itemView) {
                super(itemView);
                categoryTextView = itemView.findViewById(R.id.service_name);
                customCategoryTextView = itemView.findViewById(R.id.transaction_date);
                amountTextView = itemView.findViewById(R.id.transaction_amount);
                dateTextView = itemView.findViewById(R.id.transaction_date);
                transactionIcon = itemView.findViewById(R.id.icon); // Reference the ImageView
            }
        }
    }
