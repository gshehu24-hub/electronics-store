package com.electronicstore.model.user;

import java.util.List;

public class Administrator extends User {

    public Administrator(String userId, String username) {
        super(userId, username);
        this.accessLevel = AccessLevel.ADMIN;
    }

    public void manageUser() {
        
    }

    public void viewReport() {
        
    }

    @Override
    public String getRole() {
        return "Administrator";
    }

    @Override
    public List<String> getPermissions() {
        return List.of("USER_MANAGE", "VIEW_REPORTS");
    }
}
