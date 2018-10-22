package com.example.dshubhadeep.expensify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
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

import static android.content.ContentValues.TAG;

public class LabelActivity extends Activity {

    private TextView budgetTextView;

    private FloatingActionButton fab;
    private BottomAppBar bar;

    private RecyclerView label_list;
    private List<Label> labelList;
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

    }

    private void initVars() {

        budgetTextView = findViewById(R.id.budget_text_view);
        fab = findViewById(R.id.fab);
        bar = findViewById(R.id.bar);

        sharedPreferences = getSharedPreferences("Budget", Context.MODE_PRIVATE);


        labelList = new ArrayList<>();

        // Firestore DB init.
        db = FirebaseFirestore.getInstance();

        // Adapter
        labelListAdapter = new LabelListAdapter(getApplicationContext(), labelList);

        // RecyclerView
        label_list = findViewById(R.id.label_list);
        label_list.setHasFixedSize(true);
        label_list.setLayoutManager(new LinearLayoutManager(this));
        label_list.setAdapter(labelListAdapter);


        db.collection("labels").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        int sum = 0;

                        for(QueryDocumentSnapshot doc: task.getResult()) {
                            String id = doc.getId();

                            String label_name = (String) doc.get("name");
                            String label_budget = (String) doc.get("budget");

                            sum += Integer.parseInt(label_budget);

                            Label label = new Label();
                            label.setName(label_name);
                            label.setBudget(label_budget);
                            labelList.add(label);

                            Log.d("Label Firestore", label_name + " : " + label_budget);
                            labelListAdapter.notifyDataSetChanged();
                        }

                        setBudgetView(sum);
                    }
                });


    }

    private void setBudgetView(int s) {

        String budget = sharedPreferences.getString("budget", "0");
        String budgetLeft = s + "/" + budget;
        budgetTextView.setText(budgetLeft);

    }

}
