package com.example.dshubhadeep.expensify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

public class LabelActivity extends Activity {

    private TextView budgetTextView;

    private FloatingActionButton fab;
    private BottomAppBar bar;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);


        initVars();
        setListeners();



    }

    private void setListeners() {

        // Budget textView
        budgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

            }
        });
    }

    private void initVars() {

        budgetTextView = findViewById(R.id.budget_text_view);
        fab = findViewById(R.id.fab);
        bar = findViewById(R.id.bar);

        sharedPreferences = getSharedPreferences("Budget", Context.MODE_PRIVATE);

        String budget = sharedPreferences.getString("budget", "0");
        budgetTextView.setText(budget);

    }

}
