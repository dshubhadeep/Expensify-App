package com.example.dshubhadeep.expensify;

public class Expense {

    private String Name, Amount, Label, Date, id, labelColor, budgetLimit;

    public Expense() {

    }

    public Expense(String name, String amount, String date, String label, String budgetLimit) {
        this.Name = name;
        this.Amount = amount;
        this.Label = label;
        this.Date = date;
        this.budgetLimit = budgetLimit;
    }

    public String getName() {
        return Name;
    }

    public String getAmount() {
        return Amount;
    }

    public String getLabel() {
        return Label;
    }

    public String getDate() {
        return Date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id_val) {
        id = id_val;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public void setBudgetLimit(String budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public String getBudgetLimit() {
        return budgetLimit;
    }
}

