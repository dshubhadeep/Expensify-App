package com.example.dshubhadeep.expensify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder> {

    public List<Expense> expenseList;
    public Context context;

    public ExpenseListAdapter(Context context, List<Expense> expenseList) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.expense_list_item, viewGroup, false);

        return new ExpenseListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final View v = holder.mView;

        String expense_name = expenseList.get(position).getName();
        String expense_amount = expenseList.get(position).getAmount();

        holder.expenseNameText.setText(expense_name);
        holder.expenseAmountText.setText(expense_amount);


    }

    @Override
    public int getItemCount() {
        if (expenseList == null)
            return 0;
        else
            return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public TextView expenseNameText;
        public TextView expenseAmountText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            expenseNameText = mView.findViewById(R.id.expense_name_list_text);
            expenseAmountText = mView.findViewById(R.id.expense_amount_list_text);

        }
    }
}
