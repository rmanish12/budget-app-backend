package com.budget.app.model.budgetItem;

import java.util.List;

public class AddBudgetItemsRequest {

    private int userId;
    private List<BudgetItemRequestResponse> budgetItems;

    public AddBudgetItemsRequest(int userId, List<BudgetItemRequestResponse> budgetItems) {
        this.userId = userId;
        this.budgetItems = budgetItems;
    }

    public int getUserId() {
        return userId;
    }

    public List<BudgetItemRequestResponse> getBudgetItems() {
        return budgetItems;
    }

    @Override
    public String toString() {
        return "AddBudgetItemsRequest{" +
                "userId=" + userId +
                ", budgetItems=" + budgetItems +
                '}';
    }
}
