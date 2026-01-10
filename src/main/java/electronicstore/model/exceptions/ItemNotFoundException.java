package electronicstore.model.exceptions;

public class ItemNotFoundException extends Exception {
    private String itemID;

    public ItemNotFoundException(String itemID) {
        super("Item with ID " + itemID + " not found.");
        this.itemID = itemID;
    }

    public String getItemID() {
        return itemID;
    }
}