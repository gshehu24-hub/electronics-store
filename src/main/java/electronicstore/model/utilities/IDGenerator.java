package electronicstore.model.utilities;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IDGenerator {
    private static int lastBillNumber = 0;
    private static int lastItemID = 0;
    private static int lastSupplierID = 0;
    private static int lastCategoryID = 0;
    private static int lastSectorID = 0;
    private static int lastUserID = 0;

    private static final String COUNTERS_FILE = "data/counters.dat";

    static {
        loadCounters();
    }

    public static String generateBillNumber() {
        lastBillNumber++;
        saveCounters();
        return String.format("BILL-%05d", lastBillNumber);
    }

    public static String generateItemID() {
        lastItemID++;
        saveCounters();
        // Return a short numeric ID for items (00-99) without the "ITEM-" prefix.
        // We use modulo 100 to keep IDs within 0-99 as requested.
        int shortId = lastItemID % 100;
        return String.format("%02d", shortId);
    }

    public static String generateSupplierID() {
        lastSupplierID++;
        saveCounters();
        return String.format("SUPP-%05d", lastSupplierID);
    }

    public static String generateCategoryID() {
        lastCategoryID++;
        saveCounters();
        return String.format("CAT-%05d", lastCategoryID);
    }

    public static String generateSectorID() {
        lastSectorID++;
        saveCounters();
        return String.format("SECT-%05d", lastSectorID);
    }

    public static String generateUserID() {
        lastUserID++;
        saveCounters();
        return String.format("USER-%05d", lastUserID);
    }

    private static void saveCounters() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COUNTERS_FILE))) {
            Map<String, Integer> counters = new HashMap<>();
            counters.put("lastBillNumber", lastBillNumber);
            counters.put("lastItemID", lastItemID);
            counters.put("lastSupplierID", lastSupplierID);
            counters.put("lastCategoryID", lastCategoryID);
            counters.put("lastSectorID", lastSectorID);
            counters.put("lastUserID", lastUserID);
            oos.writeObject(counters);
        } catch (IOException e) {
            System.err.println("Error saving counters: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadCounters() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COUNTERS_FILE))) {
            Map<String, Integer> counters = (Map<String, Integer>) ois.readObject();
            lastBillNumber = counters.getOrDefault("lastBillNumber", 0);
            lastItemID = counters.getOrDefault("lastItemID", 0);
            lastSupplierID = counters.getOrDefault("lastSupplierID", 0);
            lastCategoryID = counters.getOrDefault("lastCategoryID", 0);
            lastSectorID = counters.getOrDefault("lastSectorID", 0);
            lastUserID = counters.getOrDefault("lastUserID", 0);
        } catch (IOException | ClassNotFoundException e) {
            
        }
    }
}