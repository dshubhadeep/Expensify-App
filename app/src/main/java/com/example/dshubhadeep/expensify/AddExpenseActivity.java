package com.example.dshubhadeep.expensify;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddExpenseActivity extends AppCompatActivity {

    private TextInputEditText expenseName, expenseLabel, expenseAmount, expenseDate;

    private String label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        initVars();
    }

    private void initVars() {
        expenseName = findViewById(R.id.expense_name_edit_text);
        expenseAmount = findViewById(R.id.expense_amount_edit_text);
        expenseLabel = findViewById(R.id.expense_label_edit_text);
        expenseDate = findViewById(R.id.expense_date_edit_text);

        Intent i = getIntent();
        String label = i.getStringExtra("label");
        expenseLabel.setText(label);
    }
}
