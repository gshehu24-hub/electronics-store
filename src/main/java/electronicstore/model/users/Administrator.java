package electronicstore.model.users;

import electronicstore.model.exceptions.UserAlreadyExistsException;
import electronicstore.model.inventory.Item;
import electronicstore.model.inventory.Sector;
import electronicstore.model.transactions.Bill;
import electronicstore.model.transactions.BillItem;
import electronicstore.model.transactions.Report;
import electronicstore.model.utilities.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Administrator extends User {
    private static final long serialVersionUID = 1L;

    private List<User> employeeList;

    public Administrator(String username, String password, String name, LocalDate dateOfBirth,
                        String phoneNumber, String email, double salary) {
        super(username, password, name, dateOfBirth, phoneNumber, email, salary, AccessLevel.ADMINISTRATOR);
        this.employeeList = new ArrayList<>();
    }

    @Override
    public void displayDashboard() {
        System.out.println("Administrator Dashboard");
    }

    public void registerEmployee(User employee) throws UserAlreadyExistsException {
        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }
        for (User u : employeeList) {
            if (u != null && u.getUsername().equals(employee.getUsername())) {
                throw new UserAlreadyExistsException(employee.getUsername());
            }
        }
        employeeList.add(employee);
        saveEmployees();
    }

    public void modifyEmployee(String username, User updatedEmployee) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getUsername().equals(username)) {
                employeeList.set(i, updatedEmployee);
                saveEmployees();
                return;
            }
        }
    }

    public boolean deleteEmployee(String username) {
        for (User u : employeeList) {
            if (u.getUsername().equals(username)) {
                employeeList.remove(u);
                saveEmployees();
                return true;
            }
        }
        return false;
    }

    public void setEmployeeActive(String username, boolean active) {
        for (User u : employeeList) {
            if (u.getUsername().equals(username)) {
                u.setActive(active);
                saveEmployees();
                return;
            }
        }
    }

    public double getTotalIncome(LocalDate startDate, LocalDate endDate) {
        double totalIncome = 0.0;
        try {
            List<Bill> bills = FileManager.loadBills();
            if (bills != null) {
                for (Bill bill : bills) {
                    if (bill.getBillDate() != null && 
                        !bill.getBillDate().isBefore(startDate) && 
                        !bill.getBillDate().isAfter(endDate)) {
                        totalIncome += bill.getTotalAmount();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading bills: " + e.getMessage());
        }
        return totalIncome;
    }

    public double getTotalCosts(LocalDate startDate, LocalDate endDate) {
        double totalCosts = 0.0;
        
        if (employeeList != null) {
            for (User u : employeeList) {
                if (u != null) {
                    totalCosts += u.getSalary();
                }
            }
        }
        
        try {
            List<Sector> sectors = FileManager.loadSectors();
            if (sectors != null) {
                for (Sector sector : sectors) {
                    if (sector.getItems() != null) {
                        for (Item item : sector.getItems()) {
                            if (item.getPurchaseDate() != null &&
                                !item.getPurchaseDate().isBefore(startDate) &&
                                !item.getPurchaseDate().isAfter(endDate)) {
                                totalCosts += item.getPurchasePrice() * item.getQuantity();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading inventory: " + e.getMessage());
        }
        
        return totalCosts;
    }

    public Report generateFinancialReport(LocalDate startDate, LocalDate endDate) {
        double income = getTotalIncome(startDate, endDate);
        double costs = getTotalCosts(startDate, endDate);
        return new Report(startDate, endDate, income, costs);
    }

    public void saveEmployees() {
        try {
            FileManager.saveUsers(employeeList);
        } catch (Exception e) {
            System.err.println("Error saving employees: " + e.getMessage());
        }
    }

    public void loadEmployees() {
        try {
            employeeList = FileManager.loadUsers();
            if (employeeList == null) {
                employeeList = new ArrayList<>();
            }
        } catch (Exception e) {
            employeeList = new ArrayList<>();
        }
    }

    public List<User> getEmployeeList() {
        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }
        return employeeList;
    }
}