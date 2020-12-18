package com.budget.app.model.budgetItem;

import java.time.LocalDate;

public class BudgetItemResponse {

    private int id;
    private float amount;
    private String description;
    private LocalDate dateOfTransaction;
    private String category;

    public BudgetItemResponse(int id, float amount, String description, LocalDate dateOfTransaction, String category) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.dateOfTransaction = dateOfTransaction;
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
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

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "BudgetItemResponse{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", dateOfTransaction=" + dateOfTransaction +
                ", category='" + category + '\'' +
                '}';
    }
}
