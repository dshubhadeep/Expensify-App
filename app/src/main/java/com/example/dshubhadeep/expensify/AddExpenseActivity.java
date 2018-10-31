package com.example.dshubhadeep.expensify;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class AddExpenseActivity extends AppCompatActivity {

    private TextInputEditText expenseName, expenseLabel, expenseAmount, expenseDate;

    private String label;

    private MaterialButton addExpenseBtn;

    Calendar calendar;
    int day, month, year;

    DatePickerDialog.OnDateSetListener dateDialog;

    FirebaseFirestore db;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        initVars();
        setListeners();

        setTodayDate();
    }

    private void setListeners() {

        //  Date listener
        expenseDate.setKeyListener(null);
        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddExpenseActivity.this,
                        dateDialog,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        // Add expense button
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = expenseName.getText().toString();
                String amount = expenseAmount.getText().toString();
                String date = expenseDate.getText().toString();

                if (validateFields(name, amount)) {

                    HashMap<String, Object> expense =
                            generateMap(name, amount, date, label);


                    Toast.makeText(context, "Added" , Toast.LENGTH_SHORT).show();

                    db.collection("expenses")
                            .add(expense)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(),
                                            "Added", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });

                    finish();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please fill all fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private HashMap<String, Object> generateMap(String name, String amount, String date, String label) {

        HashMap<String, Object> expense = new HashMap<>();

        date = date.replace("/", "-");
        String parsedDate = parseDate(date);
        int labelColor = generateColor(label);
        String color = Integer.toString(labelColor);

        expense.put("Name", name);
        expense.put("Date", date);
        expense.put("Amount", amount);
        expense.put("Label", label);
        expense.put("pDate", parsedDate);
        expense.put("labelColor", color);

        return expense;
    }

    /**
     * Parses date for easy querying
     *
     * @param date Date obtained from picker
     * @return Date parsed in format (yyyymmdd)
     */
    private String parseDate(String date) {

        String[] fields = date.split("-");
        return fields[2] + fields[1] + fields[0];
    }

    private void initVars() {
        expenseName = findViewById(R.id.expense_name_edit_text);
        expenseAmount = findViewById(R.id.expense_amount_edit_text);
        expenseLabel = findViewById(R.id.expense_label_edit_text);
        expenseDate = findViewById(R.id.expense_date_edit_text);

        addExpenseBtn = findViewById(R.id.add_expense_button);

        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        label = i.getStringExtra("label");
        expenseLabel.setText(label);

        dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                updateDate(year, month + 1, day);
            }
        };

        context = getApplicationContext();
    }

    /**
     * Updates date in date edit text
     *
     * @param year  Selected year
     * @param month Selected month
     * @param day   Selected day
     */
    private void updateDate(int year, int month, int day) {
        StringBuilder date;
        String formattedMonth, formattedDay;

        formattedMonth = month < 10
                ? "0" + Integer.toString(month)
                : Integer.toString(month);

        formattedDay = day < 10
                ? "0" + Integer.toString(day)
                : Integer.toString(day);

        date = new StringBuilder().append(formattedDay).append("/")
                .append(formattedMonth).append("/").append(year);

        expenseDate.setText(date);

    }

    /**
     * Set today's date for date edit text on create
     */
    private void setTodayDate() {
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        updateDate(year, month + 1, day);
    }


    /**
     * @param name   Expense name
     * @param amount Expense amount
     * @return value
     */
    private boolean validateFields(String name, String amount) {
        return name.length() > 0 && amount.length() > 0;
    }

    /**
     * Generates a color if label is not present in
     * shared preferences else returns
     * color stored for the label
     *
     * @param label Expense label
     * @return Label color
     */
    private int generateColor(String label) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        int color = sharedPreferences.getInt(label.toLowerCase(), 0);

        if (color == 0) { // Color not present
            // Create sharedPref
            Log.d("Label Color ", "generateColor: " + label + " not present");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int red = (int) Math.floor(Math.random() * 255);
            int green = (int) Math.floor(Math.random() * 255);
            int blue = (int) Math.floor(Math.random() * 255);
            color = Color.rgb(red, green, blue);
            editor.putInt(label.toLowerCase(), color);
            editor.apply();
        }

        return color;
    }
}
