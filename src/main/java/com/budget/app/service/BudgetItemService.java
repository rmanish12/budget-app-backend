package com.budget.app.service;

import com.budget.app.entity.BudgetItem;
import com.budget.app.model.budgetItem.AddBudgetItemsRequest;

import java.util.List;

public interface BudgetItemService {

    public void addBudgetItems(AddBudgetItemsRequest request) throws Exception;

}
