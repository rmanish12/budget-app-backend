package com.budget.app.model.budgetType;

import com.budget.app.entity.BudgetTypes;

import java.util.List;

public class GetBudgetTypesResponse {

    private List<BudgetTypes> budgetTypes;

    public GetBudgetTypesResponse() {
    }

    public GetBudgetTypesResponse(List<BudgetTypes> budgetTypes) {
        this.budgetTypes = budgetTypes;
    }

    public void setBudgetTypes(List<BudgetTypes> budgetTypes) {
        this.budgetTypes = budgetTypes;
    }

    public List<BudgetTypes> getBudgetTypes() {
        return budgetTypes;
    }
}
