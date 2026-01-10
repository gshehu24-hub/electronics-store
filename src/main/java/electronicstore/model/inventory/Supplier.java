package electronicstore.model.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    private String supplierID;
    private String name;
    private String contactPerson;
    private String phoneNumber;
    private String email;
    private String address;
    private List<Item> productsSupplied;

    public Supplier(String supplierID, String name, String contactPerson, String phoneNumber,
                    String email, String address) {
        this.supplierID = supplierID;
        this.name = name;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.productsSupplied = new ArrayList<>();
    }

    public void addProduct(Item item) {
        productsSupplied.add(item);
        item.setSupplier(this);
    }

    
    public String getSupplierID() { return supplierID; }
    public void setSupplierID(String supplierID) { this.supplierID = supplierID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Item> getProductsSupplied() { return productsSupplied; }
    public void setProductsSupplied(List<Item> productsSupplied) { this.productsSupplied = productsSupplied; }
}