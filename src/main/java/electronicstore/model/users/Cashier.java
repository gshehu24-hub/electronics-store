package electronicstore.model.users;

import electronicstore.model.exceptions.OutOfStockException;
import electronicstore.model.inventory.Item;
import electronicstore.model.inventory.Sector;
import electronicstore.model.transactions.Bill;
import electronicstore.model.utilities.FileManager;
import electronicstore.model.utilities.IDGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Cashier extends User {
    private static final long serialVersionUID = 1L;

    private List<Sector> sectors;
    private List<Bill> dailyBills;
    private Bill currentBill;

    public Cashier(String username, String password, String name, LocalDate dateOfBirth,
                   String phoneNumber, String email, double salary) {
        super(username, password, name, dateOfBirth, phoneNumber, email, salary, AccessLevel.CASHIER);
        this.sectors = new ArrayList<>();
        this.dailyBills = new ArrayList<>();
        this.currentBill = null;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Cashier Dashboard");
    }

    public Bill createNewBill() {
        currentBill = new Bill(IDGenerator.generateBillNumber(), LocalDate.now(), LocalTime.now(), this);
        return currentBill;
    }

    public boolean addItemToBill(String itemID, int qty) throws OutOfStockException {
        if (currentBill == null) {
            createNewBill();
        }
        for (Sector sector : sectors) {
            for (Item item : sector.getItems()) {
                if (item.getItemID().equals(itemID)) {
                    if (item.getQuantity() < qty) {
                        throw new OutOfStockException(item.getName(), item.getQuantity());
                    }
                    item.reduceQuantity(qty);
                    currentBill.addItem(item, qty, item.getFinalPrice());
                    return true;
                }
            }
        }
        return false;
    }

    public double calculateBillTotal() {
        return currentBill != null ? currentBill.calculateTotal() : 0.0;
    }

    public Bill generateBill() {
        if (currentBill != null && !currentBill.getItems().isEmpty()) {
            try {
                FileManager.saveBill(currentBill);
                
                List<Bill> allBills = FileManager.loadBills();
                allBills.add(currentBill);
                FileManager.saveBills(allBills);
                
                saveInventory(); 
                Bill generatedBill = currentBill;
                dailyBills.add(currentBill);
                currentBill = null;
                return generatedBill;
            } catch (Exception e) {
                System.err.println("Error saving bill: " + e.getMessage());
            }
        }
        return null;
    }

    public List<Bill> viewTodayBills() {
        return new ArrayList<>(dailyBills);
    }

    public int getTotalBillsCount() {
        return dailyBills.size();
    }

    public void loadInventory() {
        try {
            List<Item> items = FileManager.loadItems();
            List<Sector> loadedSectors = FileManager.loadSectors();
            
            
            for (Item item : items) {
                if (item.getSector() != null) {
                    
                    for (Sector sector : loadedSectors) {
                        if (sector.getSectorID().equals(item.getSector().getSectorID())) {
                            sector.getItems().add(item);
                            break;
                        }
                    }
                }
            }
            
            this.sectors = loadedSectors;
            loadTodayBills(); 
        } catch (Exception e) {
            this.sectors = new ArrayList<>();
        }
    }

    public void loadTodayBills() {
        try {
            List<Bill> allBills = FileManager.loadBills();
            LocalDate today = LocalDate.now();
            dailyBills.clear();
            for (Bill bill : allBills) {
                if (bill.getBillDate().equals(today) && bill.getCashier().getUsername().equals(this.getUsername())) {
                    dailyBills.add(bill);
                }
            }
        } catch (Exception e) {
            this.dailyBills = new ArrayList<>();
        }
    }

    public void saveInventory() {
        try {
            List<Item> allItems = new ArrayList<>();
            for (Sector sector : sectors) {
                allItems.addAll(sector.getItems());
            }
            FileManager.saveItems(allItems);
            
            FileManager.saveSectors(sectors);
        } catch (Exception e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    public Bill getCurrentBill() { return currentBill; }
    public List<Sector> getSectors() { return sectors; }
    public void setSectors(List<Sector> sectors) { this.sectors = sectors; }
}