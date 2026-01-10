package electronicstore.model.transactions;

import electronicstore.model.inventory.Item;

import java.io.Serializable;

public class BillItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Item item;
    private int quantity;
    private double priceAtSale;
    private double discountApplied;

    public BillItem(Item item, int quantity, double priceAtSale, double discountApplied) {
        this.item = item;
        this.quantity = quantity;
        this.priceAtSale = priceAtSale;
        this.discountApplied = discountApplied;
    }

    public double getSubtotal() {
        return quantity * priceAtSale * (1 - discountApplied / 100);
    }

    
    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }
    public double getPriceAtSale() { return priceAtSale; }
    public double getDiscountApplied() { return discountApplied; }
}