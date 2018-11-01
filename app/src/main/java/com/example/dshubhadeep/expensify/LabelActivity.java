package com.example.dshubhadeep.expensify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

import static android.content.ContentValues.TAG;

public class LabelActivity extends AppCompatActivity {

    private TextView budgetTextView;

    private FloatingActionButton fab;
    private BottomAppBar bar;

    private RecyclerView label_list;
    private List<Label> labelList;
    private List<Expense> expenseList;
    private LabelListAdapter labelListAdapter;

    FirebaseFirestore db;

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

        // Fab listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddLabelActivity.class);
                startActivity(i);
            }
        });

    }

    private void initVars() {

        budgetTextView = findViewById(R.id.budget_text_view);
        fab = findViewById(R.id.fab);
        bar = findViewById(R.id.bar);

        sharedPreferences = getSharedPreferences("Budget", Context.MODE_PRIVATE);

        budgetTextView.setText("Loading...");


        labelList = new ArrayList<>();

        // Firestore DB init.
        db = FirebaseFirestore.getInstance();


        // RecyclerView
        label_list = findViewById(R.id.label_list);
        label_list.setHasFixedSize(true);
        label_list.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setBudgetView(int s) {

        String budget = sharedPreferences.getString("budget", "0");
        String budgetLeft = s + "/" + budget;
        int sumLength = Integer.toString(s).length();
        budgetTextView.setText(budgetLeft);

        Spannable wordToSpan = new SpannableString(budgetLeft);

        int color = ContextCompat.getColor(getApplicationContext(), R.color.lightWhite);

        wordToSpan.setSpan(new ForegroundColorSpan(color), 0, sumLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        budgetTextView.setText(wordToSpan);

    }

    @Override
    protected void onResume() {

        // Get expenses
        db.collection("expenses").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        int sum = 0;
                        expenseList = new ArrayList<>();

                        for(QueryDocumentSnapshot doc: task.getResult()) {
                            Expense expense = doc.toObject(Expense.class);
                            String amount = (String) doc.get("Amount");
                            sum += Integer.parseInt(amount);

                            Log.d("ExpFire", "onComplete Expense: " + expense.getLabel());
                            expenseList.add(expense);
                        }

                        setBudgetView(sum);
                    }
                });

        db.collection("labels")
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        labelList = new ArrayList<>();
                        Log.d(TAG, "onComplete: Labels" + expenseList.toString());
                        // Adapter
                        labelListAdapter = new LabelListAdapter(getApplicationContext(), labelList, expenseList);
                        label_list.setAdapter(labelListAdapter);


                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String id = doc.getId();

                            String label_name = (String) doc.get("name");
                            String label_budget = (String) doc.get("budget");

                            Label label = doc.toObject(Label.class);
                            labelList.add(label);

                            Log.d("Label Firestore", label_name + " : " + label_budget);
                            labelListAdapter.notifyDataSetChanged();
                        }

                    }
                });
        super.onResume();
    }
}
