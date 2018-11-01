package com.example.dshubhadeep.expensify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

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

        Currency currency = Currency.getInstance(Locale.getDefault());
        String symbol = currency.getSymbol();

        final String expense_name = expenseList.get(position).getName();
        final String expense_amount = expenseList.get(position).getAmount();
        final String expense_date = expenseList.get(position).getDate();

        String prev_expense_date = null;
        if (position > 0) {
            prev_expense_date = expenseList.get(position - 1).getDate();
        }

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String[] date = expense_date.split("-");
        String month = months[Integer.parseInt(date[1]) - 1];

        if ((prev_expense_date != null &&
                !prev_expense_date.equalsIgnoreCase(expense_date)) || position == 0) {
            holder.expenseDateText.setText(new StringBuilder().append(date[0]).append("\n").append(month));
        }

        holder.expenseNameText.setText(expense_name);
        holder.expenseAmountText.setText(new StringBuilder()
                .append(symbol)
                .append(" ")
                .append(expense_amount));

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
        public TextView expenseDateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            expenseNameText = mView.findViewById(R.id.expense_name_list_text);
            expenseAmountText = mView.findViewById(R.id.expense_amount_list_text);
            expenseDateText = mView.findViewById(R.id.expense_date_list_text);

        }
    }
}
