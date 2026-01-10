package electronicstore.model.utilities;

import electronicstore.model.exceptions.FileOperationException;
import electronicstore.model.inventory.*;
import electronicstore.model.transactions.Bill;
import electronicstore.model.users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static void saveToBinaryFile(Object obj, String filename) throws FileOperationException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new FileOperationException(filename, "save", e.getMessage());
        }
    }

    public static Object loadFromBinaryFile(String filename) throws FileOperationException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FileOperationException(filename, "load", e.getMessage());
        }
    }

    public static void saveToTextFile(String content, String filename) throws FileOperationException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.print(content);
        } catch (IOException e) {
            throw new FileOperationException(filename, "save text", e.getMessage());
        }
    }

    public static String loadFromTextFile(String filename) throws FileOperationException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new FileOperationException(filename, "load text", e.getMessage());
        }
        return sb.toString();
    }

    public static void saveBill(Bill bill) throws FileOperationException {
        saveToTextFile(bill.generateBillContent(), "bills/" + bill.getFileName());
    }

    public static void saveBills(List<Bill> bills) throws FileOperationException {
        saveToBinaryFile(bills, "data/bills.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<Bill> loadBills() throws FileOperationException {
        try {
            return (List<Bill>) loadFromBinaryFile("data/bills.dat");
        } catch (FileOperationException e) {
            return new ArrayList<>();
        }
    }

    public static void saveUsers(List<User> users) throws FileOperationException {
        saveToBinaryFile(users, "data/employees.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() throws FileOperationException {
        try {
            return (List<User>) loadFromBinaryFile("data/employees.dat");
        } catch (FileOperationException e) {
            return new ArrayList<>();
        }
    }

    public static void saveItems(List<Item> items) throws FileOperationException {
        saveToBinaryFile(items, "data/items.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<Item> loadItems() throws FileOperationException {
        try {
            return (List<Item>) loadFromBinaryFile("data/items.dat");
        } catch (FileOperationException e) {
            return new ArrayList<>();
        }
    }

    public static void saveCategories(List<Category> categories) throws FileOperationException {
        saveToBinaryFile(categories, "data/categories.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<Category> loadCategories() throws FileOperationException {
        try {
            return (List<Category>) loadFromBinaryFile("data/categories.dat");
        } catch (FileOperationException e) {
            return new ArrayList<>();
        }
    }

    public static void saveSuppliers(List<Supplier> suppliers) throws FileOperationException {
        saveToBinaryFile(suppliers, "data/suppliers.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<Supplier> loadSuppliers() throws FileOperationException {
        try {
            return (List<Supplier>) loadFromBinaryFile("data/suppliers.dat");
        } catch (FileOperationException e) {
            return new ArrayList<>();
        }
    }

    public static void saveSectors(List<Sector> sectors) throws FileOperationException {
        saveToBinaryFile(sectors, "data/sectors.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<Sector> loadSectors() throws FileOperationException {
        try {
            return (List<Sector>) loadFromBinaryFile("data/sectors.dat");
        } catch (FileOperationException e) {
            return new ArrayList<>();
        }
    }
}