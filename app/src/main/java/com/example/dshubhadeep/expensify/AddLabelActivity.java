package com.example.dshubhadeep.expensify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddLabelActivity extends AppCompatActivity {

    private static final String TAG = "LABEL";
    private TextInputEditText labelNameEditText;
    private TextInputEditText labelBudgetEditText;

    private MaterialButton addLabelButton;

    private FirebaseFirestore db;
    private CollectionReference labelRef;

    private FirebaseAuth mAuth;

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
                final String labelName = labelNameEditText.getText().toString();
                String labelBudget = labelBudgetEditText.getText().toString();

                if (labelName.length() > 0 && labelBudget.length() > 0) {

                    HashMap<String, Object> labelData = new HashMap<>();
                    labelData.put("name", labelName);
                    labelData.put("budget", labelBudget);

                    labelRef.document(labelName).set(labelData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("FireStoreLabel", "onComplete: Label - " + labelName);
                                finish();
                            }
                        }
                    });

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

        // Firebase stuff
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getCurrentUser().getUid();

        labelRef = db.collection("users").document(uid).collection("labels");

    }


}
