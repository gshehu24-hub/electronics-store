package electronicstore.model.transactions;

import electronicstore.model.inventory.Item;

import java.io.Serializable;

public class BillItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Item item;
    private int quantity;
    private double price;
    private double discount;

    public BillItem(Item item, int quantity, double price, double discount) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public double getSubtotal() {
        return quantity * price * (1 - discount / 100);
    }

    
    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getDiscount() { return discount; }
}