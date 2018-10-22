package com.example.dshubhadeep.expensify;

public class Label {

    private String name, budget;

    public Label(){};

    public Label(String name, String budget) {
        this.name = name;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public String getBudget() {
        return budget;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
