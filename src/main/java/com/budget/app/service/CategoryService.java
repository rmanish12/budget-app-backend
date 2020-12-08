package com.budget.app.service;

import com.budget.app.model.category.AddCategoryRequest;
import com.budget.app.model.category.GetCategoriesResponse;

public interface CategoryService {

    public void addCategory(AddCategoryRequest addCategoryRequest) throws Exception;
    public GetCategoriesResponse getCategories() throws Exception;

}
