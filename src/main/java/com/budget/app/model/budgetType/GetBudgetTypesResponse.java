package com.budget.app.model.budgetType;

import com.budget.app.entity.BudgetType;

import java.util.List;

public class GetBudgetTypesResponse {

    private List<BudgetType> budgetTypes;

    public GetBudgetTypesResponse() {
    }

    public GetBudgetTypesResponse(List<BudgetType> budgetTypes) {
        this.budgetTypes = budgetTypes;
    }

    public void setBudgetTypes(List<BudgetType> budgetTypes) {
        this.budgetTypes = budgetTypes;
    }

    public List<BudgetType> getBudgetTypes() {
        return budgetTypes;
    }
}
