package com.budget.app.model.budgetItem;

import java.util.List;

public class GetBudgetItemsResponse {

    private String budgetType;
    private int totalCount;
    private List<BudgetItemResponse> budgetItems;

    public GetBudgetItemsResponse() {
    }

    public GetBudgetItemsResponse(String budgetType, int totalCount, List<BudgetItemResponse> budgetItems) {
        this.budgetType = budgetType;
        this.totalCount = totalCount;
        this.budgetItems = budgetItems;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setBudgetItems(List<BudgetItemResponse> budgetItems) {
        this.budgetItems = budgetItems;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<BudgetItemResponse> getBudgetItems() {
        return budgetItems;
    }

    @Override
    public String toString() {
        return "GetBudgetItemsResponse{" +
                "budgetType='" + budgetType + '\'' +
                ", totalCount=" + totalCount +
                ", budgetItems=" + budgetItems +
                '}';
    }
}
