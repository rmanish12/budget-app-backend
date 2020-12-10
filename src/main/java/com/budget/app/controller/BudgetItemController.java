package com.budget.app.controller;

import com.budget.app.model.Response;
import com.budget.app.model.budgetItem.AddBudgetItemsRequest;
import com.budget.app.responseMessage.ResponseMessage;
import com.budget.app.service.BudgetItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
