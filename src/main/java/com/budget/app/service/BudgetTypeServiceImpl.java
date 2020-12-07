package com.budget.app.service;

import com.budget.app.entity.BudgetTypes;
import com.budget.app.model.budgetType.GetBudgetTypesResponse;
import com.budget.app.repository.BudgetTypeRepository;
import com.budget.app.responseMessage.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetTypeServiceImpl implements BudgetTypeService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetTypeServiceImpl.class);

    private static final String className = "[BudgetTypeServiceImpl] ";

    @Autowired
    private BudgetTypeRepository budgetTypeRepository;

    @Override
    public GetBudgetTypesResponse getBudgetTypes() throws Exception {

        logger.info(className + ResponseMessage.GET_BUDGET_TYPES_REQUEST.toString());

        GetBudgetTypesResponse budgetTypes;

        try {
            budgetTypes = new GetBudgetTypesResponse(budgetTypeRepository.findAll());
        } catch (Exception e) {
            logger.error(className + ResponseMessage.GET_BUDGET_TYPES_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        return budgetTypes;
    }
}
