package com.example.dshubhadeep.expensify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {

    private static final String TAG = "Firestore";
    private TextView expenseHeader, noExpenseText;
    private String label;

    private RecyclerView expense_list;
    private List<Expense> expenseList;
    private ExpenseListAdapter expenseListAdapter;

    private FloatingActionButton fab;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        initVars();
        setListeners();

    }

    private void setListeners() {

        // FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExpenseActivity.this,
                        AddExpenseActivity.class);
                i.putExtra("label", label);
                startActivity(i);
            }
        });


    }

    private void initVars() {

        expenseHeader = findViewById(R.id.expense_header);
        noExpenseText = findViewById(R.id.no_expenses_view);
        Intent i = getIntent();
        label = i.getStringExtra("label");
        expenseHeader.setText(label);

        fab = findViewById(R.id.fab);

        db = FirebaseFirestore.getInstance();

        expenseList = new ArrayList<>();

        // RecyclerView
        expense_list = findViewById(R.id.expense_list);
        expense_list.setHasFixedSize(true);
        expense_list.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {

        db.collection("expenses")
                .whereEqualTo("Label", label)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        expenseList = new ArrayList<>();
                        expenseListAdapter = new ExpenseListAdapter(getApplicationContext(), expenseList);
                        expense_list.setAdapter(expenseListAdapter);

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Expense expense = doc.toObject(Expense.class);

                            expenseList.add(expense);

                            expenseListAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onComplete: " + doc.getId());
                        }

                        if  (expenseList.size() > 0 ) {
                            noExpenseText.setVisibility(View.GONE);
                        } else {
                            noExpenseText.setVisibility(View.VISIBLE);
                        }

                    }
                });


        super.onResume();
    }
}
