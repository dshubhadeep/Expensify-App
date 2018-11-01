package com.example.dshubhadeep.expensify;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TextInputEditText searchField, fromDate, toDate;

    Chip nameChip, labelChip, dateChip;

    MaterialButton searchBtn;

    TextView noMatchesText;

    String searchQuery;

    FirebaseFirestore db;

    LinearLayout dateRangeLayout;

    Calendar calendar;

    DatePickerDialog.OnDateSetListener fromDateDialog, toDateDialog;

    private List<Expense> expenseList;
    private ExpenseListAdapter expenseListAdapter;

    RecyclerView expense_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initVars();
        setListeners();
    }

    private void initVars() {

        searchField = findViewById(R.id.search_field);
        fromDate = findViewById(R.id.from_date_edit_text);
        toDate = findViewById(R.id.to_date_edit_text);

        searchBtn = findViewById(R.id.search_button);

        nameChip = findViewById(R.id.name_chip);
        dateChip = findViewById(R.id.date_chip);
        labelChip = findViewById(R.id.label_chip);

        dateRangeLayout = findViewById(R.id.date_range_layout);

        noMatchesText = findViewById(R.id.no_matches_text);

        db = FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();

        expense_list = findViewById(R.id.expense_list);
        expense_list.setHasFixedSize(true);
        expense_list.setLayoutManager(new LinearLayoutManager(this));

        toDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                updateDate(year, month + 1, day, toDate);
            }
        };

        fromDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                updateDate(year, month + 1, day, fromDate);
            }
        };
    }

    private void setListeners() {

        // Search button click listener
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchQuery = searchField.getText().toString().toLowerCase();

                if (searchQuery.length() > 0 || dateChip.isChecked()) {
                    db.collection("expenses")
                            .orderBy("pDate")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {

                                        expenseList = new ArrayList<>();
                                        expenseListAdapter = new ExpenseListAdapter(
                                                getApplicationContext(),
                                                expenseList);
                                        expense_list.setAdapter(expenseListAdapter);

                                        for (QueryDocumentSnapshot doc : task.getResult()) {

                                            String id = doc.getId();
                                            Log.d("SearchFire", "onComplete: " + id);
                                            Expense expense = doc.toObject(Expense.class);
                                            expense.setId(id);

                                            if (nameChip.isChecked()) {
                                                String[] query = {searchQuery};
                                                getResults(expense, query, "name");
                                            } else if (labelChip.isChecked()) {
                                                String[] query = {searchQuery};
                                                getResults(expense, query, "label");
                                            } else if (dateChip.isChecked()) {
                                                String from_date = fromDate.getText().toString();
                                                String to_date = toDate.getText().toString();

                                                if (from_date.length() > 0 && to_date.length() > 0) {
                                                    String []dates = {from_date, to_date};
                                                    getResults(expense, dates, "date");
                                                } else {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Fill date fields properly",
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                }

                                            }

                                            expenseListAdapter.notifyDataSetChanged();
                                        }

                                        if (expenseList.size() == 0) {
                                            noMatchesText.setVisibility(View.VISIBLE);
                                        } else {
                                            noMatchesText.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Query Failed",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }


                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Search field must not be empty.",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        // Date chip
        dateChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    dateRangeLayout.setVisibility(View.VISIBLE);
                else
                    dateRangeLayout.setVisibility(View.GONE);
            }
        });

        // From date editText
        fromDate.setKeyListener(null);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SearchActivity.this,
                        fromDateDialog,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        // To date editText
        toDate.setKeyListener(null);
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SearchActivity.this,
                        toDateDialog,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }

    private void getResults(Expense expense, String[] searchQuery, String action) {

        switch (action) {
            case "name":
                if (expense.getName().toLowerCase().contains(searchQuery[0])) {
                    expenseList.add(expense);
                }
                break;
            case "label":
                if (expense.getLabel().toLowerCase().contains(searchQuery[0])) {
                    expenseList.add(expense);
                }
                break;
            case "date":
                String expenseDate = expense.getDate();
                expenseDate = expenseDate.replace("-", "/");
                String parsedExpenseDate = parseDate(expenseDate);
                String parsedFromDate = parseDate(searchQuery[0]);
                String parsedToDate = parseDate(searchQuery[1]);

                if (parsedExpenseDate.compareTo(parsedFromDate) >= 0 && parsedExpenseDate.compareTo(parsedToDate) <= 0) {
                    expenseList.add(expense);
                }

                break;
        }
    }

    private void updateDate(int year, int month, int day, TextInputEditText tx) {
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

        date = new StringBuilder().append(formattedDay).append("/")
                .append(formattedMonth).append("/").append(year);

        tx.setText(date);
    }

    /**
     * Parses date for easy querying
     *
     * @return date parsed in format yyyymmdd
     * @params Date from picker
     */
    private String parseDate(String date) {

        String[] fields = date.split("/");
        return fields[2] + fields[1] + fields[0];
    }


}
