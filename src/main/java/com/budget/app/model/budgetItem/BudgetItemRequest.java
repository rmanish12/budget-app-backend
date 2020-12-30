package com.budget.app.model.budgetItem;

import java.time.LocalDate;

public class BudgetItemRequest {

    private int type;
    private int category;
    private float amount;
    private String description;
    private LocalDate dateOfTransaction;

    public BudgetItemRequest(int type, int category, float amount, String description, LocalDate dateOfTransaction) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.dateOfTransaction = dateOfTransaction;
    }

    public int getType() {
        return type;
    }

    public int getCategory() {
        return category;
    }

    public float getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction;
    }

    @Override
    public String toString() {
        return "BudgetItemRequest{" +
                "type=" + type +
                ", category=" + category +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", dateOfTransaction=" + dateOfTransaction +
                '}';
    }
}
