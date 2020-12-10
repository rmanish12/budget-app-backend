package com.budget.app.model.budgetItem;

import java.time.LocalDate;

public class BudgetItemRequestResponse {

    private int typeId;
    private int categoryId;
    private float amount;
    private String description;
    private LocalDate dateOfTransaction;

    public BudgetItemRequestResponse() {
    }

    public BudgetItemRequestResponse(int typeId, int categoryId, float amount, String description, LocalDate dateOfTransaction) {
        this.typeId = typeId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.dateOfTransaction = dateOfTransaction;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getCategoryId() {
        return categoryId;
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
                "typeId=" + typeId +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", dateOfTransaction=" + dateOfTransaction +
                '}';
    }
}
