package com.budget.app.model.category;

import com.budget.app.entity.Category;

import java.util.List;

public class GetCategoriesResponse {

    private List<Category> income;
    private List<Category> expense;

    public GetCategoriesResponse() {
    }

    public GetCategoriesResponse(List<Category> income, List<Category> expense) {
        this.income = income;
        this.expense = expense;
    }

    public List<Category> getIncome() {
        return income;
    }

    public List<Category> getExpense() {
        return expense;
    }

}
