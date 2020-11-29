package com.budget.app.model.user;

import java.time.LocalDate;

public class ForgotPasswordRequest {

    private String email;
    private LocalDate dateOfBirth;
    private String password;

    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(String email, LocalDate dateOfBirth, String password) {
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
