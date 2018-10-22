package com.example.dshubhadeep.expensify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BudgetActivity extends AppCompatActivity {

    private EditText budgetEditText;
    private MaterialButton setBudgetBtn;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        initVars();
        setListeners();
    }

    private void initVars() {
        budgetEditText = findViewById(R.id.budget_edit_text);
        setBudgetBtn = findViewById(R.id.set_budget_button);

        sharedPreferences = getSharedPreferences("Budget", Context.MODE_PRIVATE);
    }

    private void setListeners() {

        // Set budget button
        setBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budget = budgetEditText.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("budget", budget);
                editor.apply();

                Intent i = new Intent(BudgetActivity.this, LabelActivity.class);
                startActivity(i);

//                finish();
            }
        });

    }
}
