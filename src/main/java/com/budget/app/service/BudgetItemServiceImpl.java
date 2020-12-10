package com.budget.app.service;

import com.budget.app.entity.BudgetItem;
import com.budget.app.entity.BudgetType;
import com.budget.app.entity.Category;
import com.budget.app.entity.User;
import com.budget.app.exceptions.NotFoundException;
import com.budget.app.model.budgetItem.AddBudgetItemsRequest;
import com.budget.app.model.budgetItem.BudgetItemRequest;
import com.budget.app.repository.BudgetTypeRepository;
import com.budget.app.repository.CategoryRepository;
import com.budget.app.repository.UserRepository;
import com.budget.app.responseMessage.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetItemServiceImpl implements BudgetItemService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetTypeRepository budgetTypeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(BudgetTypeServiceImpl.class);

    private static final String className = "[BudgetItemServiceImpl] - ";

    @Override
    @Transactional
    public void addBudgetItems(AddBudgetItemsRequest request) throws Exception {

        logger.info(className + ResponseMessage.ADD_BUDGET_ITEMS_REQUEST.toString() + request);

        try {

            // extracting user optional from request
            Optional<User> userPresent = userRepository.findById(request.getUserId());

            // checking if the user is present
            // else throwing NotFoundException
            if(!userPresent.isPresent()) {
                throw new NotFoundException(ResponseMessage.USER_NOT_FOUND.toString());
            }

            // extracting user object
            User user = userPresent.get();

            request
                    .getBudgetItems()
                    .stream()
                    .forEach(item -> {

                        // extracting budget type optional from each budget item
                        Optional<BudgetType> budgetType = budgetTypeRepository.findById(item.getTypeId());

                        if(!budgetType.isPresent()) {
                            throw new NotFoundException(ResponseMessage.BUDGET_TYPE_NOT_FOUND.toString());
                        }

                        // extracting category type optional from each category item
                        Optional<Category> category = categoryRepository.findById(item.getCategoryId());

                        if(!category.isPresent()) {
                            throw new NotFoundException(ResponseMessage.CATEGORY_NOT_FOUND.toString());
                        }

                        BudgetItem budgetItem = new BudgetItem();
                        budgetItem.setAmount(item.getAmount());
                        budgetItem.setDescription(item.getDescription());
                        budgetItem.setDateOfTransaction(item.getDateOfTransaction());
                        budgetItem.setBudgetType(budgetType.get());
                        budgetItem.setCategory(category.get());

                        user.addBudgetItem(budgetItem);
                        userRepository.save(user);

                    });

            logger.info(className + ResponseMessage.ADD_BUDGET_ITEMS_SUCCESS.toString());

        } catch (Exception e) {
            logger.error(className + ResponseMessage.ADD_BUDGET_ITEMS_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

    }
}
