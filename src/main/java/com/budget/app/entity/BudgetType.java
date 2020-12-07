package com.budget.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "types")
public class BudgetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 20)
    private String type;

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

    @Override
    public String toString() {
        return "BudgetTypes{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
