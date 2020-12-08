package com.budget.app.model.category;

import com.budget.app.entity.Category;

public class AddCategoryRequest {

    private String type;

    private Category category;

    public AddCategoryRequest(String type, Category category) {
        this.type = type;
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }
}
