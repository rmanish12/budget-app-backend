package com.budget.app.service;

import com.budget.app.entity.BudgetType;
import com.budget.app.entity.Category;
import com.budget.app.exceptions.NotFoundException;
import com.budget.app.model.category.AddCategoryRequest;
import com.budget.app.model.category.GetCategoriesResponse;
import com.budget.app.repository.BudgetTypeRepository;
import com.budget.app.responseMessage.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private static final String className = "[CategoryServiceImpl] - ";

    @Autowired
    private BudgetTypeRepository budgetTypeRepository;

    @Override
    public void addCategory(AddCategoryRequest categoryRequest) throws Exception {

        logger.info(className + ResponseMessage.ADD_CATEGORY_REQUEST.toString() + categoryRequest.getCategory() + " for type " + categoryRequest.getType());

        try {

            String typeName = categoryRequest.getType().toUpperCase();
            Optional<BudgetType> budgetType = budgetTypeRepository.findByType(typeName);

            // checking if the given budget type is present
            // else throwing NotFoundException
            if(!budgetType.isPresent()) {
                throw new NotFoundException(ResponseMessage.BUDGET_TYPE_NOT_FOUND.toString());
            } else {

                BudgetType type = budgetType.get();

                // adding category to the given budget type
                type.addCategory(categoryRequest.getCategory());
                budgetTypeRepository.save(type);

                logger.info(className + ResponseMessage.ADD_CATEGORY_SUCCESS.toString());
            }

        } catch (Exception e) {
            logger.error(className + ResponseMessage.ADD_CATEGORY_FAILURE.toString() + " " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public GetCategoriesResponse getCategories() throws Exception {

        logger.info(className + ResponseMessage.GET_CATEGORIES_REQUEST.toString());

        GetCategoriesResponse categories = null;

        try {

            String incomeType = "income";
            Optional<BudgetType> incomeBudgetType = budgetTypeRepository.findByType(incomeType.toUpperCase());

            List<Category> income = incomeBudgetType.get().getCategories();

            String expenseType = "expense";
            Optional<BudgetType> expenseBudgetType = budgetTypeRepository.findByType(expenseType.toUpperCase());

            List<Category> expense = expenseBudgetType.get().getCategories();

            categories = new GetCategoriesResponse(income, expense);

            logger.info(className + ResponseMessage.GET_CATEGORIES_SUCCESS.toString());

        } catch (Exception e) {
            logger.error(className + ResponseMessage.GET_CATEGORIES_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        return categories;

    }
}
