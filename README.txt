# Electronic Store Management System

A comprehensive JavaFX-based application for managing an electronic store's inventory, sales, and user roles. This project implements a multi-level user system with features for administrators, managers, and cashiers to handle daily operations efficiently.

## Features

### User Management
- **Multi-level Authentication**: Separate roles for Administrator, Manager, and Cashier with different access levels
- **Secure Login System**: Username/password authentication with role-based permissions

### Inventory Management
- **Product Catalog**: Add, update, and manage electronic items with categories and suppliers
- **Stock Tracking**: Real-time inventory monitoring with low stock alerts (threshold: 3 items)
- **Supplier Management**: Maintain supplier information and relationships

### Sales and Billing
- **Bill Generation**: Automated bill creation with item selection and quantity management
- **Stock Updates**: Automatic inventory reduction upon successful sales
- **Bill Storage**: Persistent storage of bills in text format with standardized naming

### Reporting and Analytics
- **Financial Reports**: Sales and revenue tracking
- **Performance Statistics**: User and system performance metrics
- **Sector-based Organization**: Items organized by store sectors for better management

### Additional Features
- **MVC Architecture**: Clean separation of Model, View, and Controller components
- **Data Persistence**: Binary files for system data, text files for bills
- **User-friendly GUI**: Intuitive JavaFX interface for all operations

## Technologies Used
- **Java**: Core programming language
- **JavaFX**: GUI framework for desktop application
- **Maven**: Build automation and dependency management
- **Git**: Version control

## Prerequisites
- Java 11 or higher
- Maven 3.6+
- JavaFX SDK (included in project or install separately)

## Usage

### Login Credentials
- **Administrator**: username=`admin`, password=`admin123`
- **Manager/Cashier**: Create accounts through Administrator panel

### Application Workflow
1. **Launch** the application by running `Main.java`
2. **Login** with appropriate credentials
3. **Administrator**:
   - Create and manage Manager/Cashier accounts
   - View system-wide reports
4. **Manager**:
   - Add/edit inventory items
   - Manage suppliers and categories
   - View sales reports
5. **Cashier**:
   - Process customer bills
   - Update inventory through sales

### Key Operations
- **Inventory Management**: Add new items with category, supplier, price, and stock
- **Bill Creation**: Select items, specify quantities, generate printable bills
- **Stock Alerts**: Automatic notifications when items fall below threshold
- **Reports**: Access financial and performance data

## Project Structure
```
ElectronicStore/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── electronicstore/
│   │   │       ├── Main.java
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── service/
│   │   │       ├── util/
│   │   │       └── view/
│   │   └── resources/
│   │       └── styles.css
│   └── test/
├── javafx/
│   ├── lib/
│   └── legal/
├── data/
├── bills/
├── diagrams/
├── scripts/
├── target/
├── .gitignore
└── README.md
```

## Design Decisions

### Architecture
- **MVC Pattern**: Ensures separation of concerns and maintainable code
- **Layered Design**: Clear distinction between presentation, business logic, and data layers

### Data Storage
- **Binary Files**: For system data (items, users, suppliers) for efficiency
- **Text Files**: For bills to ensure readability and portability
- **File Naming Convention**: `BILL-[number]_[YYYY-MM-DD].txt`

### User Experience
- **Role-based Access**: Prevents unauthorized operations
- **Intuitive GUI**: JavaFX components for modern desktop experience
- **Real-time Updates**: Immediate reflection of changes in the interface

### Performance
- **Stock Threshold**: Set to 3 items for proactive inventory management
- **Efficient Data Structures**: Optimized for quick searches and updates

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.

---

**Note**: This project was developed as part of a school assignment demonstrating software engineering principles, GUI development, and data management in Java.