package com.budget.app.model.user;

import java.time.LocalDate;

public class GetUserDetailsResponse {

    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;

    public GetUserDetailsResponse() {
    }

    public GetUserDetailsResponse(String email, String firstName, String lastName, LocalDate dateOfBirth, String gender) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
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
