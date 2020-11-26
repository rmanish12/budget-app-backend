package com.budget.app.model.user;

public class UserInfoOnLogin {

    private int id;
    private String firstName;
    private String role;

    public UserInfoOnLogin() {
    }

    public UserInfoOnLogin(int id, String firstName, String role) {
        this.id = id;
        this.firstName = firstName;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
