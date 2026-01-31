package electronicstore.model.users;

import electronicstore.model.exceptions.ItemNotFoundException;
import electronicstore.model.inventory.*;
import electronicstore.model.transactions.Bill;
import electronicstore.model.transactions.BillItem;
import electronicstore.model.transactions.Statistics;
import electronicstore.model.utilities.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Manager extends User {
    private static final long serialVersionUID = 1L;

    private List<Sector> sectors;
    private List<Supplier> suppliers;

    public Manager(String username, String password, String name, LocalDate dateOfBirth,
                   String phoneNumber, String email, double salary) {
        super(username, password, name, dateOfBirth, phoneNumber, email, salary, AccessLevel.MANAGER);
        this.sectors = new ArrayList<>();
        this.suppliers = new ArrayList<>();
    }

    @Override
    public void displayDashboard() {
        System.out.println("Manager Dashboard");
    }

    public void addItemToStock(Item item) {
        
        if (item == null) return;

        Sector itemSector = item.getSector();
        if (itemSector != null) {
            
            Sector found = null;
            for (Sector sector : sectors) {
                if (sector.getSectorID().equals(itemSector.getSectorID())) {
                    found = sector;
                    break;
                }
            }
            if (found == null) {
                
                itemSector.setItems(new java.util.ArrayList<>());
                sectors.add(itemSector);
                found = itemSector;
            }
            
            boolean merged = false;
            for (Item existing : found.getItems()) {
                if (existing.getItemID().equals(item.getItemID())) {
                    
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    existing.setSellingPrice(item.getSellingPrice());
                    existing.setPurchasePrice(item.getPurchasePrice());
                    existing.setPurchaseDate(item.getPurchaseDate());
                    existing.setSupplier(item.getSupplier());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                found.getItems().add(item);
            }
        } else {
            
            if (sectors.isEmpty()) {
                Sector defaultSector = new Sector("SEC-000", "Default", "Default sector");
                defaultSector.getItems().add(item);
                sectors.add(defaultSector);
            } else {
                sectors.get(0).getItems().add(item);
            }
        }

        
        if (item.getSupplier() != null) {
            boolean exists = false;
            for (Supplier s : suppliers) {
                if (s.getSupplierID().equals(item.getSupplier().getSupplierID())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) suppliers.add(item.getSupplier());
        }

        saveInventory();
    }

    public void addCategory(Category category) {
        
        for (Sector sector : sectors) {
            sector.getItems().addAll(category.getItems());
        }
        saveInventory();
    }

    public void applyDiscount(String itemID, double percent) throws ItemNotFoundException {
        if (sectors == null) {
            throw new ItemNotFoundException(itemID);
        }
        for (Sector sector : sectors) {
            if (sector == null || sector.getItems() == null) {
                continue;
            }
            for (Item item : sector.getItems()) {
                if (item != null && item.getItemID().equals(itemID)) {
                    item.applyDiscount(percent);
                    saveInventory();
                    return;
                }
            }
        }
        throw new ItemNotFoundException(itemID);
    }

    public List<Item> checkStockAlerts() {
        List<Item> lowStockItems = new ArrayList<>();
        if (sectors == null) {
            return lowStockItems;
        }
        for (Sector sector : sectors) {
            if (sector != null) {
                lowStockItems.addAll(sector.getLowStockItems());
            }
        }
        return lowStockItems;
    }

    public Statistics viewCashierPerformance(String cashierUsername, LocalDate startDate, LocalDate endDate) {
        Statistics stats = new Statistics(startDate, endDate, cashierUsername);
        try {
            List<Bill> bills = FileManager.loadBills();
            int totalBills = 0;
            int totalItems = 0;
            double totalRevenue = 0.0;
            
            for (Bill bill : bills) {
                if (bill.getCashier().getUsername().equals(cashierUsername) &&
                    !bill.getBillDate().isBefore(startDate) &&
                    !bill.getBillDate().isAfter(endDate)) {
                    totalBills++;
                    totalRevenue += bill.getTotalAmount();
                    for (BillItem item : bill.getItems()) {
                        totalItems += item.getQuantity();
                    }
                }
            }
            
            stats.setTotalBills(totalBills);
            stats.setItemsSold(totalItems);
            stats.setTotalRevenue(totalRevenue);
        } catch (Exception e) {
            System.err.println("Error calculating statistics: " + e.getMessage());
        }
        return stats;
    }

    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        try {
            bills = FileManager.loadBills();
        } catch (Exception e) {
            System.err.println("Error loading bills: " + e.getMessage());
        }
        return bills;
    }

    public List<Bill> getBillsForSector(LocalDate startDate, LocalDate endDate) {
        List<Bill> sectorBills = new ArrayList<>();
        try {
            List<Bill> allBills = FileManager.loadBills();
            List<User> allUsers = FileManager.loadUsers();
            
            List<String> cashierNames = new ArrayList<>();
            for (User user : allUsers) {
                if (user instanceof Cashier) {
                    Cashier cashier = (Cashier) user;
                    for (Sector cashierSector : cashier.getSectors()) {
                        for (Sector mySector : sectors) {
                            if (cashierSector.getSectorID().equals(mySector.getSectorID())) {
                                cashierNames.add(cashier.getUsername());
                                break;
                            }
                        }
                    }
                }
            }
            
            for (Bill bill : allBills) {
                if (cashierNames.contains(bill.getCashier().getUsername()) &&
                    !bill.getBillDate().isBefore(startDate) &&
                    !bill.getBillDate().isAfter(endDate)) {
                    sectorBills.add(bill);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading sector bills: " + e.getMessage());
        }
        return sectorBills;
    }

    public List<String> getCashiersInSector() {
        List<String> cashierUsernames = new ArrayList<>();
        try {
            List<User> allUsers = FileManager.loadUsers();
            for (User user : allUsers) {
                if (user instanceof Cashier) {
                    Cashier cashier = (Cashier) user;
                    for (Sector cashierSector : cashier.getSectors()) {
                        for (Sector mySector : sectors) {
                            if (cashierSector.getSectorID().equals(mySector.getSectorID())) {
                                if (!cashierUsernames.contains(cashier.getUsername())) {
                                    cashierUsernames.add(cashier.getUsername());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading cashiers: " + e.getMessage());
        }
        return cashierUsernames;
    }

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
        saveInventory();
    }

    public void updateSupplier(Supplier supplier) {
        
        saveInventory();
    }

    public void deleteItem(String itemID) throws ItemNotFoundException {
        if (sectors == null) {
            throw new ItemNotFoundException(itemID);
        }
        for (Sector sector : sectors) {
            if (sector == null || sector.getItems() == null) {
                continue;
            }
            for (Item item : sector.getItems()) {
                if (item != null && item.getItemID().equals(itemID)) {
                    sector.getItems().remove(item);
                    saveInventory();
                    return;
                }
            }
        }
        throw new ItemNotFoundException(itemID);
    }

    public void deleteSupplier(String supplierID) throws Exception {
        Supplier toRemove = null;
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierID().equals(supplierID)) {
                toRemove = supplier;
                break;
            }
        }
        if (toRemove != null) {
            suppliers.remove(toRemove);
            saveInventory();
        } else {
            throw new Exception("Supplier not found: " + supplierID);
        }
    }

    public void saveInventory() {
        try {
            if (sectors == null) {
                sectors = new ArrayList<>();
            }
            if (suppliers == null) {
                suppliers = new ArrayList<>();
            }
            List<Item> allItems = new ArrayList<>();
            for (Sector sector : sectors) {
                if (sector != null && sector.getItems() != null) {
                    allItems.addAll(sector.getItems());
                }
            }
            FileManager.saveItems(allItems);
            FileManager.saveSuppliers(suppliers);
            FileManager.saveSectors(sectors);
        } catch (Exception e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    public void loadInventory() {
        try {
            suppliers = FileManager.loadSuppliers();
            sectors = FileManager.loadSectors();
            
            if (suppliers == null) suppliers = new ArrayList<>();
            if (sectors == null) sectors = new ArrayList<>();
            
            for (Sector s : sectors) {
                if (s.getItems() == null) {
                    s.setItems(new ArrayList<>());
                }
                for (Item item : s.getItems()) {
                    if (item.getSupplier() != null) {
                        boolean foundSup = false;
                        for (Supplier sup : suppliers) {
                            if (sup.getSupplierID().equals(item.getSupplier().getSupplierID())) {
                                foundSup = true;
                                break;
                            }
                        }
                        if (!foundSup) {
                            suppliers.add(item.getSupplier());
                        }
                    }
                }
            }
        } catch (Exception e) {
            sectors = new ArrayList<>();
            suppliers = new ArrayList<>();
        }
    }

    public List<Sector> getSectors() { 
        if (sectors == null) {
            sectors = new ArrayList<>();
        }
        return sectors; 
    }
    
    public List<Supplier> getSuppliers() { 
        if (suppliers == null) {
            suppliers = new ArrayList<>();
        }
        return suppliers; 
    }
}