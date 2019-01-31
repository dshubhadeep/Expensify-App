package com.example.dshubhadeep.expensify;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class EditExpenseActivity extends AppCompatActivity {

    private TextInputEditText expenseNameEditText,
            expenseLabelEditText,
            expenseDateEditText,
            expenseAmountEditText;

    private Button editExpenseButton;

    private FirebaseFirestore db;

    private String expense_id;

    private Calendar calendar;

    private DatePickerDialog.OnDateSetListener dateDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        initVars();
        setListeners();
    }

    private void initVars() {

        // Get intent data
        Intent i  = getIntent();
        HashMap<String, String> expenseHashMap = (HashMap<String, String>) i.getSerializableExtra("expense_map");

        // Store ID for reference
        expense_id = expenseHashMap.get("expense_id");

        // Set text fields
        expenseNameEditText = findViewById(R.id.expense_name_edit_text);
        expenseNameEditText.setText(expenseHashMap.get("expense_name"));

        expenseAmountEditText = findViewById(R.id.expense_amount_edit_text);
        expenseAmountEditText.setText(expenseHashMap.get("expense_amount"));

        expenseDateEditText = findViewById(R.id.expense_date_edit_text);
        expenseDateEditText.setText(expenseHashMap.get("expense_date"));

        expenseLabelEditText = findViewById(R.id.expense_label_edit_text);
        expenseLabelEditText.setText(expenseHashMap.get("expense_label"));

        // Map button
        editExpenseButton = findViewById(R.id.edit_expense_button);

        db = FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();

        dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                updateDate(year, month + 1, day);
            }
        };

    }

    private void setListeners() {

        // Edit expense button
        editExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String parsedDate, name = expenseNameEditText.getText().toString(),
                        amount = expenseAmountEditText.getText().toString(),
                        date = expenseDateEditText.getText().toString();

                date = date.replace("/", "-");
                parsedDate = parseDate(date);


                Log.d("EditExpenseDebug", "onClick: Name - " + name + " " + amount + " Id : " + expense_id);


                db.collection("expenses").document(expense_id)
                        .update(
                                "Name", name,
                                "Amount", amount,
                                "Date", date,
                                "pDate", parsedDate
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Expense details updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Something went horribly wrong.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Date text
        expenseDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditExpenseActivity.this,dateDialog,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * Updates date in date edit text
     *
     * @param year  Selected year
     * @param month Selected month
     * @param day   Selected day
     *
     */
    private void updateDate(int year, int month, int day) {
        StringBuilder date;
        String formattedMonth, formattedDay;

        if (month < 10) {
            formattedMonth = "0" + Integer.toString(month);
        } else {
            formattedMonth = Integer.toString(month);
        }

        if (day < 10) {
            formattedDay = "0" + Integer.toString(day);
        } else {
            formattedDay = Integer.toString(day);
        }

        date = new StringBuilder().append(formattedDay).append("/").append(formattedMonth).append("/").append(year);

        expenseDateEditText.setText(date);
    }

    /**
     * Parses date for easy querying
     *
     * @param date from picker
     * @return date parsed in format yyyymmdd
     */
    private String parseDate(String date) {

        String[] fields = date.split("-");
        return fields[2] + fields[1] + fields[0];
    }
}
