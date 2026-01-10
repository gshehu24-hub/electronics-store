package electronicstore.model.exceptions;

public class OutOfStockException extends Exception {
    private String itemName;
    private int availableQuantity;

    public OutOfStockException(String itemName, int availableQuantity) {
        super("Item " + itemName + " is out of stock. Available quantity: " + availableQuantity);
        this.itemName = itemName;
        this.availableQuantity = availableQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}