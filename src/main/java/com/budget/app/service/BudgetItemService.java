package com.budget.app.service;

import com.budget.app.model.budgetItem.AddBudgetItemsRequest;
import com.budget.app.model.budgetItem.GetBudgetItemsResponse;
import com.budget.app.model.budgetItem.MonthlyBudgetOverview;

import java.time.LocalDate;

public interface BudgetItemService {

    void addBudgetItems(AddBudgetItemsRequest request) throws Exception;
    GetBudgetItemsResponse getBudgetItems(int userId, LocalDate startDate, LocalDate endDate, String type, String sortBy, String orderBy, int page, int limit) throws Exception;
    MonthlyBudgetOverview getMonthlyBudgetOverview(int userId) throws Exception;

}
