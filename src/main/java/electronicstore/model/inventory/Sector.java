package electronicstore.model.inventory;

import electronicstore.model.users.Cashier;
import electronicstore.model.users.Manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sector implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sectorID;
    private String sectorName;
    private String description;
    private List<Item> items;
    private List<Manager> managers;
    private List<Cashier> cashiers;

    public Sector(String sectorID, String sectorName, String description) {
        this.sectorID = sectorID;
        this.sectorName = sectorName;
        this.description = description;
        this.items = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.cashiers = new ArrayList<>();
    }

    public List<Item> getLowStockItems() {
        List<Item> lowStock = new ArrayList<>();
        for (Item item : items) {
            if (item.needsRestock()) {
                lowStock.add(item);
            }
        }
        return lowStock;
    }

    
    public String getSectorID() { return sectorID; }
    public void setSectorID(String sectorID) { this.sectorID = sectorID; }

    public String getSectorName() { return sectorName; }
    public void setSectorName(String sectorName) { this.sectorName = sectorName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public List<Manager> getManagers() { return managers; }
    public void setManagers(List<Manager> managers) { this.managers = managers; }

    public List<Cashier> getCashiers() { return cashiers; }
    public void setCashiers(List<Cashier> cashiers) { this.cashiers = cashiers; }
}