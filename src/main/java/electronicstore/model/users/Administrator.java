package electronicstore.model.users;

import electronicstore.model.exceptions.UserAlreadyExistsException;
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
        for (User u : employeeList) {
            if (u.getUsername().equals(employee.getUsername())) {
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

    public double getTotalIncome(LocalDate startDate, LocalDate endDate) {
        
        return 0.0;
    }

    public double getTotalCosts(LocalDate startDate, LocalDate endDate) {
        
        double totalSalaries = employeeList.stream().mapToDouble(User::getSalary).sum();
        
        return totalSalaries;
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
        } catch (Exception e) {
            employeeList = new ArrayList<>();
        }
    }

    public List<User> getEmployeeList() {
        return employeeList;
    }
}