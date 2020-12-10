package com.budget.app.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "budget")
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private float amount;

    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BudgetType budgetType;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "date_of_transaction", nullable = false)
    private LocalDate dateOfTransaction;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public BudgetItem() {
    }

    public BudgetItem(float amount, String description, BudgetType budgetType, Category category, LocalDate dateOfTransaction) {
        this.amount = amount;
        this.description = description;
        this.budgetType = budgetType;
        this.category = category;
        this.dateOfTransaction = dateOfTransaction;
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

    public BudgetType getBudgetType() {
        return budgetType;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    @Override
    public String toString() {
        return "BudgetItem{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", budgetType=" + budgetType +
                ", category=" + category +
                ", dateOfTransaction=" + dateOfTransaction +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
