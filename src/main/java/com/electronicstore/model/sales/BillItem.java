package com.electronicstore.model.sales;

import com.electronicstore.model.inventory.Item;

public class BillItem {
    private Item item;
    private int quantity;
    private double price;

    public BillItem(Item item, int quantity, double price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public double getLineTotal() {
        return price * quantity;
    }
}
