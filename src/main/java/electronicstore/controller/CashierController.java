package electronicstore.controller;

import electronicstore.model.transactions.Bill;
import electronicstore.model.users.Cashier;

public class CashierController {
    private Cashier cashier;

    public CashierController(Cashier cashier) {
        this.cashier = cashier;
    }

    public boolean addItemToBill(String itemID, int qty) throws Exception {
        return cashier.addItemToBill(itemID, qty);
    }

    public double getBillTotal() {
        return cashier.calculateBillTotal();
    }

    public Bill generateBill() {
        return cashier.generateBill();
    }

    public Cashier getCashier() {
        return cashier;
    }
}