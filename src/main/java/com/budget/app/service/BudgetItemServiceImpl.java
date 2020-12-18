package com.budget.app.service;

import com.budget.app.entity.BudgetItem;
import com.budget.app.entity.BudgetType;
import com.budget.app.entity.Category;
import com.budget.app.entity.User;
import com.budget.app.exceptions.NotFoundException;
import com.budget.app.model.budgetItem.AddBudgetItemsRequest;
import com.budget.app.model.budgetItem.BudgetItemResponse;
import com.budget.app.model.budgetItem.GetBudgetItemsResponse;
import com.budget.app.repository.BudgetItemRepository;
import com.budget.app.repository.BudgetTypeRepository;
import com.budget.app.repository.CategoryRepository;
import com.budget.app.repository.UserRepository;
import com.budget.app.responseMessage.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    private BudgetItemRepository budgetItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(BudgetTypeServiceImpl.class);

    private static final String className = "[BudgetItemServiceImpl] - ";

    @Override
    @Transactional
    public void addBudgetItems(AddBudgetItemsRequest request) throws Exception {

        logger.info(className + ResponseMessage.ADD_BUDGET_ITEMS_REQUEST.toString() + request);

        List<BudgetItem> budgetItems = new ArrayList<>();

        try {

            // extracting user optional from request
            Optional<User> userPresent = userRepository.findById(request.getUserId());

            // checking if the user is present
            // else throwing NotFoundException
            if(!userPresent.isPresent()) {
                throw new NotFoundException(ResponseMessage.USER_NOT_FOUND.toString());
            }

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
                        budgetItem.setUser(userPresent.get());

                        budgetItems.add(budgetItem);

                    });

            budgetItemRepository.saveAll(budgetItems);

            logger.info(className + ResponseMessage.ADD_BUDGET_ITEMS_SUCCESS.toString());

        } catch (Exception e) {
            logger.error(className + ResponseMessage.ADD_BUDGET_ITEMS_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public GetBudgetItemsResponse getBudgetItems(int userId, LocalDate startDate, LocalDate endDate, String type, String sortBy, String orderBy, int page, int limit) throws Exception {

        logger.info(className + ResponseMessage.GET_BUDGET_ITEMS_REQUEST.toString() + userId);

        GetBudgetItemsResponse response = null;

        try {

            Pageable pageable = PageRequest.of(page, limit, orderBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

            List<BudgetItemResponse> budgetItems = budgetItemRepository.getBudgetItems(userId, startDate, endDate, type.toUpperCase(), pageable);

            int totalCount = budgetItemRepository.findBudgetItemsCount(userId, startDate, endDate, type.toUpperCase());

            response = new GetBudgetItemsResponse();
            response.setBudgetItems(budgetItems);
            response.setBudgetType(type);
            response.setTotalCount(totalCount);

            logger.info(className + ResponseMessage.GET_BUDGET_ITEMS_SUCCESS);

        } catch (Exception e) {
            logger.error(className + ResponseMessage.GET_BUDGET_ITEMS_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        return response;
    }
}
