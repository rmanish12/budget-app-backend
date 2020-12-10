package com.budget.app.model.budgetItem;

import java.util.List;

public class AddBudgetItemsRequest {

    private int userId;
    private List<BudgetItemRequest> budgetItems;

    public AddBudgetItemsRequest(int userId, List<BudgetItemRequest> budgetItems) {
        this.userId = userId;
        this.budgetItems = budgetItems;
    }

    public int getUserId() {
        return userId;
    }

    public List<BudgetItemRequest> getBudgetItems() {
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
