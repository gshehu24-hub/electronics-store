package electronicstore.model.users;

import electronicstore.model.exceptions.InvalidCredentialsException;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String username;
    protected String password;
    protected String name;
    protected LocalDate dateOfBirth;
    protected String phoneNumber;
    protected String email;
    protected double salary;
    protected AccessLevel accessLevel;
    protected boolean isActive;

    public User(String username, String password, String name, LocalDate dateOfBirth,
                String phoneNumber, String email, double salary, AccessLevel accessLevel) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.salary = salary;
        this.accessLevel = accessLevel;
        this.isActive = true;
    }

    public abstract void displayDashboard();

    public boolean login(String username, String password) throws InvalidCredentialsException {
        if (this.username.equals(username) && this.password.equals(password) && isActive) {
            return true;
        }
        throw new InvalidCredentialsException("Invalid username or password");
    }

    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public AccessLevel getAccessLevel() { return accessLevel; }
    public void setAccessLevel(AccessLevel accessLevel) { this.accessLevel = accessLevel; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}