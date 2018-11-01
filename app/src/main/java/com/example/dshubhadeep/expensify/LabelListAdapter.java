package com.example.dshubhadeep.expensify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class LabelListAdapter extends RecyclerView.Adapter<LabelListAdapter.ViewHolder> {

    public List<Label> labelList;
    public List<Expense> expenseList;
    public Context context;
    public HashMap<String, Integer> labelWithExpenseList;

    public LabelListAdapter(Context context, List<Label> labelList, List<Expense> expenseList) {
        this.labelList = labelList;
        this.expenseList = expenseList;
        this.context = context;
        initList();
    }

    private void initList() {

        labelWithExpenseList = new HashMap<>();
        for (Expense e : expenseList) {
            String label = e.getLabel();
            int amount = Integer.parseInt(e.getAmount());
            Log.d("LABELLISTADAPTER", "initList: " + label);

            if (!labelWithExpenseList.containsKey(label)) {
                labelWithExpenseList.put(label, amount);
            } else {
                int newAmount = labelWithExpenseList.get(label) + amount;
                labelWithExpenseList.put(label, newAmount);
            }

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.label_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final View v = holder.mView;
        String budget_left;

        final String label_name = labelList.get(position).getName();
        String label_budget = labelList.get(position).getBudget();

        if (labelWithExpenseList.containsKey(label_name)) {
            int amount = labelWithExpenseList.get(label_name);
            budget_left = amount + "/" + label_budget;
        } else {
            budget_left = label_budget;
        }

        holder.labelNameText.setText(label_name);
        holder.labelBudgetText.setText(budget_left);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(v.getContext(), ExpenseActivity.class);
                i.putExtra("label", label_name);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (labelList == null)
            return 0;
        else
            return labelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView labelNameText;
        TextView labelBudgetText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            labelNameText = mView.findViewById(R.id.label_name_list_text);
            labelBudgetText = mView.findViewById(R.id.label_budget_list_text);

        }
    }
}
