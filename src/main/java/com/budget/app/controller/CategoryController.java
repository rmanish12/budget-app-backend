package com.budget.app.controller;

import com.budget.app.model.Response;
import com.budget.app.model.category.AddCategoryRequest;
import com.budget.app.model.category.GetCategoriesResponse;
import com.budget.app.responseMessage.ResponseMessage;
import com.budget.app.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private static final String className = "[CategoryController] - ";

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Response> addCategory(@RequestBody AddCategoryRequest addCategoryRequest) throws Exception {

        logger.info(className + ResponseMessage.ADD_CATEGORY_REQUEST.toString() + addCategoryRequest.getCategory() + "for type: " + addCategoryRequest.getType());

        Response response = null;

        try {
            categoryService.addCategory(addCategoryRequest);
        } catch (Exception e) {
            logger.error(className + ResponseMessage.ADD_CATEGORY_FAILURE.toString() + " " + e.getMessage(), e);
            throw e;
        }

        response = new Response(HttpStatus.OK.value(), ResponseMessage.ADD_CATEGORY_SUCCESS.toString(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<GetCategoriesResponse> getCategories() throws Exception {

        logger.info(className + ResponseMessage.GET_CATEGORIES_REQUEST.toString());

        GetCategoriesResponse categories = null;

        try {
            categories = categoryService.getCategories();
            logger.info(className + ResponseMessage.GET_CATEGORIES_SUCCESS.toString());
        } catch (Exception e) {
            logger.error(className + ResponseMessage.GET_CATEGORIES_FAILURE.toString() + e.getMessage(), e);
            throw e;
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);

    }

}
