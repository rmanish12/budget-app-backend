package com.budget.app.controller;

import com.budget.app.model.budgetType.GetBudgetTypesResponse;
import com.budget.app.responseMessage.ResponseMessage;
import com.budget.app.service.BudgetTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/types")
public class BudgetTypeController {

    private static final Logger logger = LoggerFactory.getLogger(BudgetTypeController.class);

    private static final String className = "[BudgetTypeController] ";

    @Autowired
    private BudgetTypeService budgetTypeService;

    @GetMapping
    public ResponseEntity<GetBudgetTypesResponse> getBudgetTypes() throws Exception {

        logger.info(className + ResponseMessage.GET_BUDGET_TYPES_REQUEST.toString());

        GetBudgetTypesResponse budgetTypes = null;

        try {
            budgetTypes = budgetTypeService.getBudgetTypes();
        } catch (Exception e) {
            logger.error(className + ResponseMessage.GET_BUDGET_TYPES_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        return new ResponseEntity<>(budgetTypes, HttpStatus.OK);

    }

}
