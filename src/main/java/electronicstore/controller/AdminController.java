package electronicstore.controller;

import electronicstore.model.users.Administrator;
import electronicstore.model.users.User;

public class AdminController {
    private Administrator admin;

    public AdminController(Administrator admin) {
        this.admin = admin;
    }

    public void addEmployee(User employee) {
        try {
            admin.registerEmployee(employee);
        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }

    public void modifyEmployee(String username, User updatedEmployee) {
        admin.modifyEmployee(username, updatedEmployee);
    }

    public void deleteEmployee(String username) {
        admin.deleteEmployee(username);
    }

    public Administrator getAdmin() {
        return admin;
    }
}