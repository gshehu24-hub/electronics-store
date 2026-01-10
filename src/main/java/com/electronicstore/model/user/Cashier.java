package com.electronicstore.model.user;

import java.util.List;
import java.util.ArrayList;

public class Cashier extends User {
    private List<String> currentDayBills = new ArrayList<>();

    public Cashier(String userId, String username) {
        super(userId, username);
        this.accessLevel = AccessLevel.CASHIER;
    }

    public void createBill() {
        
    }

    public void viewBills() {
        
    }

    public void checkout() {
        
    }

    @Override
    public String getRole() {
        return "Cashier";
    }

    @Override
    public List<String> getPermissions() {
        return List.of("CREATE_BILL", "VIEW_BILLS", "CHECKOUT");
    }
}
