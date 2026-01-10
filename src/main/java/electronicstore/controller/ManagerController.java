package electronicstore.controller;

import electronicstore.model.inventory.Item;
import electronicstore.model.inventory.Supplier;
import electronicstore.model.users.Manager;

public class ManagerController {
    private Manager manager;

    public ManagerController(Manager manager) {
        this.manager = manager;
    }

    public void addItem(Item item) {
        manager.addItemToStock(item);
    }

    public void applyDiscount(String itemID, double percent) {
        try {
            manager.applyDiscount(itemID, percent);
        } catch (Exception e) {
            System.err.println("Error applying discount: " + e.getMessage());
        }
    }

    public void addSupplier(Supplier supplier) {
        manager.addSupplier(supplier);
    }

    public void updateSupplier(Supplier supplier) {
        manager.updateSupplier(supplier);
    }

    public void deleteItem(String itemID) throws Exception {
        manager.deleteItem(itemID);
    }

    public void deleteSupplier(String supplierID) throws Exception {
        manager.deleteSupplier(supplierID);
    }

    public Manager getManager() {
        return manager;
    }
}