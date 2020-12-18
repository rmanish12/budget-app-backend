package com.budget.app.service;

import com.budget.app.model.budgetItem.AddBudgetItemsRequest;
import com.budget.app.model.budgetItem.GetBudgetItemsResponse;

import java.time.LocalDate;

public interface BudgetItemService {

    public void addBudgetItems(AddBudgetItemsRequest request) throws Exception;
    public GetBudgetItemsResponse getBudgetItems(int userId, LocalDate startDate, LocalDate endDate, String type, String sortBy, String orderBy, int page, int limit) throws Exception;

}
