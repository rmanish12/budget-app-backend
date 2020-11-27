package com.budget.app.responseMessage;

public enum ResponseMessage {

    USER_CREATION_REQUEST("Creating new user"),
    USER_CREATION_ERROR("Error while creating user"),
    USER_CREATION_SUCCESS("User account has been created successfully"),
    USER_AUTHENTICATION_REQUEST("Request received for authenticating user"),
    USER_AUTHENTICATED("User authenticated. Logging in as: "),
    BAD_CREDENTIALS("Username or password is not correct"),
    USER_AUTHENTICATION_FAILURE("Error while authenticating user"),
    USER_NOT_FOUND("No such user found"),
    TYPE_NOT_FOUND("No such type found"),
    CATEGORY_NOT_FOUND("No such category found"),
    BUDGET_NOT_FOUND("No such budget item found"),
    USER_ALREADY_PRESENT("User with the given id/email is already present. Please try again with another email id"),
    TYPE_ALREADY_PRESENT("The given type is already present"),
    CATEGORY_ALREADY_PRESENT("The given category is already present"),
    BUDGET_ALREADY_PRESENT("The given budget item is already present"),
    SERVER_ERROR("Something went wrong. Please try again"),
    REGISTERED_SUCCESSFULLY("You have successfully registered with us"),
    USER_UPDATED_SUCCESSFULLY("User has been updated successfully"),
    USER_DELETED_SUCCESSFULLY("User has been deleted successfully"),
    TYPE_ADDED_SUCCESSFULLY("Type has been added successfully"),
    TYPE_DELETED_SUCCESSFULLY("Type has been deleted successfully"),
    TYPE_UPDATED_SUCCESSFULLY("Type has been updated successfully"),
    CATEGORY_ADDED_SUCCESSFULLY("Category has been added successfully"),
    CATEGORY_DELETED_SUCCESSFULLY("Category has been deleted successfully"),
    CATEGORY_UPDATED_SUCCESSFULLY("Category has been updated successfully"),
    BUDGET_ADDED_SUCCESSFULLY("Budget has been added successfully"),
    BUDGET_DELETED_SUCCESSFULLY("Budget items has been deleted successfully"),
    BUDGET_UPDATED_SUCCESSFULLY("Budget item has been successfully updated");

    public final String label;

    private ResponseMessage(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }

}
