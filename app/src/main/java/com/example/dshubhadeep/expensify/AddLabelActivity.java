package com.example.dshubhadeep.expensify;

import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddLabelActivity extends AppCompatActivity {

    private static final String TAG = "LABEL";
    private TextInputEditText labelNameEditText;
    private TextInputEditText labelBudgetEditText;

    private MaterialButton addLabelButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label);

        initVars();
        setListeners();

    }

    private void setListeners() {

        // Add label button
        addLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String labelName = labelNameEditText.getText().toString();
                String labelBudget = labelBudgetEditText.getText().toString();

                if (labelName.length() > 0 && labelBudget.length() > 0) {

                    HashMap<String, Object> labelData = new HashMap<>();
                    labelData.put("name", labelName);
                    labelData.put("budget", labelBudget);

                    db.collection("labels")
                            .add(labelData)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    DocumentReference doc = task.getResult();

                                    Log.d(TAG, "onComplete: " + doc.getId());

                                }
                            });

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void initVars() {

        labelNameEditText = findViewById(R.id.label_name_edit_text);
        labelBudgetEditText = findViewById(R.id.label_budget_edit_text);

        addLabelButton = findViewById(R.id.add_label_button);

        db = FirebaseFirestore.getInstance();

    }


}
