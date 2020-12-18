package com.budget.app.controller;

import com.budget.app.model.Response;
import com.budget.app.model.budgetItem.AddBudgetItemsRequest;
import com.budget.app.model.budgetItem.GetBudgetItemsResponse;
import com.budget.app.model.budgetItem.MonthlyBudgetOverview;
import com.budget.app.model.budgetItem.UpdateBudgetItemRequest;
import com.budget.app.responseMessage.ResponseMessage;
import com.budget.app.service.BudgetItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/budget")
public class BudgetItemController {

    @Autowired
    private BudgetItemService budgetItemService;

    private static final Logger logger = LoggerFactory.getLogger(BudgetItemController.class);

    private static final String className = "[BudgetItemController] - ";

    @PostMapping
    public ResponseEntity<Response> addBudgetItems(@RequestBody AddBudgetItemsRequest request) throws Exception {

        logger.info(className + ResponseMessage.ADD_BUDGET_ITEMS_REQUEST.toString() + request);

        Response response = null;

        try {
            budgetItemService.addBudgetItems(request);
            logger.info(className + ResponseMessage.ADD_BUDGET_ITEMS_SUCCESS.toString());
        } catch (Exception e) {
            logger.error(className + ResponseMessage.ADD_BUDGET_ITEMS_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        response = new Response(HttpStatus.OK.value(), ResponseMessage.ADD_BUDGET_ITEMS_SUCCESS.toString(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBudgetItemsResponse> getBudgetItems(
            @PathVariable("id") int userId,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("type") String type,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("orderBy") String orderBy,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) throws Exception {

        logger.info(className + ResponseMessage.GET_BUDGET_ITEMS_REQUEST.toString() + userId);
        GetBudgetItemsResponse response = null;

        try {

            LocalDate startDate = LocalDate.parse(fromDate);
            LocalDate endDate = LocalDate.parse(toDate);

            response = budgetItemService.getBudgetItems(userId, startDate, endDate, type, sortBy, orderBy, page, limit);

            logger.info(className + ResponseMessage.GET_BUDGET_ITEMS_SUCCESS);

        } catch (Exception e) {
            logger.error(className + ResponseMessage.GET_BUDGET_ITEMS_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/monthly/{id}")
    public ResponseEntity<MonthlyBudgetOverview> getMonthlyBudgetOverview(@PathVariable("id") int userId) throws Exception {

        logger.info(className + ResponseMessage.GET_MONTHLY_BUDGET_OVERVIEW_REQUEST.toString());

        MonthlyBudgetOverview response = null;

        try {
            response = budgetItemService.getMonthlyBudgetOverview(userId);

            logger.info(className + ResponseMessage.GET_MONTHLY_BUDGET_OVERVIEW_SUCCESS.toString());
        } catch (Exception e) {
            logger.error(className + ResponseMessage.GET_MONTHLY_BUDGET_OVERVIEW_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<Response> updateBudgetItem(@PathVariable("budgetId") int budgetId, @RequestBody UpdateBudgetItemRequest request) throws Exception {

        logger.info(className + ResponseMessage.UPDATE_BUDGET_ITEM_REQUEST.toString());

        Response response = null;

        try {
            budgetItemService.updateBudgetItem(budgetId, request);

            logger.info(className + ResponseMessage.UPDATE_BUDGET_ITEM_SUCCESS.toString());
        } catch (Exception e) {
            logger.error(className + ResponseMessage.UPDATE_BUDGET_ITEM_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        response = new Response(HttpStatus.OK.value(), ResponseMessage.UPDATE_BUDGET_ITEM_SUCCESS.toString(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
