package com.budget.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "types")
public class BudgetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 20)
    private String type;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    private List<Category> categories;

    @OneToMany(mappedBy = "budgetType", cascade = CascadeType.ALL)
    private List<BudgetItem> budgetItems;

    public BudgetType() {
    }

    public BudgetType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "BudgetType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", categories=" + categories +
                '}';
    }

    public void addCategory(Category category) {
        if(categories==null) {
            categories = new ArrayList<>();
        }

        categories.add(category);
    }

    public void addBudgetItem(BudgetItem budgetItem) {
        if(budgetItems==null) {
            budgetItems = new ArrayList<>();
        }

        budgetItems.add(budgetItem);
    }
}
