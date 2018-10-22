package com.example.dshubhadeep.expensify;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LabelListAdapter extends RecyclerView.Adapter<LabelListAdapter.ViewHolder>{

    public List<Label> labelList;
    public Context context;

    public LabelListAdapter(Context context, List<Label> labelList) {
        this.labelList = labelList;
        this.context = context;
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

        String label_name = labelList.get(position).getName();
        String label_budget = labelList.get(position).getBudget();

        holder.labelNameText.setText(label_name);
        holder.labelBudgetText.setText(label_budget);

    }

    @Override
    public int getItemCount() {
        if (labelList == null)
            return 0;
        else
            return labelList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        View mView;

        public TextView labelNameText;
        public TextView labelBudgetText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            labelNameText = mView.findViewById(R.id.label_name_list_text);
            labelBudgetText = mView.findViewById(R.id.label_budget_list_text);

        }
    }
}
