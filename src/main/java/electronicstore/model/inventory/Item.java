package electronicstore.model.inventory;

import electronicstore.model.exceptions.OutOfStockException;

import java.io.Serializable;
import java.time.LocalDate;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemID;
    private String name;
    private Category category;
    private Supplier supplier;
    private Sector sector;
    private LocalDate purchaseDate;
    private double purchasePrice;
    private double sellingPrice;
    private int quantity;
    private double discount;
    private int minStock;

    public Item(String itemID, String name, Category category, Supplier supplier, Sector sector,
                LocalDate purchaseDate, double purchasePrice, double sellingPrice, int quantity) {
        this.itemID = itemID;
        this.name = name;
        this.category = category;
        this.supplier = supplier;
        this.sector = sector;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.discount = 0.0;
        this.minStock = 3;
    }

    public boolean reduceQuantity(int qty) throws OutOfStockException {
        if (quantity < qty) {
            throw new OutOfStockException(name, quantity);
        }
        quantity -= qty;
        return true;
    }

    public void applyDiscount(double percent) {
        this.discount = percent;
    }

    public double getFinalPrice() {
        return sellingPrice * (1 - discount / 100);
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    public boolean needsRestock() {
        return quantity <= minStock;
    }

    
    public String getItemID() { return itemID; }
    public void setItemID(String itemID) { this.itemID = itemID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public Sector getSector() { return sector; }
    public void setSector(Sector sector) { this.sector = sector; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public int getMinStock() { return minStock; }
    public void setMinStock(int minStock) { this.minStock = minStock; }
}