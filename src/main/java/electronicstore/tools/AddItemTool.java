package electronicstore.tools;

import electronicstore.model.inventory.Category;
import electronicstore.model.inventory.Item;
import electronicstore.model.inventory.Sector;
import electronicstore.model.inventory.Supplier;
import electronicstore.model.utilities.FileManager;
import electronicstore.model.utilities.IDGenerator;

import java.time.LocalDate;
import java.util.List;

public class AddItemTool {
    public static void main(String[] args) {
        try {
            List<Item> items = FileManager.loadItems();
            List<Supplier> suppliers = FileManager.loadSuppliers();
            List<Sector> sectors = FileManager.loadSectors();

            String targetId = "ITEM-00003";

            
            Item existing = null;
            for (Item it : items) {
                if (it.getItemID().equals(targetId)) { existing = it; break; }
            }

            if (existing != null) {
                System.out.println("Found existing item with ID " + targetId + ". Increasing quantity by 5.");
                existing.setQuantity(existing.getQuantity() + 5);
            } else {
                System.out.println("ITEM-00003 not found — creating new item.");
                Category cat = new Category(IDGenerator.generateCategoryID(), "General", "");
                Supplier supp = new Supplier(IDGenerator.generateSupplierID(), "Default Supplier", "", "", "", "");
                Sector sect = new Sector(IDGenerator.generateSectorID(), "General", "");

                Item newItem = new Item(targetId, "Sample Item 3", cat, supp, sect,
                        LocalDate.now(), 10.0, 15.0, 10);

                boolean supExists = false;
                for (Supplier s : suppliers) {
                    if (s.getSupplierID().equals(supp.getSupplierID())) {
                        supExists = true;
                        break;
                    }
                }
                if (!supExists) suppliers.add(supp);

                boolean sectExists = false;
                for (Sector s : sectors) {
                    if (s.getSectorID().equals(sect.getSectorID())) {
                        sectExists = true;
                        break;
                    }
                }
                if (!sectExists) sectors.add(sect);

                items.add(newItem);
            }

            FileManager.saveItems(items);
            FileManager.saveSuppliers(suppliers);
            FileManager.saveSectors(sectors);

            System.out.println("Save complete.");

            
            System.out.println("Total items now: " + FileManager.loadItems().size());

        } catch (Exception e) {
            System.err.println("Error in AddItemTool: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
