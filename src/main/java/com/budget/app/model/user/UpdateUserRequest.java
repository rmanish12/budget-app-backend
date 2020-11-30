package com.budget.app.model.user;

import java.time.LocalDate;

public class UpdateUserRequest {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;

    public UpdateUserRequest(String firstName, String lastName, LocalDate dateOfBirth, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }
}
