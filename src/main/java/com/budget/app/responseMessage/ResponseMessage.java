package com.budget.app.responseMessage;

public enum ResponseMessage {

    USER_CREATION_REQUEST("Creating new user"),
    USER_CREATION_ERROR("Error while creating user"),
    USER_CREATION_SUCCESS("User account has been created successfully"),
    USER_AUTHENTICATION_REQUEST("Request received for authenticating user"),
    USER_AUTHENTICATED("User authenticated. Logging in as: "),
    BAD_CREDENTIALS("Username or password is not correct"),
    USER_AUTHENTICATION_FAILURE("Error while authenticating user"),
    FORGOT_PASSWORD_REQUEST("Forgot password request received for user: "),
    USER_NOT_FOUND("No user found with the given email"),
    FORGOT_PASSWORD_DETAILS_MISMATCH("Details provided is incorrect"),
    PASSWORD_RESET_SUCCESS("Password has been successfully changed"),
    PASSWORD_RESET_FAILURE("Error while changing password"),
    UPDATE_USER_REQUEST("Request received for updating user with id: "),
    USER_WITH_ID_NOT_FOUND("No user found with the given id"),
    UPDATE_USER_SUCCESS("User details has been successfully updated"),
    UPDATE_USER_FAILURE("Error while updating user detail"),
    UPDATE_PASSWORD_REQUEST("Request received for updating password for user with id: "),
    GET_USER_DETAILS_REQUEST("Fetching user details for user with id: "),
    GET_USER_DETAILS_FAILURE("Error while fetching user details"),
    GET_USER_DETAILS_SUCCESS("Returning user details"),
    OLD_PASSWORD_MISMATCH("Old password does not match"),
    USER_ALREADY_PRESENT("User already present. Please try again"),

    GET_BUDGET_TYPES_REQUEST("Request received for fetching all budget types"),
    GET_BUDGET_TYPES_FAILURE("Error while fetching budget types "),

    ADD_CATEGORY_REQUEST("Request received for adding category "),
    BUDGET_TYPE_NOT_FOUND("No such budget type found"),
    ADD_CATEGORY_FAILURE("Error while adding category"),
    ADD_CATEGORY_SUCCESS("Category has been successfully added for the given budget type"),
    GET_CATEGORIES_REQUEST("Request received for fetching all categories"),
    GET_CATEGORIES_SUCCESS("Returning fetched categories"),
    GET_CATEGORIES_FAILURE("Error while fetching categories "),
    CATEGORY_NOT_FOUND("No such category found"),

    ADD_BUDGET_ITEMS_REQUEST("Request received for adding budget items "),
    ADD_BUDGET_ITEMS_FAILURE("Error while adding budget items "),
    ADD_BUDGET_ITEMS_SUCCESS("Budget items have been successfully added"),
    GET_BUDGET_ITEMS_REQUEST("Request received for fetching budget items for userId: "),
    GET_BUDGET_ITEMS_SUCCESS("Returning budget items for the user"),
    GET_BUDGET_ITEMS_FAILURE("Error while fetching budget items"),
    GET_MONTHLY_BUDGET_OVERVIEW_REQUEST("Request received for fetching monthly budget overview for user with id: "),
    GET_MONTHLY_BUDGET_OVERVIEW_FAILURE("Error while fetching monthly budget overview"),
    GET_MONTHLY_BUDGET_OVERVIEW_SUCCESS("Returning monthly budget overview"),
    UPDATE_BUDGET_ITEM_REQUEST("Request received for updating budget item with id: "),
    UPDATE_BUDGET_ITEM_SUCCESS("Budget item updated successfully"),
    UPDATE_BUDGET_ITEM_FAILURE("Error while updating budget items"),
    BUDGET_ITEM_NOT_FOUND("No such budget item found"),
    DELETE_BUDGET_ITEM_REQUEST("Request received for deleting budget items"),
    DELETE_BUDGET_ITEM_SUCCESS("Budget items have been successfully deleted"),
    DELETE_BUDGET_ITEM_FAILURE("Error while deleting budget items"),

    SERVER_ERROR("Something went wrong. Please try again");

    public final String label;

    private ResponseMessage(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }

}
